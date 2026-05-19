package com.example.a214188_radhiahjamalludin_drnazatul_lab05.ui

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.a214188_radhiahjamalludin_drnazatul_lab05.R
import com.example.a214188_radhiahjamalludin_drnazatul_lab05.data.DataSource
import com.example.a214188_radhiahjamalludin_drnazatul_lab05.ui.theme.*

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
            Text("Learning Statistics", modifier = Modifier.fillMaxWidth(), style = MaterialTheme.typography.titleMedium, color = WhiteText, fontWeight = FontWeight.Bold)

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard(title = "Courses (Tap)", value = "${learningPlan.size}", icon = Icons.Default.Book, modifier = Modifier.weight(1f).clickable { showCoursePicker = true }, containerColor = StatCardOrange)
                StatCard(title = "Quizzes (History)", value = "${quizResults.size}", icon = Icons.Default.Quiz, modifier = Modifier.weight(1f).clickable { navController.navigate("quiz_history") }, containerColor = SuccessGreen)
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard(title = "Avg Score", value = String.format("%.1f", viewModel.getAverageScore(quizResults)), icon = Icons.Default.BarChart, modifier = Modifier.weight(1f), containerColor = StatCardPink)
                StatCard(title = "Highest", value = "${viewModel.getHighestScore(quizResults)}", icon = Icons.Default.EmojiEvents, modifier = Modifier.weight(1f), containerColor = StatCardPurple)
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
                                Text(course.name, style = MaterialTheme.typography.titleSmall, color = WhiteText)
                                Text("Lecturer: ${course.lecturer}", style = MaterialTheme.typography.bodySmall, color = WhiteTextSecondary)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

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
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Back to Home", color = Color.White)
            }
        }
    }

    // --- Reset Confirmation Dialog ---
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

    // --- Course Picker Dialog (Dikekalkan) ---
    if (showCoursePicker) {
        Dialog(onDismissRequest = { showCoursePicker = false }) {
            Card(modifier = Modifier.fillMaxWidth().padding(16.dp), shape = RoundedCornerShape(16.dp)) {
                Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Choose Your Courses", style = MaterialTheme.typography.titleLarge, color = WhiteText, fontWeight = FontWeight.Bold)
                    DataSource.courses.forEach { course ->
                        val isSelected = learningPlan.any { it.id == course.id }
                        Row(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).clickable { viewModel.toggleCourse(course) }.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked, contentDescription = null, tint = if (isSelected) Color(0xFF4CAF50) else Color.Gray)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(course.name, color = WhiteText)
                                Text(course.level, color = WhiteTextSecondary, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                    Button(onClick = { showCoursePicker = false }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)) {
                        Text("Done Choosing", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, icon: ImageVector, modifier: Modifier = Modifier, containerColor: Color = MaterialTheme.colorScheme.surfaceVariant) {
    Card(modifier = modifier, shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = containerColor)) {
        Column(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
            Text(text = title, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.8f), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        }
    }
}
