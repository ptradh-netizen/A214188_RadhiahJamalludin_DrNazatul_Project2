package com.example.a214188_radhiahjamalludin_drnazatul_lab04.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.a214188_radhiahjamalludin_drnazatul_lab04.model.Course

class LearningViewModel : ViewModel() {
    private val _learningPlan = mutableStateListOf<Course>()
    val learningPlan: List<Course> get() = _learningPlan

    fun addToPlan(course: Course) {
        if (_learningPlan.none { it.id == course.id }) {
            _learningPlan.add(course)
        }
    }

    fun getTotalCredits(): Int {
        return _learningPlan.sumOf { it.credit }
    }

    fun resetPlan() {
        _learningPlan.clear()
    }
}
