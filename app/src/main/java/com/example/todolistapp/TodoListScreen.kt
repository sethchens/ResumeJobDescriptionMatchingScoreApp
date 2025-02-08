package com.example.todolistapp

// Most of the import are for the ui and reference of other files
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolistapp.viewmodel.MainViewModel
import com.example.todolistapp.components.TodoItem
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todolistapp.components.TodoDialog
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun TodoListScreen(todoViewModel: MainViewModel) {

    // Collect the todo list from the ViewModel
    // Declared variables here will survive the recomposition
    val todoList by todoViewModel.todoList.collectAsStateWithLifecycle()
    val showDialog by todoViewModel.showDialog.collectAsStateWithLifecycle()
    var searchText by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .systemBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "To Do List",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier.fillMaxWidth().height(40.dp),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
                singleLine = true,
                shape = MaterialTheme.shapes.medium,
                textStyle = TextStyle(fontSize = 12.sp)
            )

            // Display all todo items from ViewModel
            todoList.forEach { todo ->
                TodoItem(
                    id = todo.id,
                    description = todo.description,
                    title = todo.title,
                    isChecked = todo.isChecked,
                    onCheckedChange = { checked ->
                        todoViewModel.updateTodo(todo.id, checked)
                    },
                    onDelete = { id ->
                        todoViewModel.deleteTodo(id)
                    }
                )
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(30.dp),
            onClick = { todoViewModel.openDialog() } // This calls the function from viewModel to open the dialog
        ) {
            Icon(Icons.Default.Add, contentDescription = "Create New Todo")
        }

        // Render the TodoDialog only when showDialog is true
        if (showDialog) {
            TodoDialog(
                viewModel = todoViewModel,
                isOpen = showDialog, // Dialog visibility comes from ViewModel
                onSubmit = { taskName, taskDescription ->
                    todoViewModel.addTodo(taskName, taskDescription) // Add new task to the list
                    todoViewModel.closeDialog() // Close the dialog after submitting
                }
            )
        }
    }
}