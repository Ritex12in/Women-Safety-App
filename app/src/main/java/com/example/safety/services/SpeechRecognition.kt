package com.example.safety.services

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.internal.ContextUtils.getActivity

class SpeechRecognition(context: Activity, onMatched: onMatched) {
    private var speechRecognizer: SpeechRecognizer
    private val helpList = listOf("helpme", "help", "helpmeplease", "ineedhelp","ineedhelpplease",
        "i need help now", "i need help right now","help i am hurt","i am in danger","get help",
        "call the police","save me")

    init {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(android.Manifest.permission.RECORD_AUDIO),
                1
            )
        }

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {}
            override fun onResults(results: Bundle?) {}

            override fun onPartialResults(partialResults: Bundle?) {
                val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    val partialText = matches[0].trim().lowercase()
                    val matchFound = helpList.any { partialText.contains(it.trim().lowercase())}
                    if (matchFound){
                        stopListening()
                        onMatched.onMatched()
                    }
                }
            }
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })
    }

    public fun startListening() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(
            RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS,
            100000 // Adjust as needed
        )
        intent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true) // Enable partial results
        speechRecognizer.startListening(intent)
    }

    public fun stopListening() {
        speechRecognizer.stopListening()
        speechRecognizer.destroy()
    }

    interface onMatched{
        fun onMatched()
    }
}