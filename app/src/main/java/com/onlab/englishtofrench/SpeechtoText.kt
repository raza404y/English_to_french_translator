package com.onlab.englishtofrench

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.onlab.englishtofrench.databinding.ActivitySpeechtoTextBinding
import java.util.Locale

class SpeechtoText : AppCompatActivity() {
    private lateinit var binding: ActivitySpeechtoTextBinding
    private val REQUEST_RECORD_AUDIO_PERMISSION = 200
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var textView: TextView
    private lateinit var translator: Translator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpeechtoTextBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val micButton = findViewById<ConstraintLayout>(R.id.micButton)
        val translateBtn = findViewById<Button>(R.id.translateButton)
        textView = findViewById(R.id.textView11)

        binding.backbtn.setOnClickListener {
            onBackPressed()
        }

        // Check for microphone permission
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), REQUEST_RECORD_AUDIO_PERMISSION)
        }

        translateBtn.setOnClickListener { translateText() }

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.FRENCH)
            .build()
        translator = Translation.getClient(options)

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
                    val recognizedText = matches[0]
                    textView.text = recognizedText
                }
            }
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        micButton.setOnClickListener { startListening() }


        binding.clearbtn.setOnClickListener {
            binding.textView11.text = ""
        }

        binding.copybtn.setOnClickListener {
            val text = binding.textView11.text.toString()

            try {
                val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("text", text)
                clipboardManager.setPrimaryClip(clipData)

                Toast.makeText(this, "french text copied", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(this, "Failed to copy text to clipboard", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }


        binding.sharebtn.setOnClickListener {
            val text = binding.textView11.text.toString()

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
            }
            val packageManager = this.packageManager
            if (shareIntent.resolveActivity(packageManager) != null) {
                startActivity(Intent.createChooser(shareIntent, "Share Text"))
            } else {
                Toast.makeText(this, "No app available to handle sharing", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun startListening() {
        // Start listening for speech
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.US.language)
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

    private fun translateText() {
        val inputText = textView.text.toString()

        // Check if the model is downloaded
        if (translator == null) {
            Log.e("Translation", "Translator is null, model not downloaded")
            return
        }

        translator.downloadModelIfNeeded()
            .addOnSuccessListener {
                // Model is already downloaded or downloaded successfully
                Log.d("Translation", "Model downloaded successfully")
                performTranslation(inputText)
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
                textView.text = translatedText
            }
            .addOnFailureListener { e ->
                // Handle translation failure
            }
    }

}