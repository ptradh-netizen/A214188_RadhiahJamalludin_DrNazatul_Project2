package com.example.a214188_radhiahjamalludin_drnazatul_project1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a214188_radhiahjamalludin_drnazatul_project1.ui.*
import com.example.a214188_radhiahjamalludin_drnazatul_project1.ui.theme.A214188_RadhiahJamalludin_DrNazatul_Project1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A214188_RadhiahJamalludin_DrNazatul_Project1Theme(dynamicColor = false) {
                SmartLearningApp()
            }
        }
    }
}

@Composable
fun SmartLearningApp(
    viewModel: LearningViewModel = viewModel()
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "search"
    ) {
        // 1. Search Screen (Home)
        composable(route = "search") {
            SearchScreen(navController = navController, viewModel = viewModel)
        }

        // 2. Quiz Screen
        composable(route = "quiz/{courseId}") { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId")?.toIntOrNull() ?: 0
            val course = com.example.a214188_radhiahjamalludin_drnazatul_project1.data.DataSource.courses.find { it.id == courseId }
            course?.let {
                QuizScreen(
                    navController = navController,
                    course = it,
                    viewModel = viewModel
                )
            }
        }

        // 3. Score Screen
        composable(route = "score") {
            ScoreScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        // 4. Profile Screen
        composable(route = "profile") {
            ProfileScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        // 5. Quiz History Screen
        composable(route = "quiz_history") {
            QuizHistoryScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}
