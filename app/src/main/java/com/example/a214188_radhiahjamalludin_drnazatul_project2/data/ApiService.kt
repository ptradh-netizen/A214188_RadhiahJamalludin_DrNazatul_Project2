package com.example.a214188_radhiahjamalludin_drnazatul_project2.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

data class CourseCloudInfo(
    val id: Int,
    val title: String,
    val description: String,
    val level: String
)

data class AdviceSlip(
    val slip: Advice
)

data class Advice(
    val id: Int,
    val advice: String
)

interface ApiService {
    @GET("radhiah-api/main/courses.json") 
    suspend fun getCloudCourses(): List<CourseCloudInfo>

    @GET("https://api.adviceslip.com/advice")
    suspend fun getLearningTip(): AdviceSlip

    companion object {
        private var apiService: ApiService? = null
        fun getInstance(): ApiService {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl("https://raw.githubusercontent.com/radhiah-jamalludin/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiService::class.java)
            }
            return apiService!!
        }
    }
}
