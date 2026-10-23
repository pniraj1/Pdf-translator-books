package com.example.geminipdftranslator

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.geminipdftranslator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.statusText.text = getString(R.string.app_ready)
    }
}
