package com.example.a214188_radhiahjamalludin_drnazatul_lab03

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a214188_radhiahjamalludin_drnazatul_lab03.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A214188_RadhiahJamalludin_DrNazatul_Lab03Theme(dynamicColor = false) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->
                    EduScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

data class CourseInfo(val name: String, val level: String, val icon: Int)

@Composable
fun EduScreen(modifier: Modifier = Modifier) {
    var searchText by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("Ready to search") }
    var highlightedCourse by remember { mutableStateOf<String?>(null) }

    val courses = listOf(
        CourseInfo("Java Basic", "Beginning", R.drawable.java),
        CourseInfo("Mobile Development", "Intermediate", R.drawable.android),
        CourseInfo("Cybersecurity Fundamentals", "Beginning", R.drawable.cyber2),
        CourseInfo("AI & Machine Learning", "Advanced", R.drawable.ai),
        CourseInfo("Cisco/Networking", "Intermediate", R.drawable.network)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
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
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Smart Learning",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
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
                    // MENGGUNAKAN BOX + BASIC TEXT FIELD UNTUK ALIGNMENT YANG SEMPURNA
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (searchText.isEmpty()) {
                            Text("Search course...", fontSize = 12.sp, color = Color.Gray)
                        }
                        BasicTextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            textStyle = TextStyle(
                                fontSize = 14.sp,
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
                            containerColor = Color(0xFFADD8E6), 
                            contentColor = Color.Black
                        )
                    ) {
                        Text("Search", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Text(
                    text = resultText, 
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White // Found: java Putih Tajam
                )
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
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // --- SECTION TITLE ---
        Text(
            text = "Your Courses",
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(start = 4.dp),
            color = Color.White // Your Courses Putih Tajam
        )

        // --- COURSE LIST ---
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            courses.forEach { course ->
                var expanded by remember { mutableStateOf(false) }

                val isHighlighted = highlightedCourse != null && course.name.contains(highlightedCourse!!, ignoreCase = true)
                
                val cardBgColor = if (isHighlighted) {
                    SearchHighlightColor
                } else {
                    MaterialTheme.colorScheme.surface
                }

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
                                Text(
                                    text = course.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White // Nama Kursus Putih Tajam
                                )
                                Text(
                                    text = "Level : ${course.level}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            }
                        }

                        if (expanded) {
                            Spacer(Modifier.height(6.dp))
                            HorizontalDivider(
                                thickness = 0.5.dp, 
                                color = Color.White.copy(alpha = 0.3f)
                            )
                            Spacer(Modifier.height(6.dp))
                            Text(
                                text = "Learn core concepts of ${course.name}.",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = Color.White // Deskripsi Putih Tajam
                            )
                        }
                    }
                }
            }
        }

        // --- FOOTER ---
        Text(
            text = "Copyright @Smart Learning Corp",
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White, // Footer Putih Tajam
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    A214188_RadhiahJamalludin_DrNazatul_Lab03Theme(dynamicColor = false) {
        EduScreen()
    }
}
