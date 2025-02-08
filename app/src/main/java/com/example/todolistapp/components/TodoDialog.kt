package com.example.todolistapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.todolistapp.viewmodel.MainViewModel

@Composable
fun TodoDialog(
    isOpen: Boolean,
    onSubmit: (String, String) -> Unit,
    viewModel: MainViewModel
) {
    // State for the task name input
    val taskName = remember { mutableStateOf("") }
    val taskDescription = remember { mutableStateOf("") }

    if (isOpen) {
        // Show the dialog
        AlertDialog(
            onDismissRequest = { viewModel.closeDialog() },
            title = { Text("Enter Task Name") },
            text = {
                Column {
                    // Task Name input field
                    TextField(
                        value = taskName.value,
                        onValueChange = { taskName.value = it },
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )

                    // Task Description input field
                    TextField(
                        value = taskDescription.value,
                        onValueChange = { taskDescription.value = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onSubmit(taskName.value, taskDescription.value)
                        viewModel.closeDialog()
                    }
                ) {
                    Text("Add")
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
