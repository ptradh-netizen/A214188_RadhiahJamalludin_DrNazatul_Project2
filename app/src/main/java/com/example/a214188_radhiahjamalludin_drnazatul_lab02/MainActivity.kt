package com.example.a214188_radhiahjamalludin_drnazatul_lab02

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a214188_radhiahjamalludin_drnazatul_lab02.ui.theme.A214188_RadhiahJamalludin_DrNazatul_Lab02Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A214188_RadhiahJamalludin_DrNazatul_Lab02Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    EduScreen(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// Data class to keep code simple and organized
data class CourseInfo(val name: String, val level: String, val icon: Int)

@Composable
fun EduScreen(modifier: Modifier = Modifier) {
    var searchText by remember { mutableStateOf("") }
    var resultText by remember { mutableStateOf("Result text") }

    // List of courses
    val courses = listOf(
        CourseInfo("Java", "Beginning", R.drawable.java),
        CourseInfo("Mobile Development", "Intermediate", R.drawable.android),
        CourseInfo("CyberSecurity Fundamentals", "Beginning", R.drawable.cyber2),
        CourseInfo("AI & Machine Learning", "Advanced", R.drawable.ai),
        CourseInfo("Cisco/Networking", "Intermediate", R.drawable.network)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F8))
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // --- HEADER ---
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Welcome,", color = Color.Black, style = MaterialTheme.typography.bodySmall)
                Text("Smart Learning", color = Color.Black, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.radh),
                    contentDescription = null,
                    modifier = Modifier.size(45.dp).clip(CircleShape).background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )
                Text("Profile", color = Color.Black, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
            }
        }

        // --- SEARCH SECTION ---
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Search Course", color = Color.Black, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier.weight(1f).clip(RoundedCornerShape(8.dp)),
                    placeholder = { Text("Search course...") }
                )
                Spacer(Modifier.width(8.dp))
                Button(
                    onClick = { 
                        if (searchText.isNotBlank()) {
                            // Check if searchText exists in the course names
                            val exists = courses.any { it.name.contains(searchText, ignoreCase = true) }
                            resultText = if (exists) "Found: $searchText" else "No results found"
                        } else {
                            resultText = "Enter a name"
                        }
                    },
                    shape = RoundedCornerShape(8.dp)
                ) { Text("Search") }
            }
            Text(resultText, color = Color.Black, style = MaterialTheme.typography.labelMedium)
        }

        // --- PROMOTION CARD ---
        Box(
            modifier = Modifier.fillMaxWidth().height(80.dp).background(Color(0xFF0D47A1), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("Let's learn something new!", color = Color.White, fontWeight = FontWeight.Medium)
        }

        // --- COURSES LIST ---
        Text("Your Courses", color = Color.Black, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        
        courses.forEach { course ->
            Row(
                modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(10.dp)).padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(course.icon),
                    contentDescription = null,
                    modifier = Modifier.size(35.dp).clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(course.name, color = Color.Black, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    Text("Level: ${course.level}", color = Color.Black, style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        // --- FOOTER ---
        Text(
            text = "Copyright @Smart Learning Corp",
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            color = Color.Black,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    A214188_RadhiahJamalludin_DrNazatul_Lab02Theme { EduScreen() }
}
