package com.example.a214188_radhiahjamalludin_drnazatul_lab05.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.a214188_radhiahjamalludin_drnazatul_lab05.CourseApplication
import com.example.a214188_radhiahjamalludin_drnazatul_lab05.data.CourseRepository
import com.example.a214188_radhiahjamalludin_drnazatul_lab05.data.local.CourseEntity
import com.example.a214188_radhiahjamalludin_drnazatul_lab05.data.local.QuizEntity
import com.example.a214188_radhiahjamalludin_drnazatul_lab05.model.Course
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Pengurus Data yang menggunakan Room Database
class LearningViewModel(private val repository: CourseRepository) : ViewModel() {

    // Memerhati senarai kursus dari database
    val learningPlan: StateFlow<List<CourseEntity>> = repository.allCourses
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Memerhati sejarah kuiz dari database
    val quizHistory: StateFlow<List<QuizEntity>> = repository.allQuizResults
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val studentName = "Radhiah Jamalludin"
    val matricNumber = "A214188"
    val program = "Bachelor of Software Engineering"

    fun toggleCourse(course: Course) {
        viewModelScope.launch {
            val entity = CourseEntity(
                id = course.id,
                name = course.name,
                level = course.level,
                icon = course.icon,
                lecturer = course.lecturer,
                credit = course.credit,
                semester = course.semester
            )
            if (learningPlan.value.any { it.id == course.id }) {
                repository.deleteCourse(entity)
            } else {
                repository.insertCourse(entity)
            }
        }
    }

    fun addToPlan(course: Course) {
        viewModelScope.launch {
            if (learningPlan.value.none { it.id == course.id }) {
                repository.insertCourse(
                    CourseEntity(
                        id = course.id,
                        name = course.name,
                        level = course.level,
                        icon = course.icon,
                        lecturer = course.lecturer,
                        credit = course.credit,
                        semester = course.semester
                    )
                )
            }
        }
    }

    fun addQuizResultById(courseId: Int, courseName: String, score: Int) {
        viewModelScope.launch {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val currentDate = sdf.format(Date())
            repository.insertQuizResult(
                QuizEntity(
                    courseId = courseId,
                    courseName = courseName,
                    score = score,
                    dateTime = currentDate
                )
            )
        }
    }

    fun getAverageScore(results: List<QuizEntity>): Double {
        if (results.isEmpty()) return 0.0
        return results.map { it.score }.average()
    }

    fun getHighestScore(results: List<QuizEntity>): Int {
        if (results.isEmpty()) return 0
        return results.maxOf { it.score }
    }

    fun getTotalCredits(plan: List<CourseEntity>): Int {
        return plan.sumOf { it.credit }
    }

    fun resetPlan() {
        viewModelScope.launch {
            repository.deleteAllCourses()
            repository.deleteAllQuizResults()
        }
    }

    fun isCourseCompleted(courseId: Int): Boolean {
        return quizHistory.value.any { it.courseId == courseId }
    }

    fun getCourseScore(courseId: Int): Int? {
        return quizHistory.value.find { it.courseId == courseId }?.score
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as CourseApplication
                return LearningViewModel(application.repository) as T
            }
        }
    }
}
