package com.example.a214188_radhiahjamalludin_drnazatul_project2.ui

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.a214188_radhiahjamalludin_drnazatul_project2.CourseApplication
import com.example.a214188_radhiahjamalludin_drnazatul_project2.data.ApiService
import com.example.a214188_radhiahjamalludin_drnazatul_project2.data.CourseRepository
import com.example.a214188_radhiahjamalludin_drnazatul_project2.data.DataSource
import com.example.a214188_radhiahjamalludin_drnazatul_project2.data.local.CourseEntity
import com.example.a214188_radhiahjamalludin_drnazatul_project2.data.local.QuizEntity
import com.example.a214188_radhiahjamalludin_drnazatul_project2.data.local.FavouriteTipEntity
import com.example.a214188_radhiahjamalludin_drnazatul_project2.model.Course
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class LeaderboardEntry(
    val studentName: String = "",
    val courseName: String = "",
    val courseId: Int = 0,
    val score: Int = 0,
    val dateTime: String = ""
)

class LearningViewModel(private val repository: CourseRepository) : ViewModel() {

    val studentName = "Radhiah Jamalludin"
    val matricNumber = "A214188"
    val program = "Bachelor of Software Engineering"

    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private var isRestoring = false 

    init {
        syncDataFromCloud()
    }

