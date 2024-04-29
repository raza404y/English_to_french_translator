package com.onlab.englishtofrench

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.onlab.englishtofrench.databinding.ActivityTextTranslatorBinding

class TextTranslator : AppCompatActivity() {
    private lateinit var binding: ActivityTextTranslatorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextTranslatorBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}