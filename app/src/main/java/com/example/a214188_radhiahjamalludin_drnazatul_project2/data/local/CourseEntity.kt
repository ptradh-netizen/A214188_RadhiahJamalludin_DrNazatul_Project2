package com.example.a214188_radhiahjamalludin_drnazatul_project2.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "learning_plan")
data class CourseEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val level: String,
    val icon: Int,
    val lecturer: String,
    val credit: Int,
    val semester: Int
)

@Entity(tableName = "quiz_results")
data class QuizEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    val courseId: Int,
    val courseName: String,
    val score: Int,
    val dateTime: String
)

@Entity(tableName = "favourite_tips")
data class FavouriteTipEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tip: String
)
