package com.example.a214188_radhiahjamalludin_drnazatul_project2.data

import com.example.a214188_radhiahjamalludin_drnazatul_project2.R
import com.example.a214188_radhiahjamalludin_drnazatul_project2.model.Course

object DataSource {
    val courses = listOf(
        Course(1, "Java Basic", "Beginning", R.drawable.java, "Ts Rohaizah", 3, 2),
        Course(2, "Mobile Development", "Intermediate", R.drawable.android, "Dr Nazatul Aini", 3, 4),
        Course(3, "Cybersecurity Fundamentals", "Beginning", R.drawable.cyber2, "Ts Dr Kamal", 3, 3),
        Course(4, "AI & Machine Learning", "Advanced", R.drawable.ai, "Dr Rahimi", 4, 5),
        Course(5, "Cisco/Networking", "Intermediate", R.drawable.network, "Dr Fauzan", 3, 4)
    )
}
