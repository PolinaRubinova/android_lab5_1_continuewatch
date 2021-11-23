package com.example.android_lab5_1_continuewatch

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*

class MainActivity3 : AppCompatActivity() {
    private var secondsElapsed: Int = 0
    private lateinit var textSecondsElapsed: TextView
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textSecondsElapsed = findViewById(R.id.textSecondsElapsed)
        sharedPref = getSharedPreferences("SEC", Context.MODE_PRIVATE)

        lifecycleScope.launchWhenStarted {
            Log.d("MainActivity3", "coroutine launch")
            while (true) {
                Log.d("MainActivity3", "coroutine work")
                textSecondsElapsed.post {
                    textSecondsElapsed.text = getString(R.string.sec_elapsed, secondsElapsed++)
                }
                delay(1000)
            }
        }
    }

    override fun onStart() {
        secondsElapsed = sharedPref.getInt("SEC", 0)
        Log.d("MainActivity3", "on start\nSEC = $secondsElapsed")
        super.onStart()
    }

    override fun onStop() {
        sharedPref.edit().putInt("SEC", secondsElapsed).apply()
        Log.d("MainActivity3", "on stop\nSEC = $secondsElapsed")
        super.onStop()
    }
}