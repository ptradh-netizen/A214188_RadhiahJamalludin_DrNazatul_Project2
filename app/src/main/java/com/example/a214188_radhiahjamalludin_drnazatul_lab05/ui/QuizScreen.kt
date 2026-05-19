package com.example.a214188_radhiahjamalludin_drnazatul_lab05.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.a214188_radhiahjamalludin_drnazatul_lab05.model.Course
import com.example.a214188_radhiahjamalludin_drnazatul_lab05.ui.theme.UnifiedButtonColor
import com.example.a214188_radhiahjamalludin_drnazatul_lab05.ui.theme.UnifiedButtonTextColor
import com.example.a214188_radhiahjamalludin_drnazatul_lab05.ui.theme.WhiteText
import com.example.a214188_radhiahjamalludin_drnazatul_lab05.ui.theme.WhiteTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    navController: NavController,
    course: Course,
    viewModel: LearningViewModel
) {
    // Quiz state management
    var selectedAnswer1 by rememberSaveable { mutableStateOf(-1) }
    var selectedAnswer2 by rememberSaveable { mutableStateOf(-1) }
    var selectedAnswer3 by rememberSaveable { mutableStateOf(-1) }

    val questions = listOf(
        Question(
            "What is the main focus of ${course.name}?",
            listOf("Theory only", "Practical application", "History", "Games"),
            1 // Index of correct answer
        ),
        Question(
            "How many credits is this course worth?",
            listOf("1", "2", "3", "4"),
            if (course.credit in 1..4) course.credit - 1 else 2
        ),
        Question(
            "Is ${course.name} a ${course.level} level course?",
            listOf("Yes", "No", "Maybe", "Not sure"),
            0
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quiz: ${course.name}", color = WhiteText) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = WhiteText
                        )
                    }
                },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Question 1
            QuizQuestionItem(
                question = questions[0].text,
                options = questions[0].options,
                selectedIndex = selectedAnswer1,
                onOptionSelected = { selectedAnswer1 = it }
            )

            // Question 2
            QuizQuestionItem(
                question = questions[1].text,
                options = questions[1].options,
                selectedIndex = selectedAnswer2,
                onOptionSelected = { selectedAnswer2 = it }
            )

            // Question 3
            QuizQuestionItem(
                question = questions[2].text,
                options = questions[2].options,
                selectedIndex = selectedAnswer3,
                onOptionSelected = { selectedAnswer3 = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Calculate score
                    var score = 0
                    if (selectedAnswer1 == questions[0].correctIndex) score += 10
                    if (selectedAnswer2 == questions[1].correctIndex) score += 10
                    if (selectedAnswer3 == questions[2].correctIndex) score += 10

                    // Save result - using ID for accuracy
                    viewModel.addQuizResultById(course.id, course.name, score)

                    // Navigate
                    navController.navigate("score")
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = UnifiedButtonColor,
                    contentColor = UnifiedButtonTextColor
                ),
                enabled = selectedAnswer1 != -1 && selectedAnswer2 != -1 && selectedAnswer3 != -1
            ) {
                Text("Submit Quiz", style = MaterialTheme.typography.labelLarge)
            }
        }
    }
}

@Composable
fun QuizQuestionItem(
    question: String,
    options: List<String>,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = question,
                style = MaterialTheme.typography.titleMedium,
                color = WhiteText,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            options.forEachIndexed { index, option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = (index == selectedIndex),
                            onClick = { onOptionSelected(index) },
                            role = Role.RadioButton
                        )
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (index == selectedIndex),
                        onClick = null,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = UnifiedButtonColor,
                            unselectedColor = WhiteTextSecondary
                        )
                    )
                    Text(
                        text = option,
                        style = MaterialTheme.typography.bodyMedium,
                        color = WhiteText,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }
}

data class Question(
    val text: String,
    val options: List<String>,
    val correctIndex: Int
)
