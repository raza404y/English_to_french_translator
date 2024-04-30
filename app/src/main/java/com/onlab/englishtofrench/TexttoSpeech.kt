package com.onlab.englishtofrench

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.TextView
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.onlab.englishtofrench.databinding.ActivityTexttoSpeechBinding
import java.util.Locale

class TexttoSpeech : AppCompatActivity() {
    private lateinit var binding: ActivityTexttoSpeechBinding
    private val REQUEST_RECORD_AUDIO_PERMISSION = 200
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var textView144: TextView
    private lateinit var translator: Translator
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var recognizedText: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTexttoSpeechBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.backbtn.setOnClickListener {
            onBackPressed()
        }

        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.FRENCH)
            .build()
        translator = Translation.getClient(options)



        binding.playbtn.setOnClickListener {
            translateText()
            val translatedText = binding.translatedtextview.text.toString()
            playTranslatedText(translatedText)
        }

        binding.materialButton2.setOnClickListener {
            translateText()
            val translatedText = binding.translatedtextview.text.toString()
            playTranslatedText(translatedText)
        }

        textToSpeech = TextToSpeech(this) { status ->
            if (status != TextToSpeech.ERROR) {
                textToSpeech.language = Locale.US
            }
        }
    }
    private fun playTranslatedText(translatedText: String) {
       textToSpeech.speak(translatedText, TextToSpeech.QUEUE_FLUSH, null)
    }

    private fun translateText() {
        val inputText = binding.textedt.text.toString()

        // Check if the model is downloaded
        if (translator == null) {
            Log.e("Translation", "Translator is null, model not downloaded")
            return
        }

        translator?.downloadModelIfNeeded()
            ?.addOnSuccessListener {
                // Model is already downloaded or downloaded successfully
                Log.d("Translation", "Model downloaded successfully")
                performTranslation(inputText)
            }
            ?.addOnFailureListener { e ->
                // Model download failed
                Log.e("Translation", "Failed to download model: ${e.message}")
            }
    }

    private fun performTranslation(inputText: String) {
        translator?.translate(inputText)
            ?.addOnSuccessListener { translatedText ->
                Log.d("Translation", "Translation successful: $translatedText")
                binding.translatedtextview.text = translatedText
            }
            ?.addOnFailureListener { e ->
            }
    }
}