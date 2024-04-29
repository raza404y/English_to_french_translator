package com.onlab.englishtofrench

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.onlab.englishtofrench.databinding.ActivityVoiceTranslatorBinding

class VoiceTranslator : AppCompatActivity() {
    private lateinit var binding: ActivityVoiceTranslatorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityVoiceTranslatorBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.backbtn.setOnClickListener {
            onBackPressed()
        }

    }
}