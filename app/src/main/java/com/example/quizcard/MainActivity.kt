package com.example.quizcard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Toggle question and answer visibility
        findViewById<View>(R.id.quizcard_question).setOnClickListener {
            findViewById<View>(R.id.quizcard_answer).visibility =View.VISIBLE
            findViewById<View>(R.id.quizcard_question).visibility =View.INVISIBLE
        }

        findViewById<View>(R.id.quizcard_answer).setOnClickListener {
            findViewById<View>(R.id.quizcard_question).visibility = View.VISIBLE
            findViewById<View>(R.id.quizcard_answer).visibility = View.INVISIBLE

        }
    }


}