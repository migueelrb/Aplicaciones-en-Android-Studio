package com.example.otakuizz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.otakuizz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding : ActivityMainBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        binding.tvHello.text = getString(R.string.hello_payer)
        setContentView(view)

    }
}