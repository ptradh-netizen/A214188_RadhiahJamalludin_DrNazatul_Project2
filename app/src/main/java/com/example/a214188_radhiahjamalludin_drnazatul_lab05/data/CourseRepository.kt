package com.example.a214188_radhiahjamalludin_drnazatul_lab05.data

import com.example.a214188_radhiahjamalludin_drnazatul_lab05.data.local.CourseDao
import com.example.a214188_radhiahjamalludin_drnazatul_lab05.data.local.CourseEntity
import com.example.a214188_radhiahjamalludin_drnazatul_lab05.data.local.QuizEntity
import kotlinx.coroutines.flow.Flow

class CourseRepository(private val courseDao: CourseDao) {
    // Learning Plan
    val allCourses: Flow<List<CourseEntity>> = courseDao.getAllCourses()
    suspend fun insertCourse(course: CourseEntity) = courseDao.insertCourse(course)
    suspend fun deleteCourse(course: CourseEntity) = courseDao.deleteCourse(course)
    suspend fun deleteAllCourses() = courseDao.deleteAllCourses()

    // Quiz Results
    val allQuizResults: Flow<List<QuizEntity>> = courseDao.getAllQuizResults()
    suspend fun insertQuizResult(quiz: QuizEntity) = courseDao.insertQuizResult(quiz)
    suspend fun deleteAllQuizResults() = courseDao.deleteAllQuizResults()
}
