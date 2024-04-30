package com.onlab.englishtofrench

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.onlab.englishtofrench.databinding.ActivityVoiceTranslatorBinding
import java.util.*

class VoiceTranslator : AppCompatActivity() {
    private lateinit var binding: ActivityVoiceTranslatorBinding
    private val REQUEST_RECORD_AUDIO_PERMISSION = 200
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var textView144: TextView
    private lateinit var translator: Translator
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var recognizedText: String

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityVoiceTranslatorBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        textView144 = findViewById(R.id.textview144)

        binding.backbtn.setOnClickListener {
            onBackPressed()
        }
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_RECORD_AUDIO_PERMISSION)
        }

        // Set up SpeechRecognizer
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {}
            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (matches != null && matches.isNotEmpty()) {
                    recognizedText = matches[0]
                    textView144.text = recognizedText
                    translateText()
                }
            }
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        binding.micebtn.setOnClickListener { startListening() }

        textToSpeech = TextToSpeech(applicationContext) { status ->
            if (status != TextToSpeech.ERROR) {
                textToSpeech.language = Locale.FRENCH
            }
        }

        binding.playbtn.setOnClickListener {
            val translatedText = textView144.text.toString()
            playTranslatedText(translatedText)
        }
        binding.materialButton.setOnClickListener {
            val translatedText = textView144.text.toString()
            playTranslatedText(translatedText)
        }

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.FRENCH)
            .build()
        translator = Translation.getClient(options)


    }
    private fun startListening() {
        // Start listening for speech
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US") // Change language as needed
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
        speechRecognizer.startListening(intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
    }

    private fun playTranslatedText(translatedText: String) {
        textToSpeech.speak(translatedText, TextToSpeech.QUEUE_FLUSH, null)
    }

    private fun translateText() {
        // Check if the model is downloaded
        if (translator == null) {
            Log.e("Translation", "Translator is null, model not downloaded")
            return
        }

        translator.downloadModelIfNeeded()
            .addOnSuccessListener {
                // Model is already downloaded or downloaded successfully
                Log.d("Translation", "Model downloaded successfully")
                performTranslation(recognizedText)
            }
            .addOnFailureListener { e ->
                // Model download failed
                Log.e("Translation", "Failed to download model: ${e.message}")
            }
    }

    private fun performTranslation(inputText: String) {
        translator.translate(inputText)
            .addOnSuccessListener { translatedText ->
                Log.d("Translation", "Translation successful: $translatedText")
                textView144.text = translatedText
            }
            .addOnFailureListener { e ->
                // Handle translation failure
            }
    }
}