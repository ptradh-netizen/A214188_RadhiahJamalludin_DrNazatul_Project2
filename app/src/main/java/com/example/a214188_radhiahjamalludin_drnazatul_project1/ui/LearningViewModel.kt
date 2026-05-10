package com.example.a214188_radhiahjamalludin_drnazatul_project1.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.a214188_radhiahjamalludin_drnazatul_project1.model.Course
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class QuizResult(
    val courseName: String,
    val score: Int,
    val dateTime: String,
    val courseId: Int = -1
)

class LearningViewModel : ViewModel() {
    private val _learningPlan = mutableStateListOf<Course>()
    val learningPlan: List<Course> get() = _learningPlan

    private val _quizHistory = mutableStateListOf<QuizResult>()
    val quizHistory: List<QuizResult> get() = _quizHistory

    val studentName = "Radhiah Jamalludin"
    val matricNumber = "A214188"
    val program = "Bachelor of Software Engineering"

    fun toggleCourse(course: Course) {
        if (_learningPlan.any { it.id == course.id }) {
            _learningPlan.removeIf { it.id == course.id }
        } else {
            _learningPlan.add(course)
        }
    }

    fun addToPlan(course: Course) {
        if (_learningPlan.none { it.id == course.id }) {
            _learningPlan.add(course)
        }
    }

    fun addQuizResult(courseName: String, score: Int) {
        val course = _learningPlan.find { it.name == courseName }
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val currentDate = sdf.format(Date())
        val courseId = course?.id ?: -1
        val newResult = QuizResult(courseName, score, currentDate, courseId)
        
        val index = _quizHistory.indexOfFirst { it.courseId == courseId && it.courseName == courseName }
        if (index != -1) {
            _quizHistory[index] = newResult
        } else {
            _quizHistory.add(newResult)
        }
    }

    fun addQuizResultById(courseId: Int, courseName: String, score: Int) {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val currentDate = sdf.format(Date())
        val newResult = QuizResult(courseName, score, currentDate, courseId)
        
        val index = _quizHistory.indexOfFirst { it.courseId == courseId }
        if (index != -1) {
            _quizHistory[index] = newResult
        } else {
            _quizHistory.add(newResult)
        }
    }

    fun getAverageScore(): Double {
        if (_quizHistory.isEmpty()) return 0.0
        return _quizHistory.map { it.score }.average()
    }

    fun getHighestScore(): Int {
        if (_quizHistory.isEmpty()) return 0
        return _quizHistory.maxOf { it.score }
    }

    fun getTotalCredits(): Int {
        return _learningPlan.sumOf { it.credit }
    }

    fun resetPlan() {
        _learningPlan.clear()
        _quizHistory.clear()
    }
    
    fun isCourseCompleted(courseId: Int): Boolean {
        return _quizHistory.any { it.courseId == courseId }
    }

    fun getCourseScore(courseId: Int): Int? {
        return _quizHistory.find { it.courseId == courseId }?.score
    }
}
