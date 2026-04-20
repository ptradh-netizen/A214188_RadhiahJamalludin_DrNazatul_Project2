package com.example.a214188_radhiahjamalludin_drnazatul_lab04.ui

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
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.a214188_radhiahjamalludin_drnazatul_lab04.R
import com.example.a214188_radhiahjamalludin_drnazatul_lab04.data.DataSource
import com.example.a214188_radhiahjamalludin_drnazatul_lab04.ui.theme.SearchHighlightColor
import com.example.a214188_radhiahjamalludin_drnazatul_lab04.ui.theme.UnifiedButtonColor
import com.example.a214188_radhiahjamalludin_drnazatul_lab04.ui.theme.UnifiedButtonTextColor

@Composable
fun SearchScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("Ready to search") }
    var highlightedCourse by remember { mutableStateOf<String?>(null) }
    val courses = DataSource.courses

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("plan") },
                containerColor = UnifiedButtonColor,
                contentColor = UnifiedButtonTextColor,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Assignment,
                    contentDescription = "My Learning Plan",
                    tint = UnifiedButtonTextColor
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            // --- GREETING CARD ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Welcome,",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White
                        )
                        Text(
                            text = "Smart Learning",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = R.drawable.radh),
                            contentDescription = "Profile",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = "Radhiah",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // --- SEARCH FORM ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(1.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        "Search Course", 
                        style = MaterialTheme.typography.labelSmall, 
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .background(Color.White, RoundedCornerShape(12.dp))
                                .padding(horizontal = 12.dp),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (searchText.isEmpty()) {
                                Text("Search course...", 
                                    style = MaterialTheme.typography.bodySmall, 
                                    color = Color.Gray)
                            }
                            BasicTextField(
                                value = searchText,
                                onValueChange = { searchText = it },
                                textStyle = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                ),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        
                        Spacer(Modifier.width(8.dp))
                        
                        Button(
                            onClick = {
                                searchQuery = searchText
                                if (searchText.isNotBlank()) {
                                    val foundMatch = courses.any { it.name.contains(searchText, ignoreCase = true) }
                                    if (foundMatch) {
                                        resultText = "Found: $searchText"
                                        highlightedCourse = searchText
                                    } else {
                                        resultText = "No results found"
                                        highlightedCourse = null
                                    }
                                } else {
                                    resultText = "Enter a name"
                                    highlightedCourse = null
                                }
                            },
                            modifier = Modifier.height(46.dp),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = UnifiedButtonColor, 
                                contentColor = UnifiedButtonTextColor
                            )
                        ) {
                            Text(
                                text = "Search", 
                                style = MaterialTheme.typography.labelMedium.copy(color = UnifiedButtonTextColor) 
                            )
                        }
                    }
                    Text(text = resultText, style = MaterialTheme.typography.labelMedium, color = Color.White)
                }
            }

            // --- PROMOTION CARD ---
            Card(
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Let's learn something new!",
                        color = Color.White,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }

            // --- SECTION TITLE ---
            Text(
                text = "Your Courses",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 4.dp),
                color = Color.White
            )

            // --- COURSE LIST ---
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    val filteredCourses = if (searchQuery.isEmpty()) courses else courses.filter { it.name.contains(searchQuery, ignoreCase = true) }
                    
                    filteredCourses.forEach { course ->
                        var expanded by remember { mutableStateOf(false) }
                        val isHighlighted = highlightedCourse != null && course.name.contains(highlightedCourse!!, ignoreCase = true)
                        val cardBgColor = if (isHighlighted) SearchHighlightColor else MaterialTheme.colorScheme.surface

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateContentSize()
                                .clickable { expanded = !expanded },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = cardBgColor),
                            elevation = CardDefaults.cardElevation(1.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        modifier = Modifier.size(36.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFFFFF1F1)
                                    ) {
                                        Image(
                                            painter = painterResource(course.icon),
                                            contentDescription = null,
                                            modifier = Modifier.padding(5.dp)
                                        )
                                    }
                                    Spacer(Modifier.width(12.dp))
                                    Column {
                                        Text(text = course.name, style = MaterialTheme.typography.titleSmall, color = Color.White)
                                        Text(text = "Level : ${course.level}", style = MaterialTheme.typography.bodySmall, color = Color.White)
                                    }
                                }

                                if (expanded) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    HorizontalDivider(thickness = 0.5.dp, color = Color.White.copy(alpha = 0.3f))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Lecturer: ${course.lecturer}", style = MaterialTheme.typography.bodySmall, color = Color.White)
                                    Text("Credit: ${course.credit} | Semester: ${course.semester}", style = MaterialTheme.typography.bodySmall, color = Color.White)
                                    
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = { navController.navigate("details/${course.id}") },
                                        modifier = Modifier.align(Alignment.End).height(36.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = UnifiedButtonColor, 
                                            contentColor = UnifiedButtonTextColor
                                        )
                                    ) {
                                        Text(
                                            text = "View More", 
                                            style = MaterialTheme.typography.labelMedium.copy(color = UnifiedButtonTextColor)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    // --- FOOTER DI DALAM SCROLL UNTUK KEMAS ---
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Copyright @Smart Learning Corp",
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
