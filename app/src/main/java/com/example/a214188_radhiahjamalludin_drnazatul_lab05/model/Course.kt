package com.example.a214188_radhiahjamalludin_drnazatul_lab05.model

import androidx.annotation.DrawableRes

data class Course(
    val id: Int,
    val name: String,
    val level: String,
    @DrawableRes val icon: Int,
    val lecturer: String,
    val credit: Int,
    val semester: Int
)
