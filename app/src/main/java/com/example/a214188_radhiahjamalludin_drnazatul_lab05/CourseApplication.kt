package com.example.a214188_radhiahjamalludin_drnazatul_lab05

import android.app.Application
import com.example.a214188_radhiahjamalludin_drnazatul_lab05.data.CourseRepository
import com.example.a214188_radhiahjamalludin_drnazatul_lab05.data.local.CourseDatabase

class CourseApplication : Application() {
    val database: CourseDatabase by lazy { CourseDatabase.getDatabase(this) }
    val repository: CourseRepository by lazy { CourseRepository(database.courseDao()) }
}
