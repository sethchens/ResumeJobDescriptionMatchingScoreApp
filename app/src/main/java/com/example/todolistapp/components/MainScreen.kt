package com.example.todolistapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.navigation.NavHostController
import com.example.todolistapp.viewmodel.MainViewModel

@Composable
fun MainScreen(mainViewModel: MainViewModel, navController: NavHostController) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Resume-Job-Ranking", "Speech to Schedule")

    Column {
        Spacer(
            modifier = Modifier
            .height(55.dp))
        TabRow(selectedTabIndex = selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> ResumeJobScreen(mainViewModel)
            1 -> SpeechToScheduleScreen(navController)
        }
    }
}