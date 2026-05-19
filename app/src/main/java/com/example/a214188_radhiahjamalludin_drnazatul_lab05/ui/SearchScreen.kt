package com.example.a214188_radhiahjamalludin_drnazatul_lab05.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.a214188_radhiahjamalludin_drnazatul_lab05.R
import com.example.a214188_radhiahjamalludin_drnazatul_lab05.ui.theme.*

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
    
    // MENGGUNAKAN collectAsState untuk Room Database
    val selectedCourses by viewModel.learningPlan.collectAsState()
    val quizResults by viewModel.quizHistory.collectAsState()

    Scaffold(
        containerColor = Color(0xFF155AC2)
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = WelcomeCardGray)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "Welcome,", style = MaterialTheme.typography.labelMedium, color = WhiteText)
                        Text(text = "Smart Learning", style = MaterialTheme.typography.headlineSmall, color = WhiteText, fontWeight = FontWeight.Bold)
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { navController.navigate("profile") }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.radh),
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(45.dp)
                                .clip(CircleShape)
                                .background(SurfaceOverlay),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Radhiah",
                            style = MaterialTheme.typography.labelSmall,
                            color = WhiteText,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            if (selectedCourses.isEmpty()) {
                SDGProblemCard()
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                        .background(
                            color= WelcomeCardGray.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(24.dp)
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Please choose course in your profile first",
                            color = WhiteText,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = { navController.navigate("profile") },
                            modifier = Modifier.height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                        ) {
                            Text("Go to Profile", color = WhiteText, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.weight(1f).height(46.dp).background(SearchBoxBackground, RoundedCornerShape(8.dp)).padding(horizontal = 12.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (searchText.isEmpty()) Text("Search your courses...", color = PlaceholderColor)
                            BasicTextField(
                                value = searchText,
                                onValueChange = { searchText = it },
                                textStyle = MaterialTheme.typography.bodyMedium.copy(color = BlackText),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(Modifier.width(8.dp))
                        Button(
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
                            colors = ButtonDefaults.buttonColors(containerColor = UnifiedButtonColor)
                        ) {
                            Text("Search", color = UnifiedButtonTextColor)
                        }
                    }
                }

                Text(
                    text = searchStatus,
                    color = if (searchStatus == "Result not found") ErrorRed else WhiteText,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp)
                )

                Text("Your Learning List", style = MaterialTheme.typography.titleMedium, color = WhiteText)

                Column(
                    modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val filtered = if (searchQuery.isEmpty()) selectedCourses else selectedCourses.filter { it.name.contains(searchQuery, ignoreCase = true) }
                    
                    filtered.forEach { course ->
                        var expanded by remember { mutableStateOf(false) }
                        val isHighlighted = highlightedCourse != null && course.name.contains(highlightedCourse!!, ignoreCase = true)
                        
                        // Semak status kuiz dari quizResults StateFlow
                        val quizInfo = quizResults.find { it.courseId == course.id }
                        val isCompleted = quizInfo != null

                        Card(
                            modifier = Modifier.fillMaxWidth().animateContentSize().clickable { expanded = !expanded },
                            colors = CardDefaults.cardColors(containerColor = if (isHighlighted) SearchHighlightColor else CardBackground)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(modifier = Modifier.size(40.dp), shape = RoundedCornerShape(8.dp), color = SurfaceOverlay) {
                                        Image(painter = painterResource(course.icon), contentDescription = null, modifier = Modifier.padding(8.dp))
                                    }
                                    Spacer(Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(course.name, style = MaterialTheme.typography.titleSmall, color = WhiteText, fontWeight = FontWeight.Bold)
                                        Text("${course.level} • ${course.credit} Credits", style = MaterialTheme.typography.bodySmall, color = WhiteTextSecondary)
                                        
                                        if (isCompleted) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = SuccessGreen, modifier = Modifier.size(14.dp))
                                                Spacer(Modifier.width(4.dp))
                                                Text("Completed", style = MaterialTheme.typography.labelSmall, color = SuccessGreen)
                                            }
                                        }
                                    }
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Expand",
                                        tint = WhiteTextSecondary
                                    )
                                }

                                if (expanded) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    HorizontalDivider(color = DividerColor)
                                    Spacer(modifier = Modifier.height(12.dp))
                                    
                                    if (isCompleted) {
                                        Text(
                                            "Quiz completed! Score: ${quizInfo?.score}/30",
                                            color = WhiteText,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }

                                    Button(
                                        onClick = { navController.navigate("quiz/${course.id}") },
                                        modifier = Modifier.fillMaxWidth().height(45.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF0EA814),
                                            contentColor = Color.White
                                        )
                                    ) {
                                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.White)
                                        Spacer(Modifier.width(8.dp))
                                        Text(if (isCompleted) "Retake Quiz" else "Take Quiz Now", color = Color.White, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Copyright @Smart Learning Corp",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = WhiteTextSecondary,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Composable
fun SDGProblemCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = SdgCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = stringResource(id = R.string.sdg_title),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = BlackText
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(id = R.string.sdg_problem),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify,
                fontWeight = FontWeight.Bold,
                color = BlackText

            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(id = R.string.sdg_impact),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify,
                fontWeight = FontWeight.Bold,
                color = BlackText
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(id = R.string.sdg_solution),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Justify,
                fontWeight = FontWeight.Bold,
                color = BlackText
            )
        }
    }
}
