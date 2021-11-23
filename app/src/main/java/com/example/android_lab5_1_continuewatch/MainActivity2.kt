package com.example.android_lab5_1_continuewatch

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity2 : AppCompatActivity() {
    private var secondsElapsed: Int = 0
    lateinit var textSecondsElapsed: TextView
    private lateinit var sharedPref: SharedPreferences
    private lateinit var executor: ExecutorService

    private fun startTimer() {
        executor = Executors.newFixedThreadPool(1)
        executor.execute {
            while(!executor.isShutdown) {
                Log.d("MainActivity2", "${Thread.currentThread()} is iterating")
                textSecondsElapsed.post {
                    textSecondsElapsed.text = getString(R.string.sec_elapsed, secondsElapsed++)
                }
                Thread.sleep(1000)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textSecondsElapsed = findViewById(R.id.textSecondsElapsed)
        sharedPref = getSharedPreferences("SEC", Context.MODE_PRIVATE)
    }

    override fun onStart() {
        startTimer()
        secondsElapsed = sharedPref.getInt("SEC", 0)
        Log.d("MainActivity2", "on start\nSEC = $secondsElapsed")
        super.onStart()
    }

    override fun onStop() {
        executor.shutdown()
        sharedPref.edit().putInt("SEC", secondsElapsed).apply()
        Log.d("MainActivity2", "on stop\nSEC = $secondsElapsed")
        super.onStop()
    }
}