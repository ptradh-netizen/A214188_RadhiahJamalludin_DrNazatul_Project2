package com.example.a214188_radhiahjamalludin_drnazatul_lab05

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a214188_radhiahjamalludin_drnazatul_lab05.ui.*
import com.example.a214188_radhiahjamalludin_drnazatul_lab05.ui.theme.A214188_RadhiahJamalludin_DrNazatul_Project1Theme

//Bahagian 1: Class MainActivity
class MainActivity : ComponentActivity() {  //entry point utk Android guna Jetpack Compose
    override fun onCreate(savedInstanceState: Bundle?) { //fungsi (fun) pertama kali cipta aktiviti
        super.onCreate(savedInstanceState) //panggil fungsi dari kelas induk
        enableEdgeToEdge() //membolehkan aplikasi paparkan seluruh skrin
        setContent { //UI bermula, gantikan fail XML
            A214188_RadhiahJamalludin_DrNazatul_Project1Theme(dynamicColor = false) { //ikut theme yg ditetapkan
                SmartLearningApp() //paparkan compose fungsi SmartLearningApp()
            }
        }
    }
}

//Bahagian 2: SmartLearningApp (Navigasi)
@Composable
fun SmartLearningApp() {
    // MENGGUNAKAN Factory supaya data Room Database boleh diakses dengan betul
    val viewModel: LearningViewModel = viewModel(factory = LearningViewModel.Factory)
    val navController = rememberNavController() //kawal pergerakan ant skrin

    NavHost(  //bekas(container) utk semua skrin
        navController = navController,
        startDestination = "search" //skrin permulaan
    ) {
        //Bahagian 3: Laluan Skrin (Routes)
        
        // 1. Search Screen (Home)
        composable(route = "search") {
            SearchScreen(navController = navController, viewModel = viewModel)
        }

        // 2. Quiz Screen
        composable(route = "quiz/{courseId}") { backStackEntry ->
            val courseId = backStackEntry.arguments?.getString("courseId")?.toIntOrNull() ?: 0
            val course = com.example.a214188_radhiahjamalludin_drnazatul_lab05.data.DataSource.courses.find { it.id == courseId }
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
// Projek ini menggunakan Factory untuk memastikan ViewModel mendapat repository yang betul.
// Tanpa Factory, aplikasi mungkin akan blank atau crash kerana gagal mengakses database.
