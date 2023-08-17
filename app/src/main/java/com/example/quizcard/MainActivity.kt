package com.example.quizcard

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    lateinit var flashcardDatabase: FlashcardDatabase
    var allFlashcards = mutableListOf<Flashcard>()
    override fun onCreate(savedInstanceState: Bundle?) {

        flashcardDatabase = FlashcardDatabase(this)
        allFlashcards = flashcardDatabase.getAllCards().toMutableList()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvQuestion = findViewById<TextView>(R.id.quizcard_question)
        val tvAnswer = findViewById<TextView>(R.id.quizcard_answer)
        val btnAdd = findViewById<ImageButton>(R.id.btn_addCard)
        val btnEdit = findViewById<ImageButton>(R.id.btn_edit)
        // Toggle question and answer visibility
        tvQuestion.setOnClickListener {
            tvAnswer.visibility =View.VISIBLE
            tvQuestion.visibility =View.INVISIBLE
        }

        tvAnswer.setOnClickListener {
            tvQuestion.visibility = View.VISIBLE
            tvAnswer.visibility = View.INVISIBLE

        }

        // first check the database to see if there's any saved flashcards
        if(allFlashcards.size>0){
            tvQuestion.text = allFlashcards[0].question
            tvAnswer.text = allFlashcards[0].answer

        }





        val addCardLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result?.data
                val question = data?.getStringExtra("question")
                val answer = data?.getStringExtra("answer")


                if (!question.isNullOrEmpty()) {
                    tvQuestion.text = question
                }

                if (!answer.isNullOrEmpty()) {
                    tvAnswer.text = answer
                }

                // Card insertion
                if(!question.isNullOrEmpty() && !answer.isNullOrEmpty()){
                flashcardDatabase.insertCard(Flashcard(question, answer))
                }
            }
        }




        btnAdd.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            addCardLauncher.launch(intent)
        }


        btnEdit.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            val question = tvQuestion.text.toString()
            val answer = tvAnswer.text.toString()

            intent.putExtra("question", question)
            intent.putExtra("answer", answer)
            addCardLauncher.launch(intent)

        }



    }


}