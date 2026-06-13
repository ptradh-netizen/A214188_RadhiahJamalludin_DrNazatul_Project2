package com.example.a214188_radhiahjamalludin_drnazatul_project2.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.a214188_radhiahjamalludin_drnazatul_project2.R
import com.example.a214188_radhiahjamalludin_drnazatul_project2.data.DataSource
import com.example.a214188_radhiahjamalludin_drnazatul_project2.ui.theme.UnifiedButtonColor
import com.example.a214188_radhiahjamalludin_drnazatul_project2.ui.theme.UnifiedButtonTextColor
import com.example.a214188_radhiahjamalludin_drnazatul_project2.ui.theme.WhiteText
import com.example.a214188_radhiahjamalludin_drnazatul_project2.ui.theme.WhiteTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoreScreen(
    navController: NavController,
    viewModel: LearningViewModel
) {
    val history by viewModel.quizHistory.collectAsState()
    val latestScore = history.lastOrNull()?.score ?: 0
    val totalQuizzes = history.size
    val averageScore = viewModel.getAverageScore(history)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiz Results", color = WhiteText) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- Summary Card ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Latest Score", style = MaterialTheme.typography.labelLarge)
                    Text(
                        text = "$latestScore/30",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Total Quizzes", style = MaterialTheme.typography.labelSmall)
                            Text("$totalQuizzes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Average Score", style = MaterialTheme.typography.labelSmall)
                            Text(String.format("%.1f", averageScore), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Text(
                "Quiz History (Room DB)",
                style = MaterialTheme.typography.titleMedium,
                color = WhiteText,
                fontWeight = FontWeight.Bold
            )

            // --- History List (Step 4: Room Integration) ---
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(history.reversed()) { result ->
                    val courseIcon = DataSource.courses.find { it.id == result.courseId || it.name == result.courseName }?.icon ?: R.drawable.java
                    
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
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
                                    painter = painterResource(courseIcon),
                                    contentDescription = null,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(result.courseName, style = MaterialTheme.typography.titleSmall, color = WhiteText)
                                Text(result.dateTime, style = MaterialTheme.typography.bodySmall, color = WhiteTextSecondary)
                            }
                            Text(
                                text = "${result.score}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = WhiteText
                            )
                        }
                    }
                }
            }

            // --- Bottom Navigation Section (Step 1: 7th Screen Connection) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Buttan ke Skrin ke-7 (Leaderboard)
                Button(
                    onClick = { navController.navigate("leaderboard") },
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = UnifiedButtonColor, contentColor = UnifiedButtonTextColor)
                ) {
                    Icon(Icons.Default.Leaderboard, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Rankings",
                        fontSize = 12.sp   // Changed from 12.dp to 12.sp
                    )
                }

                Button(
                    onClick = { navController.navigate("profile") },
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = UnifiedButtonColor,
                        contentColor = UnifiedButtonTextColor
                    )
                ) {
                    Icon(Icons.Default.Person, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Profile",
                        fontSize = 12.sp   // Changed from 12.dp to 12.sp
                    )
                }
            }
        }
    }
}
