package com.example.todolistapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolistapp.data.RepositoryFlaskAPI
import com.example.todolistapp.data.RepositoryMainViewModelToFireStore
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// This is a heads up for the viewModel saying that there is a thing called TodoItem
data class TodoItem(val id: Int, val title: String, val description: String, var isChecked: Boolean)

class MainViewModel(
    private val repoFirestore: RepositoryMainViewModelToFireStore,
    private val repoFlaskAPI: RepositoryFlaskAPI
) : ViewModel() {

    private var nextId = 0
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog
    val resumeJob: LiveData<List<DocumentSnapshot>> = repoFirestore.resumeJob

    // The construction handles the listening to firebase
    init {
        repoFirestore.fetchResumeJob()
    }

    fun openDialog() {
        _showDialog.value = true
    }

    fun closeDialog() {
        _showDialog.value = false
    }

    fun submitResumeJobScore(resumeText: String, jobText: String) {
        viewModelScope.launch {
            try {
                val score = getSimilarityScore(resumeText, jobText)
                repoFirestore.createResumeJobScore(resumeText, jobText, score)
            } catch (e: Exception) {
                Log.w("Flask", e)
            }
        }
    }

    fun updateResumeJob() {
        repoFirestore.fetchResumeJob()
    }

    private suspend fun getSimilarityScore(resume_text: String, job_text: String): Float {
        return repoFlaskAPI.getSimilarityScore(resume_text, job_text)
    }
}