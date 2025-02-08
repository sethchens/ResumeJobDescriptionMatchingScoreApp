package com.example.todolistapp.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// This is a heads up for the viewModel saying that there is a thing called TodoItem
data class TodoItem(val id: Int, val title: String, val description: String, var isChecked: Boolean)

class MainViewModel : ViewModel() {
    private val _todoList = MutableStateFlow<List<TodoItem>>(emptyList()) // List inside StateFlow
    val todoList: StateFlow<List<TodoItem>> = _todoList // this creates a reference (.value creates a copy)

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog

    private var nextId = 0

    fun openDialog() {
        _showDialog.value = true
    }

    fun closeDialog() {
        _showDialog.value = false
    }

    fun addTodo(title: String, description: String) {
        _todoList.value += TodoItem(nextId++, title, description, false) // Assign unique ID
    }

    fun updateTodo(id: Int, isChecked: Boolean) {
        _todoList.value = _todoList.value.map { todo ->
            if (todo.id == id) todo.copy(isChecked = isChecked) else todo // Check by ID
        }
    }

    fun deleteTodo(id: Int) {
        _todoList.value = _todoList.value.filter { it.id != id }
    }
}