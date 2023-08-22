package com.example.quizcard

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.ContactsContract.CommonDataKinds.Im
import android.util.Log.v
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    lateinit var flashcardDatabase: FlashcardDatabase
    var allFlashcards = mutableListOf<Flashcard>()
    var countDownTimer: CountDownTimer? = null


    override fun onCreate(savedInstanceState: Bundle?) {


        flashcardDatabase = FlashcardDatabase(this)
        allFlashcards = flashcardDatabase.getAllCards().toMutableList()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvQuestion = findViewById<TextView>(R.id.quizcard_question)
        val tvAnswer = findViewById<TextView>(R.id.quizcard_answer)
        val btnAdd = findViewById<ImageButton>(R.id.btn_addCard)
        val btnEdit = findViewById<ImageButton>(R.id.btn_edit)
        val btnDelete =findViewById<ImageButton>(R.id.btn_delete)
        val btnNext = findViewById<ImageButton>(R.id.btn_next)
        val btnPrevious = findViewById<ImageButton>(R.id.btn_previous)

        // Toggle question and answer visibility
        tvQuestion.setOnClickListener {

                tvQuestion.animate()
                .rotationY(90f)
                .setDuration(200)
                .withEndAction(
                    Runnable {
                        tvQuestion.setVisibility(View.INVISIBLE)
                      tvAnswer.visibility = View.VISIBLE
                        // second quarter turn
                        tvAnswer.rotationY = -90f
                        tvAnswer.animate()
                            .rotationY(0f)
                            .setDuration(200)
                            .start()
                    }
                ).start()
        }


        tvAnswer.setOnClickListener {

            tvAnswer.animate()
                .rotationY(90f)
                .setDuration(200)
                .withEndAction(
                    Runnable {
                        tvAnswer.setVisibility(View.INVISIBLE)
                        tvQuestion.visibility = View.VISIBLE
                        // second quarter turn
                        tvQuestion.rotationY = -90f
                        tvQuestion.animate()
                            .rotationY(0f)
                            .setDuration(200)
                            .start()
                    }
                ).start()

        }

        fun startTimer() {
            countDownTimer?.cancel() // Cancel any existing timer
            countDownTimer?.start() // Start the new timer
        }


//        Initialize timer
        countDownTimer = object : CountDownTimer(16000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                findViewById<TextView>(R.id.timer).text = "" + millisUntilFinished / 1000
            }

            override fun onFinish() {}
        }




        // first check the database to see if there's any saved flashcards
        var cardIndex = 0

        fun updateFlashcardDisplay() {
            if (allFlashcards.isEmpty()) {
                Snackbar.make(
                    tvQuestion, "No flashcards available.",
                    Snackbar.LENGTH_SHORT
                ).show()
                return
            }

            val (question, answer) = allFlashcards[cardIndex]
            tvQuestion.text = question
            tvAnswer.text = answer
        }

        btnNext.setOnClickListener{
            if (allFlashcards.isEmpty()) {
                Snackbar.make(
                    tvQuestion, "No flashcards available.",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }


            cardIndex ++

            if(cardIndex>=allFlashcards.size){

                //show cards in random order
                allFlashcards.shuffle()

                Snackbar.make(
                    tvQuestion, "You've reached the end of the cards.",
                    Snackbar.LENGTH_SHORT
                ).show()
                cardIndex = 0
            }

            startTimer()
            updateFlashcardDisplay()

            val leftOutAnim = AnimationUtils.loadAnimation(this, R.anim.left_out)
            val rightInAnim = AnimationUtils.loadAnimation(this, R.anim.right_in)


            leftOutAnim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {
                    // Called when the animation starts
                }

                override fun onAnimationEnd(animation: Animation?) {
                    // Called when the animation ends
                    tvQuestion.startAnimation(rightInAnim) // Start the rightIn animation here
                }

                override fun onAnimationRepeat(animation: Animation?) {

                }
            })
            // Start the leftOut animation on the view
            tvQuestion.startAnimation(leftOutAnim)
        }

         // Previous Button
        btnPrevious.setOnClickListener{
            if (allFlashcards.isEmpty()){
                return@setOnClickListener
            }


            cardIndex --

            if(cardIndex<0){


                cardIndex = allFlashcards.size -1

            }

            // pull the question and answer from the database
           updateFlashcardDisplay()
        }


        btnDelete.setOnClickListener{
            if (allFlashcards.isEmpty()) {
                Snackbar.make(
                    tvQuestion, "No flashcards available.",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
                // Delete card
            allFlashcards = allFlashcards.toMutableList().apply{removeAt(cardIndex) }

            if (allFlashcards.isEmpty()) {
                tvQuestion.text = ""
                tvAnswer.text = ""
                Snackbar.make(
                    tvQuestion, "Flashcard deleted. No more flashcards.",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                if (cardIndex >= allFlashcards.size) {
                    cardIndex = 0
                }
                updateFlashcardDisplay()
            }


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
            overridePendingTransition(R.anim.right_in, R.anim.left_out)
        }


        btnEdit.setOnClickListener {
            val intent = Intent(this, AddCardActivity::class.java)
            val question = tvQuestion.text.toString()
            val answer = tvAnswer.text.toString()

            intent.putExtra("question", question)
            intent.putExtra("answer", answer)
            addCardLauncher.launch(intent)
            overridePendingTransition(R.anim.right_in, R.anim.left_out)


        }



    }


}