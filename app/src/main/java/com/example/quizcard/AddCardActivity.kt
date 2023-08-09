package com.example.quizcard

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton

class AddCardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)

        val btnClose = findViewById<ImageButton>(R.id.btn_cancel)
        val btnSave = findViewById<ImageButton>(R.id.btn_save)
        val editTextQuestion = findViewById<EditText>(R.id.et_question)
        val editTextAnswer =findViewById<EditText>(R.id.et_answer)

        btnClose.setOnClickListener {
            finish()
        }
        btnSave.setOnClickListener {
            val question = editTextQuestion.text.toString()
            val answer = editTextAnswer.text.toString()
            val contents = Intent()
            contents.putExtra("question",question)
            contents.putExtra("answer",answer)
            setResult(Activity.RESULT_OK,contents)
            finish()

        }
    }


}