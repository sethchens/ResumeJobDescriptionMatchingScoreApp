package com.example.todolistapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun TodoItem(
    id: Int,
    title: String,
    description: String,
    isChecked: Boolean,
    onDelete: (Int) -> Unit,
    onCheckedChange: (Boolean) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f) // Allow the Column to take up available space
            ) {
                Text(text = title)
                Spacer(modifier = Modifier.height(4.dp)) // Add a little space between the title and description
                Text(text = description)
            }
            Checkbox(checked = isChecked, onCheckedChange = onCheckedChange)
            IconButton(onClick = { onDelete(id) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete Todo")
            }
        }
    }
}