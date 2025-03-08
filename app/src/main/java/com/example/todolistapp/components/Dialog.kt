package com.example.todolistapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todolistapp.viewmodel.MainViewModel


@Composable
fun Dialog(
    isOpen: Boolean,
    onSubmit: (String, String) -> Unit,
    viewModel: MainViewModel
) {
    // State for the task name input
    val resumeInput = remember { mutableStateOf("") }
    val jobInput = remember { mutableStateOf("") }

    if (isOpen) {
        // Show the dialog
        AlertDialog(
            onDismissRequest = { viewModel.closeDialog() },
            title = { Text("Enter resume and job description") },
            text = {
                Column {
                    // Task Name input field
                    TextField(
                        value = resumeInput.value,
                        onValueChange = { resumeInput.value = it },
                        label = { Text("Resume Text") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .heightIn(max = 120.dp)
                    )

                    // Task Description input field
                    TextField(
                        value = jobInput.value,
                        onValueChange = { jobInput.value = it },
                        label = { Text("Job Description") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .heightIn(max = 120.dp)
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onSubmit(resumeInput.value, jobInput.value)
                        viewModel.closeDialog()
                    }
                ) {
                    Text("Submit")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { viewModel.closeDialog() }
                ) {
                    Text("Cancel")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
