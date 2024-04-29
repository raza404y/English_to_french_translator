package com.onlab.englishtofrench

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.onlab.englishtofrench.databinding.ActivityTexttoSpeechBinding

class TexttoSpeech : AppCompatActivity() {
    private lateinit var binding: ActivityTexttoSpeechBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTexttoSpeechBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.backbtn.setOnClickListener {
            onBackPressed()
        }

    }
}