package com.example.a214188_radhiahjamalludin_drnazatul_project2.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    navController: NavController,
    viewModel: LearningViewModel
) {
    val cloudData = viewModel.leaderboard.value
    val isLoading = viewModel.isLeaderboardLoading.value
    val errorMessage = viewModel.leaderboardError.value

    LaunchedEffect(Unit) {
        viewModel.fetchLeaderboard()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Global Rankings", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.fetchLeaderboard() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White)
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
                .padding(16.dp)
        ) {
            Text(
                "Top Learners (Cloud Sync)",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                            Spacer(Modifier.height(16.dp))
                            Text("Fetching global rankings...", color = Color.Gray)
                        }
                    }
                }
                errorMessage != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.CloudOff,
                                contentDescription = null,
                                tint = Color.Red,
                                modifier = Modifier.size(48.dp)
                            )
                            Text("Error: $errorMessage", color = Color.Red, modifier = Modifier.padding(16.dp))
                            Button(onClick = { viewModel.fetchLeaderboard() }) {
                                Text("Try Again")
                            }
                        }
                    }
                }
                cloudData.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("No rankings yet. Be the first!", color = Color.Gray)
                            Spacer(Modifier.height(16.dp))
                            Button(onClick = { viewModel.fetchLeaderboard() }) {
                                Text("Refresh")
                            }
                        }
                    }
                }
                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        itemsIndexed(cloudData) { index, entry ->
                            val rankColor = when (index) {
                                0 -> Color(0xFFFFD700) // Gold
                                1 -> Color(0xFFC0C0C0) // Silver
                                2 -> Color(0xFFCD7F32) // Bronze
                                else -> Color.White
                            }

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (index < 3) rankColor.copy(alpha = 0.15f)
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Ranking Number / Icon
                                    Box(contentAlignment = Alignment.Center, modifier = Modifier.width(40.dp)) {
                                        if (index < 3) {
                                            Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = rankColor)
                                        } else {
                                            Text("#${index + 1}", fontWeight = FontWeight.Bold, color = Color.Black)
                                        }
                                    }

                                    Spacer(Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(entry.studentName, style = MaterialTheme.typography.titleMedium, color = Color.White, fontWeight = FontWeight.Bold)
                                        Text("${entry.courseName} • ${entry.dateTime}", style = MaterialTheme.typography.bodySmall, color = Color.White)
                                    }

                                    // Score
                                    Text(
                                        text = "${entry.score}",
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = rankColor
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
