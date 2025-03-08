package com.example.todolistapp.components

// Most of the import are for the ui and reference of other files
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolistapp.viewmodel.MainViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.launch

@Composable
fun MainScreen(mainViewModel: MainViewModel) {

    // Collect the todo list from the ViewModel
    // Declared variables here will survive the recomposition
    val showDialog by mainViewModel.showDialog.collectAsStateWithLifecycle()
    val resumeJob by mainViewModel.resumeJob.observeAsState(emptyList())
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
                text = "Applicants Tracking System",
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

            // Column that will display the items
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Resume-Job Description Match", style = MaterialTheme.typography.headlineSmall)

                // Iterate over the list of documents and display each item
                resumeJob.forEach { document ->
                    Log.d("MainScreen", "${document.data}")
                    JobItem(document) // Call the JobItem composable for each document
                }
            }

        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(30.dp),
            onClick = { mainViewModel.openDialog() } // This calls the function from viewModel to open the dialog
        ) {
            Icon(Icons.Default.Add, contentDescription = "Create New resume-job match")
        }

        // Render the TodoDialog only when showDialog is true
        if (showDialog) {
            Dialog(
                viewModel = mainViewModel,
                isOpen = showDialog, // Dialog visibility comes from ViewModel
                onSubmit = { resumeText, jobDescriptionText ->
                    mainViewModel.viewModelScope.launch {

                        // Run submitResumeJobScore and wait for it to finish
                        mainViewModel.submitResumeJobScore(resumeText, jobDescriptionText)

                        // Then call updateResumeJob
                        mainViewModel.updateResumeJob()

                        // Finally close the dialog
                        mainViewModel.closeDialog()
                    }
                }
            )
        }
    }
}

@Composable
fun JobItem(document: DocumentSnapshot) {
    // Extract data from the document
    val score = document.getDouble("score") ?.toFloat()
    val job = document.getString("job_description_text") ?: "Unknown Field Name"
    val resume = document.getString("resume_text") ?: "Unknown Field Name"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Resume",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.labelMedium
            )

            Text(
                text = resume,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                .heightIn(max = 300.dp)
            )

            Text(
                text = "Job Description",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = job,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .heightIn(max = 300.dp))

            Text(
                text = "Matching Score",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = score.toString(),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .heightIn(max = 300.dp))
        }
    }
}