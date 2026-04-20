package com.example.a214188_radhiahjamalludin_drnazatul_lab04.data

import com.example.a214188_radhiahjamalludin_drnazatul_lab04.R
import com.example.a214188_radhiahjamalludin_drnazatul_lab04.model.Course

object DataSource {
    val courses = listOf(
        Course(1, "Java Basic", "Beginning", R.drawable.java, "Dr Nazatul Aini", 3, 2),
        Course(2, "Mobile Development", "Intermediate", R.drawable.android, "Dr Nazatul Aini", 3, 4),
        Course(3, "Cybersecurity Fundamentals", "Beginning", R.drawable.cyber2, "Dr Nazatul Aini", 3, 3),
        Course(4, "AI & Machine Learning", "Advanced", R.drawable.ai, "Dr Nazatul Aini", 4, 5),
        Course(5, "Cisco/Networking", "Intermediate", R.drawable.network, "Dr Nazatul Aini", 3, 4)
    )
}
