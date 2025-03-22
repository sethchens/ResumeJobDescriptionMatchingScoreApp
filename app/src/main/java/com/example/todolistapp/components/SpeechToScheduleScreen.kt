package com.example.todolistapp.components


import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.Locale
import java.time.LocalDate
import java.time.LocalDateTime


@Composable
fun SpeechToScheduleScreen(navController: NavController) {
    val context = LocalContext.current
    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }
    val recognizerIntent = remember {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM) // or another model
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.toString())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now...")
            putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true) // Use on-device recognition
        }
    }

    var speechText by remember { mutableStateOf("Tap the mic and start speaking...") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = speechText, fontSize = 20.sp, textAlign = TextAlign.Center)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            speechRecognizer.setRecognitionListener(object : RecognitionListener {
                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (!matches.isNullOrEmpty()) {
                        speechText = matches[0] // Capture recognized text
                        Log.d("TTS", speechText)
                    }
                }

                override fun onError(error: Int) {
                    speechText = "Error recognizing speech: $error"

                    // Log error details
                    Log.e("SpeechRecognizer", "Error code: $error")

                    when (error) {
                        SpeechRecognizer.ERROR_AUDIO -> Log.e("SpeechRecognizer", "Audio recording error")
                        SpeechRecognizer.ERROR_CLIENT -> Log.e("SpeechRecognizer", "Client-side error")
                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> Log.e("SpeechRecognizer", "Insufficient permissions")
                        SpeechRecognizer.ERROR_NETWORK -> Log.e("SpeechRecognizer", "Network error")
                        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> Log.e("SpeechRecognizer", "Network timeout")
                        SpeechRecognizer.ERROR_NO_MATCH -> Log.e("SpeechRecognizer", "No recognition match")
                        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> Log.e("SpeechRecognizer", "Recognizer busy")
                        SpeechRecognizer.ERROR_SERVER -> Log.e("SpeechRecognizer", "Server error")
                        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> Log.e("SpeechRecognizer", "No speech input")
                        else -> Log.e("SpeechRecognizer", "Unknown error")
                    }
                }


                override fun onReadyForSpeech(params: Bundle?) {}
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}
                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
            speechRecognizer.startListening(recognizerIntent)
        }) {
            Text("Start Listening")
        }
    }

    data class ScheduleDetail(val title: String, val description: String, val dateTime: LocalDateTime)

//    fun toSchedule(speech: String) {
//        // send it to the model(maybe firestore)
//    }

//    fun getScheduleDetails(): ScheduleDetail {
//        // Do the call and return scheduleDetails
//    }

    @Composable
    fun createSchedule(details: List<ScheduleDetail>) {
        for (detail in details) {
            Card {
                Text(detail.title)
                Text(detail.description)
                Text((detail.dateTime).toString())
            }
        }
    }

}