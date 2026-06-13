package com.example.a214188_radhiahjamalludin_drnazatul_project2

import android.app.Application
import com.example.a214188_radhiahjamalludin_drnazatul_project2.data.CourseRepository
import com.example.a214188_radhiahjamalludin_drnazatul_project2.data.local.CourseDatabase
import com.google.firebase.FirebaseApp

class CourseApplication : Application() {
    val database: CourseDatabase by lazy { CourseDatabase.getDatabase(this) }
    val repository: CourseRepository by lazy { CourseRepository(database.courseDao()) }

    override fun onCreate() {
        super.onCreate()
        // Memastikan Firebase diinisialisasi sebaik sahaja aplikasi bermula
        FirebaseApp.initializeApp(this)
    }
}
