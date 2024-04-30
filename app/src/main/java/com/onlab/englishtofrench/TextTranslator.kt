package com.onlab.englishtofrench

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.onlab.englishtofrench.databinding.ActivityTextTranslatorBinding
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent

class TextTranslator : AppCompatActivity() {
    private lateinit var binding: ActivityTextTranslatorBinding
    private lateinit var editText: EditText
    private lateinit var translatedTextView: TextView
    private var translator: Translator? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextTranslatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        editText = findViewById(R.id.textedt)
        val translateButton: Button = findViewById(R.id.translatebtn)
        translatedTextView = findViewById(R.id.translatedTextView)

        binding.clearbtn.setOnClickListener {
            binding.textedt.setText("")
            binding.translatedTextView.text = ""
        }

        binding.copybtn.setOnClickListener {
            val text = binding.translatedTextView.text.toString()

            try {
                val clipboardManager = it.context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("text", text)
                clipboardManager.setPrimaryClip(clipData)

                Toast.makeText(it.context, "french text copied", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(it.context, "Failed to copy text to clipboard", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }


        binding.sharebtn.setOnClickListener {
            val text = binding.translatedTextView.text.toString()

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





        translateButton.setOnClickListener{
            translateText()
        }

        binding.backbtn.setOnClickListener {
            onBackPressed()
        }
        initializeTranslator()
    }

    private fun initializeTranslator() {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.FRENCH)
            .build()
        translator = com.google.mlkit.nl.translate.Translation.getClient(options)

        // Check and download the model if needed
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
    private fun translateText() {
        val inputText = editText.text.toString()

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
                translatedTextView.text = translatedText
            }
            ?.addOnFailureListener { e ->
            }
    }
}