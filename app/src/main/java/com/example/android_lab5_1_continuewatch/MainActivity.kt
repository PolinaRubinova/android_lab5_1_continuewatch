package com.example.android_lab5_1_continuewatch

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity: AppCompatActivity() {
    private var secondsElapsed: Int = 0
    private lateinit var textSecondsElapsed: TextView
    private lateinit var sharedPref: SharedPreferences

    private var backgroundThread = Thread {
        try {
            while (!Thread.currentThread().isInterrupted) {
                Log.d("MainActivity", "${Thread.currentThread()} is iterating")
                textSecondsElapsed.post {
                    textSecondsElapsed.text = getString(R.string.sec_elapsed, secondsElapsed++)
                }
                Thread.sleep(1000)
            }
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textSecondsElapsed = findViewById(R.id.textSecondsElapsed)
        sharedPref = getSharedPreferences("SEC", Context.MODE_PRIVATE)
    }

    override fun onStart() {
        backgroundThread.start()
        secondsElapsed = sharedPref.getInt("SEC", 0)
        Log.d("MainActivity", "backgroundThread start\nSEC = $secondsElapsed")
        super.onStart()
    }

    override fun onStop() {
        backgroundThread.interrupt()
        sharedPref.edit().putInt("SEC", secondsElapsed).apply()
        Log.d("MainActivity", "backgroundThread interrupt\nSEC = $secondsElapsed")
        super.onStop()
    }
}