package com.example.a214188_radhiahjamalludin_drnazatul_project1.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.a214188_radhiahjamalludin_drnazatul_project1.R
import com.example.a214188_radhiahjamalludin_drnazatul_project1.data.DataSource
import com.example.a214188_radhiahjamalludin_drnazatul_project1.ui.theme.WhiteText
import com.example.a214188_radhiahjamalludin_drnazatul_project1.ui.theme.WhiteTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: LearningViewModel
) {
    // State to show/hide the course picker dialog
    var showCoursePicker by remember { mutableStateOf(false) }

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
            // Profile Header
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.radh),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = viewModel.studentName,
                    style = MaterialTheme.typography.headlineSmall,
                    color = WhiteText,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Matric: ${viewModel.matricNumber}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = WhiteTextSecondary
                )
                Text(
                    text = viewModel.program,
                    style = MaterialTheme.typography.bodySmall,
                    color = WhiteTextSecondary
                )
            }

            // Learning Statistics Section
            Text(
                "Learning Statistics",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleMedium,
                color = WhiteText,
                fontWeight = FontWeight.Bold
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // MODIFIED: Green "Courses" Stat Card that triggers the picker
                StatCard(
                    title = "Courses (Tap to Choose)",
                    value = "${viewModel.learningPlan.size}",
                    icon = Icons.Default.Book,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { showCoursePicker = true },
                    containerColor = Color(0xFF4CAF50) // Green color for high visibility
                )
                // MODIFIED: Quizzes Stat Card that navigates to Quiz History
                StatCard(
                    title = "Quizzes (Tap for History)",
                    value = "${viewModel.quizHistory.size}",
                    icon = Icons.Default.Quiz,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { navController.navigate("quiz_history") },
                    containerColor = Color(0xFF2196F3) // Blue color for distinction
                )
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                StatCard(
                    title = "Avg Score",
                    value = String.format("%.1f", viewModel.getAverageScore()),
                    icon = Icons.Default.BarChart,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    title = "Highest",
                    value = "${viewModel.getHighestScore()}",
                    icon = Icons.Default.EmojiEvents,
                    modifier = Modifier.weight(1f)
                )
            }

            // Added Course List Section (Shows what you have selected)
            Text(
                "My Selected Courses",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleMedium,
                color = WhiteText,
                fontWeight = FontWeight.Bold
            )
            
            if (viewModel.learningPlan.isEmpty()) {
                Text(
                    "No courses selected. Tap the green 'Courses' box above to pick.",
                    color = WhiteTextSecondary,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 8.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            } else {
                viewModel.learningPlan.forEach { course ->
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
                                    painter = painterResource(course.icon),
                                    contentDescription = null,
                                    modifier = Modifier.padding(8.dp)
                                )
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

            Spacer(modifier = Modifier.height(10.dp))

            // Navigation Button
            Button(
                onClick = { navController.navigate("search") {
                    popUpTo("search") { inclusive = true }
                } },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Back to Home", color = Color.White)
            }
        }
    }

    // --- DIALOG FOR CHOOSING COURSES (Bullet/Checklist Style) ---
    if (showCoursePicker) {
        Dialog(onDismissRequest = { showCoursePicker = false }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        "Choose Your Courses",
                        style = MaterialTheme.typography.titleLarge,
                        color = WhiteText,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    DataSource.courses.forEach { course ->
                        val isSelected = viewModel.learningPlan.any { it.id == course.id }
                        
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { viewModel.toggleCourse(course) }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Bullet / Selection Icon
                            Icon(
                                imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (isSelected) Color(0xFF4CAF50) else Color.Gray
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(course.name, color = WhiteText, style = MaterialTheme.typography.bodyLarge)
                                Text(course.level, color = WhiteTextSecondary, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        HorizontalDivider(color = WhiteTextSecondary.copy(alpha = 0.1f))
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { showCoursePicker = false },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text("Done Choosing", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                icon, 
                contentDescription = null, 
                tint = if (containerColor == Color(0xFF4CAF50) || containerColor == Color(0xFF2196F3)) Color.White else Color(0xFFFFEB3B), 
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = if (containerColor == Color(0xFF4CAF50) || containerColor == Color(0xFF2196F3)) Color.White else WhiteText)
            Text(
                text = title, 
                style = MaterialTheme.typography.labelSmall, 
                color = if (containerColor == Color(0xFF4CAF50) || containerColor == Color(0xFF2196F3)) Color.White else WhiteTextSecondary,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}
