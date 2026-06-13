package com.example.a214188_radhiahjamalludin_drnazatul_project2.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.a214188_radhiahjamalludin_drnazatul_project2.ui.theme.UnifiedButtonColor
import com.example.a214188_radhiahjamalludin_drnazatul_project2.ui.theme.UnifiedButtonTextColor
import com.example.a214188_radhiahjamalludin_drnazatul_project2.ui.theme.WhiteText
import com.example.a214188_radhiahjamalludin_drnazatul_project2.ui.theme.WhiteTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizHistoryScreen(
    navController: NavController,
    viewModel: LearningViewModel
) {
    // MENGGUNAKAN collectAsState untuk mengambil data dari Room
    val completedQuizzes by viewModel.quizHistory.collectAsState()
    val learningPlan by viewModel.learningPlan.collectAsState()

    // Logik untuk menapis kuiz yang belum dijawab
    val availableQuizzes = learningPlan.filter { course ->
        completedQuizzes.none { it.courseId == course.id }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiz History & Available", color = WhiteText) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = WhiteText)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- SEKSYEN COMPLETED ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFF9800)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Completed Quizzes",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            if (completedQuizzes.isEmpty()) {
                Text("No quizzes completed yet.", color = WhiteTextSecondary, modifier = Modifier.padding(start = 8.dp))
            } else {
                completedQuizzes.forEach { result ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(result.courseName, style = MaterialTheme.typography.titleSmall, color = Color.White)
                                Text("Score: ${result.score}/30", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
                                Text(result.dateTime, color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- SEKSYEN AVAILABLE ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFF9800)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Available Quizzes",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            if (availableQuizzes.isEmpty()) {
                Text("No available quizzes. Choose courses in your profile!", color = WhiteTextSecondary, modifier = Modifier.padding(start = 8.dp))
            } else {
                availableQuizzes.forEach { course ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier.size(40.dp),
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Image(
                                    painter = painterResource(course.icon),
                                    contentDescription = null,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(course.name, style = MaterialTheme.typography.titleSmall, color = WhiteText)
                                Text("${course.level} • ${course.credit} Credits", color = WhiteTextSecondary, style = MaterialTheme.typography.bodySmall)
                            }
                            Button(
                                onClick = { navController.navigate("quiz/${course.id}") },
                                colors = ButtonDefaults.buttonColors(containerColor = UnifiedButtonColor)
                            ) {
                                Text("Start", color = UnifiedButtonTextColor)
                            }
                        }
                    }
                }
            }
        }
    }
}
