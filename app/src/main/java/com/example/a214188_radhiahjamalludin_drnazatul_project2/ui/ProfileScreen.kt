package com.example.a214188_radhiahjamalludin_drnazatul_project2.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.a214188_radhiahjamalludin_drnazatul_project2.R
import com.example.a214188_radhiahjamalludin_drnazatul_project2.data.DataSource
import com.example.a214188_radhiahjamalludin_drnazatul_project2.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: LearningViewModel
) {
    var showCoursePicker by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }

    val learningPlan by viewModel.learningPlan.collectAsState()
    val quizResults by viewModel.quizHistory.collectAsState()

    // Data untuk Langkah 7 (Sensor)
    val lightLevel = viewModel.lightLevel.value

    val avgScore = viewModel.getAverageScore(quizResults)
    val highestScore = viewModel.getHighestScore(quizResults)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Student Profile", color = WhiteText) },
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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- Profile Header ---
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.radh),
                    contentDescription = "Profile Picture",
                    modifier = Modifier.size(100.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = viewModel.studentName, style = MaterialTheme.typography.headlineSmall, color = WhiteText, fontWeight = FontWeight.Bold)
                Text(text = "Matric: ${viewModel.matricNumber}", color = WhiteTextSecondary)
                Text(text = viewModel.program, style = MaterialTheme.typography.bodySmall, color = WhiteTextSecondary)
            }

            // --- Statistics ---
            Text(
                "Learning Statistics",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleMedium,
                color = WhiteText,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // DIUBAH: Menggunakan R.drawable.courses
                StatCard(
                    title = "Courses (Tap)",
                    value = "${learningPlan.size}",
                    icon = painterResource(R.drawable.courses),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showCoursePicker = true },
                    containerColor = StatCardOrange
                )

                // DIUBAH: Menggunakan R.drawable.quizzes
                StatCard(
                    title = "Quizzes (History)",
                    value = "${quizResults.size}",
                    icon = painterResource(R.drawable.quizzes),
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            navController.navigate("quiz_history")
                        },
                    containerColor = SuccessGreen
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // KEKAL: Menggunakan R.drawable.avg
                StatCard(
                    title = "Avg Score",
                    value = String.format("%.1f", avgScore),
                    icon = painterResource(R.drawable.avg),
                    modifier = Modifier.weight(1f),
                    containerColor = InfoBlue
                )

                // KEKAL: Menggunakan R.drawable.high
                StatCard(
                    title = "Highest",
                    value = "$highestScore",
                    icon = painterResource(R.drawable.high),
                    modifier = Modifier.weight(1f),
                    containerColor = WarningOrange
                )
            }

            // --- Global Rankings Button ---
            Button(
                onClick = { navController.navigate("leaderboard") },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = UnifiedButtonColor, contentColor = UnifiedButtonTextColor)
            ) {
                Icon(Icons.Default.Groups, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Rankings",
                    color = BlackText,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            // --- App Info Button ---
            Button(
                onClick = { navController.navigate("insights") },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = UnifiedButtonColor)
            ) {
                Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "App Info",
                    color = BlackText,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall
                )
            }

            // --- Selected Courses List ---
            Text("My Selected Courses", modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleMedium, color = WhiteText, fontWeight = FontWeight.Bold)

            if (learningPlan.isEmpty()) {
                Text("No courses selected. Tap the orange 'Courses' box to pick.", color = WhiteTextSecondary, modifier = Modifier.padding(vertical = 8.dp))
            } else {
                learningPlan.forEach { course ->
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Surface(modifier = Modifier.size(40.dp), shape = RoundedCornerShape(8.dp), color = MaterialTheme.colorScheme.primaryContainer) {
                                Image(painter = painterResource(course.icon), contentDescription = null, modifier = Modifier.padding(8.dp))
                            }
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(course.name, style = MaterialTheme.typography.titleSmall, color = WhiteText, fontWeight = FontWeight.Bold)
                                Text("Lecturer: ${course.lecturer}", style = MaterialTheme.typography.bodySmall, color = WhiteTextSecondary)
                            }
                        }
                    }
                }
            }

            // --- LANGKAH 7: Study Environment (Sensor Cahaya) ---
            Spacer(Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (lightLevel < 50) Color(0xFFB2ECD5) else Color(0xFFF0F5F0)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (lightLevel < 50) Icons.Default.Highlight else Icons.Default.LightMode,
                            contentDescription = null,
                            tint = if (lightLevel < 50) Color.Red else Color(0xFF2E7D32)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "Study Environment",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Current Light: ${String.format("%.1f", lightLevel)} Lux",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = if (lightLevel < 50)
                            "Too dark! Please turn on the lights to protect your eyes."
                        else "The lighting is suitable for studying.",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // --- Reset Button ---
            OutlinedButton(
                onClick = { showResetDialog = true },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed),
                border = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed)
            ) {
                Icon(Icons.Default.DeleteForever, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.reset_button), fontWeight = FontWeight.Bold)
            }

            // --- Back Button ---
            Button(
                onClick = { navController.navigate("search") { popUpTo("search") { inclusive = true } } },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = UnifiedButtonColor, contentColor = UnifiedButtonTextColor)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = "Home Icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Home",
                    fontSize = 12.sp
                )
            }
        }
    }

    // --- Dialogs (Reset & Course Picker) ---
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text(stringResource(R.string.reset_dialog_title)) },
            text = { Text(stringResource(R.string.reset_dialog_message)) },
            confirmButton = {
                TextButton(onClick = { viewModel.resetPlan(); showResetDialog = false }) {
                    Text(stringResource(R.string.reset_confirm), color = ErrorRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text(stringResource(R.string.reset_cancel))
                }
            }
        )
    }

    if (showCoursePicker) {
        Dialog(onDismissRequest = { showCoursePicker = false }) {
            Card(modifier = Modifier.fillMaxWidth().padding(16.dp), shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Choose Your Courses", style = MaterialTheme.typography.titleLarge, color = WhiteText, fontWeight = FontWeight.Bold)
                    DataSource.courses.forEach { course ->
                        val isSelected = learningPlan.any { it.id == course.id }
                        Row(
                            modifier = Modifier.fillMaxWidth().clickable {
                                viewModel.toggleCourse(course)
                            }.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = isSelected, onCheckedChange = null)
                            Spacer(Modifier.width(8.dp))
                            Text(course.name, color = WhiteText)
                        }
                    }
                }
            }
        }
    }
}

// DIUBAH: Jika anda dapati warna asal PNG tidak keluar/menjadi hitam sepenuhnya,
// anda boleh menukar `Icon` di bawah kepada `Image` dan buang parameter `tint`.
@Composable
fun StatCard(
    title: String,
    value: String,
    icon: Painter,
    modifier: Modifier = Modifier,
    containerColor: Color
) {
    Card(
        modifier = modifier.height(72.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // DIUBAH: Saiz ditukar dari 20.dp kepada 32.dp supaya imej kelihatan lebih besar
            Image(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            // Jarak dikecilkan sedikit atau dibuang jika imej sudah cukup besar mengisi ruang
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
        }
    }
}