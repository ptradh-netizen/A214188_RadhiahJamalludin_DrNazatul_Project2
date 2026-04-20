package com.example.a214188_radhiahjamalludin_drnazatul_lab04

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.a214188_radhiahjamalludin_drnazatul_lab04.data.DataSource
import com.example.a214188_radhiahjamalludin_drnazatul_lab04.ui.*
import com.example.a214188_radhiahjamalludin_drnazatul_lab04.ui.theme.A214188_RadhiahJamalludin_DrNazatul_Lab04Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A214188_RadhiahJamalludin_DrNazatul_Lab04Theme(dynamicColor = false) {
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
        composable(route = "search") {
            SearchScreen(navController = navController)
        }
        composable(
            route = "details/{courseId}",
            arguments = listOf(navArgument("courseId") { type = NavType.IntType })
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getInt("courseId") ?: 0
            val course = DataSource.courses.find { it.id == courseId }
            course?.let {
                DetailsScreen(
                    navController = navController,
                    course = it,
                    viewModel = viewModel
                )
            }
        }
        composable(route = "plan") {
            LearningPlanScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}
