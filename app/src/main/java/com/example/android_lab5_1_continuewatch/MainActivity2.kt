package com.example.android_lab5_1_continuewatch

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.concurrent.ExecutorService
import java.util.concurrent.Future

class MainActivity2 : AppCompatActivity() {
    private var secondsElapsed: Int = 0
    private lateinit var textSecondsElapsed: TextView
    private lateinit var sharedPref: SharedPreferences
    private lateinit var future: Future<*>

    private fun startTimer(execServ: ExecutorService): Future<*> {
        return execServ.submit {
            while(!execServ.isShutdown) {
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
        future = startTimer((applicationContext as MyApplication).executorService)
        secondsElapsed = sharedPref.getInt("SEC", 0)
        Log.d("MainActivity2", "on start\nSEC = $secondsElapsed")
        super.onStart()
    }

    override fun onStop() {
        future.cancel(true)
        sharedPref.edit().putInt("SEC", secondsElapsed).apply()
        Log.d("MainActivity2", "on stop\nSEC = $secondsElapsed")
        super.onStop()
    }
}