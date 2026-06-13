package com.example.a214188_radhiahjamalludin_drnazatul_project2

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a214188_radhiahjamalludin_drnazatul_project2.ui.*
import com.example.a214188_radhiahjamalludin_drnazatul_project2.ui.theme.A214188_RadhiahJamalludin_DrNazatul_Project2Theme

class MainActivity : ComponentActivity(), SensorEventListener {
    
    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null
    private var viewModel: LearningViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Setup Sensors
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        
        enableEdgeToEdge()
        setContent {
            val mainViewModel: LearningViewModel = viewModel(factory = LearningViewModel.Factory)
            viewModel = mainViewModel
            
            A214188_RadhiahJamalludin_DrNazatul_Project2Theme(dynamicColor = false) {
                SmartLearningApp(mainViewModel)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lightSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
            viewModel?.updateLightLevel(event.values[0])
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

@Composable
fun SmartLearningApp(viewModel: LearningViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "search"
    ) {
        composable(route = "search") {
            SearchScreen(navController = navController, viewModel = viewModel)
        }

        composable(route = "quiz/{courseId}") { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId")?.toIntOrNull() ?: 0
            val course = com.example.a214188_radhiahjamalludin_drnazatul_project2.data.DataSource.courses.find { it.id == courseId }
            course?.let {
                QuizScreen(
                    navController = navController,
                    course = it,
                    viewModel = viewModel
                )
            }
        }

        composable(route = "score") {
            ScoreScreen(navController = navController, viewModel = viewModel)
        }

        composable(route = "profile") {
            ProfileScreen(navController = navController, viewModel = viewModel)
        }

        composable(route = "quiz_history") {
            QuizHistoryScreen(navController = navController, viewModel = viewModel)
        }

        // Screen Baru: Insights (Problem Statement, Lessons Learned, API, Sensors)
        composable(route = "insights") {
            InsightsScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = "leaderboard") {
            LeaderboardScreen(navController = navController, viewModel = viewModel)
        }

    }
}
