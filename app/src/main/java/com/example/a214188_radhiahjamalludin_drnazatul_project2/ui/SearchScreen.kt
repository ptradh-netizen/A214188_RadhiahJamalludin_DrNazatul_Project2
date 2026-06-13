package com.example.a214188_radhiahjamalludin_drnazatul_project2.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.a214188_radhiahjamalludin_drnazatul_project2.R
import com.example.a214188_radhiahjamalludin_drnazatul_project2.ui.theme.*

@Composable
fun SearchScreen(
    navController: NavController,
    viewModel: LearningViewModel,
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    var highlightedCourse by remember { mutableStateOf<String?>(null) }
    var searchStatus by remember { mutableStateOf("Ready to search") }

    val selectedCourses by viewModel.learningPlan.collectAsState()
    val quizResults by viewModel.quizHistory.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = Color(0xFF155AC2) // Unified Blue Background
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // --- Welcome Profile Header ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WelcomeCardGray),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Hello,",
                            fontSize = 12.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = viewModel.studentName,
                            fontSize = 18.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Black
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Smart Learning",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.8f),
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.CloudDone,
                                contentDescription = "Cloud Synced",
                                tint = SuccessGreen,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    Surface(
                        modifier = Modifier
                            .size(55.dp)
                            .border(2.dp, Color.White, CircleShape)
                            .clip(CircleShape)
                            .clickable { navController.navigate("profile") },
                        color = SurfaceOverlay
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.radh),
                            contentDescription = "Profile",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            if (selectedCourses.isEmpty()) {
                // --- SDG & Motivation Section ---
                SDGProblemCard()

                // --- Call to Action Section ---
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.2f)),
                    border = androidx.compose.foundation.BorderStroke(2.dp, Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LibraryBooks,
                            contentDescription = null,
                            tint = UnifiedButtonColor,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Ready to start your journey?",
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Pick your courses in your profile to begin learning.",
                            color = Color.White,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 16.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = { navController.navigate("profile") },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                        ) {
                            Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Setup My Profile", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            } else {
                // --- Search & Filter Bar ---
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .background(Color(0xFFF1F1F1), RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (searchText.isEmpty()) Text("Search courses...", color = PlaceholderColor, style = MaterialTheme.typography.bodyMedium)
                            BasicTextField(
                                value = searchText,
                                onValueChange = { searchText = it },
                                textStyle = MaterialTheme.typography.bodyMedium.copy(color = BlackText),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(Modifier.width(6.dp))
                        IconButton(
                            onClick = {
                                searchQuery = searchText
                                if (searchText.isNotBlank()) {
                                    val match = selectedCourses.find { it.name.contains(searchText, ignoreCase = true) }
                                    if (match != null) {
                                        highlightedCourse = searchText
                                        searchStatus = "Found: ${match.name}"
                                    } else {
                                        highlightedCourse = null
                                        searchStatus = "Result not found"
                                    }
                                } else {
                                    searchStatus = "Ready to search"
                                    highlightedCourse = null
                                }
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(UnifiedButtonColor)
                        ) {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = UnifiedButtonTextColor, modifier = Modifier.size(20.dp))
                        }
                    }
                }

                if (searchStatus != "Ready to search") {
                    Text(
                        text = searchStatus,
                        color = if (searchStatus == "Result not found") ErrorRed else SuccessGreen,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("My Courses", fontSize = 18.sp, color = WhiteText, fontWeight = FontWeight.Black)
                    Text("${selectedCourses.size} Total", style = MaterialTheme.typography.labelSmall, color = WhiteTextSecondary)
                }

                // --- Course List Column ---
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val filtered = if (searchQuery.isEmpty()) selectedCourses else selectedCourses.filter { it.name.contains(searchQuery, ignoreCase = true) }

                    filtered.forEach { course ->
                        var expanded by remember { mutableStateOf(false) }
                        val isHighlighted = highlightedCourse != null && course.name.contains(highlightedCourse!!, ignoreCase = true)

                        val quizInfo = quizResults.find { it.courseId == course.id }
                        val isCompleted = quizInfo != null

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateContentSize()
                                .clickable { expanded = !expanded }
                                .border(
                                    width = if (isHighlighted) 2.dp else 1.dp,
                                    color = if (isHighlighted) UnifiedButtonColor else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                ),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isHighlighted) UnifiedButtonColor.copy(alpha = 0.15f) else Color.White
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = if (isHighlighted) 6.dp else 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        modifier = Modifier.size(40.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        color = UnifiedButtonColor.copy(alpha = 0.1f)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.Book,
                                                contentDescription = null,
                                                tint = if (isCompleted) SuccessGreen else UnifiedButtonColor,
                                                modifier = Modifier.size(22.dp)
                                            )
                                        }
                                    }
                                    Spacer(Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = course.name,
                                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 15.sp),
                                            fontWeight = FontWeight.Black,
                                            color = BlackText
                                        )
                                        Text(
                                            text = course.level,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = PlaceholderColor
                                        )
                                    }
                                    Icon(
                                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                        contentDescription = null,
                                        tint = PlaceholderColor
                                    )
                                }

                                if (expanded) {
                                    Spacer(Modifier.height(12.dp))
                                    HorizontalDivider(color = Color.LightGray.copy(alpha = 0.4f))
                                    Spacer(Modifier.height(8.dp))
                                    Text(
                                        text = "Lecturer: ${course.lecturer}\nCredit: ${course.credit} | Semester: ${course.semester}",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                                        color = BlackText.copy(alpha = 0.8f),
                                        lineHeight = 18.sp
                                    )
                                    Spacer(Modifier.height(12.dp))

                                    if (isCompleted) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(SuccessGreen.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                                                .padding(6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(Icons.Default.Stars, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(18.dp))
                                            Spacer(Modifier.width(6.dp))
                                            Text(
                                                text = "Completed! Score: ${quizInfo?.score}/100",
                                                color = SuccessGreen,
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                        Spacer(Modifier.height(8.dp))
                                    }

                                    Button(
                                        onClick = { navController.navigate("quiz/${course.id}") },
                                        modifier = Modifier.fillMaxWidth().height(44.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (isCompleted) Color.Gray else UnifiedButtonColor
                                        )
                                    ) {
                                        Text(if (isCompleted) "Retake Quiz" else "Take Quiz", color = if (isCompleted) Color.White else UnifiedButtonTextColor, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SDGProblemCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SdgCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Lightbulb, contentDescription = null, tint = Color(0xFFE65100), modifier = Modifier.size(24.dp))
                Spacer(Modifier.width(8.dp))
                Text(
                    text = stringResource(id = R.string.sdg_title),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            SDGItem(icon = Icons.Default.ReportProblem, text = stringResource(id = R.string.sdg_problem), fontSize = 13.sp)
            Spacer(modifier = Modifier.height(6.dp))
            SDGItem(icon = Icons.Default.Warning, text = stringResource(id = R.string.sdg_impact), fontSize = 13.sp)
            Spacer(modifier = Modifier.height(6.dp))
            SDGItem(icon = Icons.Default.CheckCircle, text = stringResource(id = R.string.sdg_solution), fontSize = 13.sp)
        }
    }
}

@Composable
fun SDGItem(icon: ImageVector, text: String, fontSize: TextUnit = 13.sp) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp).padding(top = 1.dp),
            tint = Color(0xFF3E2723)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = fontSize,
            textAlign = TextAlign.Justify,
            fontWeight = FontWeight.Black,
            color = Color.Black,
            lineHeight = 18.sp
        )
    }
}
