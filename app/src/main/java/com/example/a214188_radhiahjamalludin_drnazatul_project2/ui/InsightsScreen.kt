package com.example.a214188_radhiahjamalludin_drnazatul_project2.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.a214188_radhiahjamalludin_drnazatul_project2.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(
    navController: NavController,
    viewModel: LearningViewModel
) {
    val lightLevel = viewModel.lightLevel.value
    val learningTip = viewModel.learningTip.value
    // collectAsState mesti berada di sini (top level function)
    val favTips by viewModel.favouriteTips.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchLearningTip()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("App Insights", color = WhiteText) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = WhiteText)
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
            // --- API / Cloud Section ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = InfoBlue.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Cloud, contentDescription = null, tint = InfoBlue)
                        Spacer(Modifier.width(8.dp))
                        Text("Learning Tips (Cloud API)", style = MaterialTheme.typography.titleMedium, color = WhiteText, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = if (learningTip.isEmpty()) "Loading tips..." else learningTip,
                        style = MaterialTheme.typography.bodyMedium,
                        color = WhiteText
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = { viewModel.fetchLearningTip() },
                            modifier = Modifier.padding(top = 8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = InfoBlue)
                        ) {
                            Text("New Tips")
                        }

                        Button(
                            onClick = { viewModel.saveCurrentTipToFavourites() },
                            modifier = Modifier.padding(top = 8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) {
                            Icon(Icons.Default.Favorite, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Save")
                        }
                    }
                }
            }

            // --- Favourite Tips List Section ---
            if (favTips.isNotEmpty()) {
                Text("My Favorite Tips:", style = MaterialTheme.typography.titleMedium, color = WhiteText)
                favTips.forEach { favEntity ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = WhiteText.copy(alpha = 0.1f))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(favEntity.tip, modifier = Modifier.weight(1f), color = WhiteText)
                            IconButton(onClick = { viewModel.deleteTipFromFavourites(favEntity) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                            }
                        }
                    }
                }
            }

            // --- Sensor Section ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = WarningOrange.copy(alpha = 0.2f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Lightbulb, contentDescription = null, tint = WarningOrange)
                        Spacer(Modifier.width(8.dp))
                        Text("Light Sensor", style = MaterialTheme.typography.titleMedium, color = WhiteText, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Current Light Level: ${String.format("%.2f", lightLevel)} lux",
                        style = MaterialTheme.typography.bodyLarge,
                        color = WhiteText,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (lightLevel < 50) "DARK environment. Please turn on the lights to study!" else "BRIGHT environment.",
                        style = MaterialTheme.typography.bodySmall,
                        color = WhiteTextSecondary
                    )
                }
            }
        }
    }
}
