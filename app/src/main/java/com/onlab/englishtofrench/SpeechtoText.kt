package com.onlab.englishtofrench

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.onlab.englishtofrench.databinding.ActivitySpeechtoTextBinding

class SpeechtoText : AppCompatActivity() {
    private lateinit var binding: ActivitySpeechtoTextBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpeechtoTextBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backbtn.setOnClickListener {
            onBackPressed()
        }

    }
}