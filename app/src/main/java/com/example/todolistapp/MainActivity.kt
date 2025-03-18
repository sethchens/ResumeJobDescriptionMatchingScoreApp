package com.example.todolistapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todolistapp.data.RepositoryFlaskAPI
import com.example.todolistapp.components.MainScreen
import com.example.todolistapp.viewmodel.MainViewModel
import com.example.todolistapp.data.RepositoryMainViewModelToFireStore
import com.example.todolistapp.ui.theme.TodoListAPPTheme
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoListAPPTheme {
                // Navigation Controller
                val navController = rememberNavController()

                // Initiate firebase instance
                val db = Firebase.firestore

                // initiate FlaskAPI repository
                val repositoryFlaskAPI = RepositoryFlaskAPI()

                // Other setup to hook up everything
                val repositoryFirestore = RepositoryMainViewModelToFireStore(db)

                // MainViewModel instance; the use of factory is so that repo can be passed in
                val mainViewModel: MainViewModel = ViewModelProvider(
                    this,
                    MainViewModelFactory(repositoryFirestore, repositoryFlaskAPI)
                )[MainViewModel::class.java]

                mainViewModel.resumeJob.observe(this) { documents ->
                    if (documents.isEmpty()) {
                        Log.w("Firestore", "No documents found")
                    } else {
                        for (document in documents) {
                            Log.d("Firestore", "Main Activity: ${document.id} => ${document.data}")
                        }
                    }
                }

                MainScreen(mainViewModel, navController) // Pass mainViewModel and navigation controller to main screen
            }
        }
    }
}

// Haven't understand why this is needed, but basically this is to enable the pass of repository to viewModel
class MainViewModelFactory(
    private val repositoryFirestore: RepositoryMainViewModelToFireStore,
    private val repositoryFlaskAPI: RepositoryFlaskAPI
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repositoryFirestore, repositoryFlaskAPI) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}