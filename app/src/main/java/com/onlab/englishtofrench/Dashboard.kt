package com.onlab.englishtofrench

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslatorOptions
import com.onlab.englishtofrench.databinding.ActivityDashboardBinding

class Dashboard : AppCompatActivity() {
    private lateinit var binding : ActivityDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.voiceTranslatorBtn.setOnClickListener {
            val intent = Intent(this,VoiceTranslator::class.java)
            startActivity(intent)
        }

        binding.texttranslatorbtn.setOnClickListener {
            val intent = Intent(this,TextTranslator::class.java)
            startActivity(intent)
        }

        binding.speechtotext.setOnClickListener {
            val intent = Intent(this,SpeechtoText::class.java)
            startActivity(intent)
        }

        binding.texttospeech.setOnClickListener {
            val intent = Intent(this,TexttoSpeech::class.java)
            startActivity(intent)
        }

        initializeTranslator()

    }
    private fun initializeTranslator() {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.FRENCH)
            .build()
        val translator = com.google.mlkit.nl.translate.Translation.getClient(options)

        // Check and download the model if needed
       // Toast.makeText(this, "Please wait a minute,language manager is downloading...", Toast.LENGTH_SHORT).show()

        translator?.downloadModelIfNeeded()
            ?.addOnSuccessListener {
                // Model is already downloaded or downloaded successfully
                Log.d("Translation", "Model downloaded successfully")
            }
            ?.addOnFailureListener { e ->
                // Model download failed
                Log.e("Translation", "Failed to download model: ${e.message}")
            }
    }
}