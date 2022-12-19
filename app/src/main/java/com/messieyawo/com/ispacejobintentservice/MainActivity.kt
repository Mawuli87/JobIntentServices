package com.messieyawo.com.ispacejobintentservice

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.util.Log
import android.widget.ScrollView
import com.messieyawo.com.ispacejobintentservice.Constants.FILE_CONTENTS_KEY
import com.messieyawo.com.ispacejobintentservice.Constants.FILE_URL
import com.messieyawo.com.ispacejobintentservice.Constants.LOG_TAG
import com.messieyawo.com.ispacejobintentservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize button click handlers
        with(binding) {
            runButton.setOnClickListener { runCode() }
            clearButton.setOnClickListener { clearOutput() }
        }
    }

    /**
     * Run some code
     */
    private fun runCode() {
        val receiver = MyResultReceiver(Handler())
        MyIntentService.startAction(this, FILE_URL, receiver)
    }

    /**
     * Clear log display
     */
    private fun clearOutput() {
        binding.logDisplay.text = ""
        scrollTextToEnd()
    }

    /**
     * Log output to logcat and the screen
     */
    @Suppress("SameParameterValue")
    private fun log(message: String) {
        Log.i(LOG_TAG, message)
        binding.logDisplay.append(message + "\n")
        scrollTextToEnd()
    }

    /**
     * Scroll to end. Wrapped in post() function so it's the last thing to happen
     */
    private fun scrollTextToEnd() {
        Handler().post { binding.scrollView.fullScroll(ScrollView.FOCUS_DOWN) }
    }

    private inner class MyResultReceiver(handler: Handler) :
        ResultReceiver(handler) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle?) {
            if (resultCode == Activity.RESULT_OK) {
                val fileContents = resultData?.getString(FILE_CONTENTS_KEY) ?: "Empty data returnrd"
                log(fileContents)
            }
        }
    }
}