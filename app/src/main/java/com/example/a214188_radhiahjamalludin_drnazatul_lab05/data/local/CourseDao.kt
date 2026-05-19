package com.example.a214188_radhiahjamalludin_drnazatul_lab05.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {
    // Learning Plan
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: CourseEntity)

    @Delete
    suspend fun deleteCourse(course: CourseEntity)

    @Query("SELECT * FROM learning_plan")
    fun getAllCourses(): Flow<List<CourseEntity>>

    @Query("DELETE FROM learning_plan")
    suspend fun deleteAllCourses()

    // Quiz Results
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizResult(quiz: QuizEntity)

    @Query("SELECT * FROM quiz_results")
    fun getAllQuizResults(): Flow<List<QuizEntity>>

    @Query("DELETE FROM quiz_results")
    suspend fun deleteAllQuizResults()
}
