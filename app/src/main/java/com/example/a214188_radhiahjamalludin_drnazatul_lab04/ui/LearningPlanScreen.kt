package com.example.a214188_radhiahjamalludin_drnazatul_lab04.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.a214188_radhiahjamalludin_drnazatul_lab04.ui.theme.UnifiedButtonColor
import com.example.a214188_radhiahjamalludin_drnazatul_lab04.ui.theme.UnifiedButtonTextColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningPlanScreen(
    navController: NavController,
    viewModel: LearningViewModel,
    modifier: Modifier = Modifier
) {
    val plan = viewModel.learningPlan

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("My Learning Plan", style = MaterialTheme.typography.titleLarge, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (plan.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Your plan is empty.\nGo back and add some courses!", 
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center, 
                        color = Color.Gray)
                }
            } else {
                Text("Selected Courses:", style = MaterialTheme.typography.titleMedium, color = Color.White)
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        plan.forEachIndexed { index, course ->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("${index + 1}. ${course.name}", style = MaterialTheme.typography.bodyLarge, color = Color.White)
                                Text("${course.credit} Cr", style = MaterialTheme.typography.bodyMedium, color = Color.LightGray)
                            }
                        }
                        
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.Gray)
                        
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Credit:", style = MaterialTheme.typography.titleSmall, color = Color.White)
                            Text("${viewModel.getTotalCredits()}", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // --- BUTANG HOME ---
                Button(
                    onClick = { 
                        navController.navigate("search") {
                            popUpTo("search") { inclusive = true }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = UnifiedButtonColor, 
                        contentColor = UnifiedButtonTextColor
                    )
                ) {
                    Icon(Icons.Default.Home, contentDescription = null, tint = UnifiedButtonTextColor)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Home", 
                        style = MaterialTheme.typography.labelLarge, 
                        color = UnifiedButtonTextColor
                    )
                }

                // --- BUTANG RESET PLAN ---
                Button(
                    onClick = { viewModel.resetPlan() },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = UnifiedButtonColor, 
                        contentColor = UnifiedButtonTextColor
                    )
                ) {
                    Text(
                        text = "Reset Plan", 
                        style = MaterialTheme.typography.labelLarge, 
                        color = UnifiedButtonTextColor
                    )
                }
            }
        }
    }
}
