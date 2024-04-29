package com.onlab.englishtofrench

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

    }
}