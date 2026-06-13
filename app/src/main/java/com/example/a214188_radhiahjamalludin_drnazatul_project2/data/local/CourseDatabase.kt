package com.example.a214188_radhiahjamalludin_drnazatul_project2.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [CourseEntity::class, QuizEntity::class, FavouriteTipEntity::class],version = 2, exportSchema = false)
abstract class CourseDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao

    companion object {
        @Volatile
        private var Instance: CourseDatabase? = null

        fun getDatabase(context: Context): CourseDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, CourseDatabase::class.java, "course_database")
                    .fallbackToDestructiveMigration() //destroy db
                    .build() //bina db baru
                    .also { Instance = it } //simpan p/ubah utk kegunaan seterusnya
            }
        }
    }
}