    // --- Database Flows ---
    val learningPlan: StateFlow<List<CourseEntity>> = repository.allCourses
        .stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = emptyList())

    val quizHistory: StateFlow<List<QuizEntity>> = repository.allQuizResults
        .stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = emptyList())

    // --- Operations ---
    fun toggleCourse(course: Course) {
        viewModelScope.launch {
            val entity = CourseEntity(
                id = course.id, name = course.name, level = course.level, 
                icon = course.icon, lecturer = course.lecturer, 
                credit = course.credit, semester = course.semester
            )
            if (learningPlan.value.any { it.id == course.id }) {
                repository.deleteCourse(entity)
            } else {
                repository.insertCourse(entity)
            }
            syncDataToCloud()
        }
    }

    fun addQuizResultById(courseId: Int, courseName: String, score: Int) {
        viewModelScope.launch {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val currentDate = sdf.format(Date())
            
            val quiz = QuizEntity(courseId = courseId, courseName = courseName, score = score, dateTime = currentDate)
            repository.insertQuizResult(quiz)
            
            syncDataToCloud() 
            syncResultToPublicLeaderboard(courseId, courseName, score, currentDate)
        }
    }

    // --- Cloud Sync: Simpan ke Profil Peribadi ---
    private fun syncDataToCloud() {
        if (isRestoring) return 
        
        viewModelScope.launch {
            try {
                val currentPlan = repository.allCourses.first()
                val currentQuizHistory = repository.allQuizResults.first()
                
                val courseIds = currentPlan.map { it.id }
                val quizData = currentQuizHistory.map { 
                    mapOf("courseId" to it.courseId, "courseName" to it.courseName, "score" to it.score, "dateTime" to it.dateTime)
                }
                
                val data = hashMapOf(
                    "matricNumber" to matricNumber,
                    "studentName" to studentName,
                    "selectedCourseIds" to courseIds,
                    "quizHistory" to quizData,
                    "lastUpdated" to com.google.firebase.Timestamp.now()
                )
                
                firestore.collection("user_plans").document(matricNumber).set(data).await()
                Log.d("FirebaseSync", "Personal profile synced")
            } catch (e: Exception) {
                Log.e("FirebaseSync", "Sync error: ${e.message}")
            }
        }
    }

    // --- Cloud Restore: Pulihkan Data Secara Mendalam ---
    private fun syncDataFromCloud() {
        viewModelScope.launch {
            isRestoring = true
            try {
                Log.d("FirebaseSync", "Attempting recovery for $matricNumber...")
                val document = firestore.collection("user_plans").document(matricNumber).get().await()
                
                if (document.exists()) {
                    val localCourses = repository.allCourses.first()
                    val localQuizzes = repository.allQuizResults.first()

                    // 1. Pulihkan Senarai Kursus (Merge)
                    val cloudCourseIds = document.get("selectedCourseIds") as? List<*> ?: emptyList<Int>()
                    cloudCourseIds.forEach { id ->
                        val cId = (id as? Number)?.toInt() ?: return@forEach
                        if (localCourses.none { it.id == cId }) {
                            DataSource.courses.find { it.id == cId }?.let {
                                repository.insertCourse(CourseEntity(
                                    id = it.id, name = it.name, level = it.level, 
                                    icon = it.icon, lecturer = it.lecturer, 
                                    credit = it.credit, semester = it.semester
                                ))
                            }
                        }
                    }

                    // 2. Pulihkan Rekod Kuiz dari Profil Peribadi (Merge)
                    val cloudQuizzes = document.get("quizHistory") as? List<Map<String, Any>> ?: emptyList()
                    cloudQuizzes.forEach { map ->
                        val cName = map["courseName"] as? String ?: ""
                        val cDate = map["dateTime"] as? String ?: ""
                        
                        if (localQuizzes.none { it.courseName == cName && it.dateTime == cDate }) {
                            repository.insertQuizResult(QuizEntity(
                                courseId = (map["courseId"] as? Number)?.toInt() ?: 0,
                                courseName = cName,
                                score = (map["score"] as? Number)?.toInt() ?: 0,
                                dateTime = cDate
                            ))
                        }
                    }
                }

                // 3. RECOVERY DARI LEADERBOARD: Jika profil peribadi tiada data tertentu
                Log.d("FirebaseSync", "Checking Leaderboard for missing records...")
                val leaderboardSnapshot = firestore.collection("leaderboard")
                    .whereEqualTo("studentName", studentName)
                    .get().await()
                
                val latestQuizzes = repository.allQuizResults.first()
                leaderboardSnapshot.documents.forEach { doc ->
                    val name = doc.getString("courseName") ?: ""
                    val date = doc.getString("dateTime") ?: ""
                    val score = doc.getLong("score")?.toInt() ?: 0
                    
                    if (latestQuizzes.none { it.courseName == name && it.dateTime == date }) {
                        val courseId = doc.getLong("courseId")?.toInt() ?: DataSource.courses.find { it.name == name }?.id ?: 0
                        repository.insertQuizResult(QuizEntity(
                            courseId = courseId, courseName = name, score = score, dateTime = date
                        ))
                    }
                }
                
                // Segerakkan semula profil peribadi selepas merge selesai
                syncDataToCloud()
            } catch (e: Exception) {
                Log.e("FirebaseSync", "Recovery error: ${e.message}")
            } finally {
                isRestoring = false
            }
        }
    }

    private fun syncResultToPublicLeaderboard(courseId: Int, courseName: String, score: Int, date: String) {
        viewModelScope.launch {
            try {
                val entry = LeaderboardEntry(
                    studentName = studentName, courseName = courseName, 
                    courseId = courseId, score = score, dateTime = date
                )
                firestore.collection("leaderboard").add(entry).await()
                fetchLeaderboard() 
            } catch (e: Exception) {
                Log.e("FirebaseDebug", "Leaderboard error: ${e.message}")
            }
        }
    }

    fun resetPlan() {
        viewModelScope.launch {
            repository.deleteAllCourses()
            repository.deleteAllQuizResults()
            repository.deleteAllFavouriteTips()
            try {
                firestore.collection("user_plans").document(matricNumber).delete().await()
                val snapshot = firestore.collection("leaderboard").whereEqualTo("studentName", studentName).get().await()
                for (document in snapshot.documents) { document.reference.delete().await() }
                _leaderboard.value = emptyList()
            } catch (e: Exception) {
                Log.e("FirebaseDebug", "Clear cloud failed: ${e.message}")
            }
        }
    }

    // --- UI State Management ---
    private val _learningTip = mutableStateOf("Tap 'Refresh' to fetch a Learning Tip")
    val learningTip: State<String> = _learningTip

    fun fetchLearningTip() {
        viewModelScope.launch {
            try {
                _learningTip.value = "Connecting..."
                val response = ApiService.getInstance().getLearningTip()
                _learningTip.value = response.slip.advice
            } catch (e: Exception) { _learningTip.value = "Error: ${e.message}" }
        }
    }

    private val _leaderboard = mutableStateOf<List<LeaderboardEntry>>(emptyList())
    val leaderboard: State<List<LeaderboardEntry>> = _leaderboard
    private val _isLeaderboardLoading = mutableStateOf(false)
    val isLeaderboardLoading: State<Boolean> = _isLeaderboardLoading
    private val _leaderboardError = mutableStateOf<String?>(null)
    val leaderboardError: State<String?> = _leaderboardError

    fun fetchLeaderboard() {
        viewModelScope.launch {
            _isLeaderboardLoading.value = true
            try {
                val snapshot = firestore.collection("leaderboard")
                    .orderBy("score", Query.Direction.DESCENDING)
                    .limit(20)
                    .get()
                    .await()
                _leaderboard.value = snapshot.toObjects(LeaderboardEntry::class.java)
            } catch (e: Exception) { _leaderboardError.value = e.message }
            finally { _isLeaderboardLoading.value = false }
        }
    }

    private val _lightLevel = mutableStateOf(0f)
    val lightLevel: State<Float> = _lightLevel
    fun updateLightLevel(level: Float) { _lightLevel.value = level } //live sensor trigger environment

    fun getAverageScore(results: List<QuizEntity>): Double = if (results.isEmpty()) 0.0 else results.map { it.score }.average()
    fun getHighestScore(results: List<QuizEntity>): Int = if (results.isEmpty()) 0 else results.maxOf { it.score }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as CourseApplication
                return LearningViewModel(application.repository) as T
            }
        }
    }

    val favouriteTips: StateFlow<List<FavouriteTipEntity>> = repository.allFavouriteTips
        .stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = emptyList())

    fun saveCurrentTipToFavourites() {
        val tip = _learningTip.value
        if (tip.isNotEmpty() && tip != "Connecting...") {
            viewModelScope.launch { repository.insertFavouriteTip(FavouriteTipEntity(tip = tip)) }
        }
    }
    fun deleteTipFromFavourites(tip: FavouriteTipEntity) { viewModelScope.launch { repository.deleteFavouriteTip(tip) } }
}
