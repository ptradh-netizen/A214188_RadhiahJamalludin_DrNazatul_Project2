package com.example.a214188_radhiahjamalludin_drnazatul_lab04.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.a214188_radhiahjamalludin_drnazatul_lab04.model.Course
import com.example.a214188_radhiahjamalludin_drnazatul_lab04.ui.theme.DetailFrameColor
import com.example.a214188_radhiahjamalludin_drnazatul_lab04.ui.theme.UnifiedButtonColor
import com.example.a214188_radhiahjamalludin_drnazatul_lab04.ui.theme.UnifiedButtonTextColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navController: NavController,
    course: Course,
    viewModel: LearningViewModel,
    modifier: Modifier = Modifier
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Course Details", style = MaterialTheme.typography.titleLarge, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Image(
                    painter = painterResource(course.icon),
                    contentDescription = null,
                    modifier = Modifier.padding(20.dp)
                )
            }

            // --- KAD MAKLUMAT ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = DetailFrameColor
                ),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Course: ${course.name}", 
                        style = MaterialTheme.typography.titleLarge, 
                        color = Color.White
                    )
                    HorizontalDivider(thickness = 1.dp, color = Color.White.copy(alpha = 0.2f))
                    Text(
                        text = "Lecturer: ${course.lecturer}", 
                        style = MaterialTheme.typography.bodyLarge, 
                        color = Color.White
                    )
                    Text(
                        text = "Credit: ${course.credit}", 
                        style = MaterialTheme.typography.bodyLarge, 
                        color = Color.White
                    )
                    Text(
                        text = "Semester: ${course.semester}", 
                        style = MaterialTheme.typography.bodyLarge, 
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // --- BUTANG ADD TO LEARNING PLAN ---
            Button(
                onClick = { 
                    viewModel.addToPlan(course)
                    navController.navigate("plan")
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = UnifiedButtonColor,
                    contentColor = UnifiedButtonTextColor
                )
            ) {
                Text(
                    text = "Add to Learning Plan", 
                    style = MaterialTheme.typography.labelLarge, 
                    color = UnifiedButtonTextColor
                )
            }

            // --- BUTANG BACK ---
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = UnifiedButtonColor,
                    contentColor = UnifiedButtonTextColor
                )
            ) {
                Text(
                    text = "Back", 
                    style = MaterialTheme.typography.labelLarge, 
                    color = UnifiedButtonTextColor
                )
            }
        }
    }
}
