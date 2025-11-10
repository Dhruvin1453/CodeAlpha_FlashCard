package com.example.flashcard

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.animation.AnimationUtils


data class Flashcard(var question:String,var answer:String)

class MainActivity : AppCompatActivity() {

    private lateinit var tvQuestion: TextView
    private lateinit var tvAnswer: TextView
    private lateinit var cardFront: LinearLayout
    private lateinit var cardBack: LinearLayout
    private lateinit var btnPrev: Button
    private lateinit var btnNext: Button
    private lateinit var btnShow: Button
    private lateinit var btnAdd: ImageButton
    private lateinit var btnEdit: ImageButton
    private lateinit var btnDelete: ImageButton


    private var flashcard = mutableListOf(

        Flashcard("What is the capital of France?", "Paris"),
        Flashcard("Who wrote 'Hamlet'?", "William Shakespeare"),
        Flashcard("What is 9 × 9?", "81")
    )

    private var currentIndex = 0
    private var isShowingAnswer = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvQuestion = findViewById(R.id.tvQuestion)
        tvAnswer = findViewById(R.id.tvAnswer)
        cardFront = findViewById(R.id.cardFront)
        cardBack = findViewById(R.id.cardBack)
        btnPrev = findViewById(R.id.btnPrev)
        btnNext = findViewById(R.id.btnNext)
        btnShow = findViewById(R.id.button)
        btnAdd = findViewById(R.id.add)
        btnEdit = findViewById(R.id.edit)
        btnDelete = findViewById(R.id.delete)

        showCard()

        btnShow.setOnClickListener { toggleAnswer() }
        btnNext.setOnClickListener { nextCard() }
        btnPrev.setOnClickListener { prevCard() }
        btnAdd.setOnClickListener { addCard() }
        btnEdit.setOnClickListener { editCard() }
        btnDelete.setOnClickListener { deleteCard() }
    }

    private fun showCard() {

        val flashcard = flashcard[currentIndex]

        if(isShowingAnswer){
            cardFront.visibility = View.GONE
            cardBack.visibility = View.VISIBLE
        }else{
            cardFront.visibility = View.VISIBLE
            cardBack.visibility = View.GONE
        }

        tvQuestion.text = flashcard.question
        tvAnswer.text = flashcard.answer
    }

    private fun toggleAnswer() {

        val flipIn = AnimationUtils.loadAnimation(this, R.anim.flip_in)
        val flipOut = AnimationUtils.loadAnimation(this, R.anim.flip_out)

        if (isShowingAnswer) {

            cardBack.startAnimation(flipOut)
            cardBack.visibility = View.GONE
            cardFront.visibility = View.VISIBLE
            cardFront.startAnimation(flipIn)

        } else {

            cardFront.startAnimation(flipOut)
            cardFront.visibility = View.GONE
            cardBack.visibility = View.VISIBLE
            cardBack.startAnimation(flipIn)
        }

        isShowingAnswer = !isShowingAnswer
        showCard()
    }



    private fun nextCard() {
        if (flashcard.isNotEmpty()) {
            currentIndex = (currentIndex + 1) % flashcard.size
            isShowingAnswer = false
            showCard()
        }
    }

    private fun prevCard() {
        if (flashcard.isNotEmpty()) {
            currentIndex = if (currentIndex - 1 < 0) flashcard.size - 1 else currentIndex - 1
            isShowingAnswer = false
            showCard()
        }
    }

    private fun addCard() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_edit, null)
        val etQuestion = dialogView.findViewById<EditText>(R.id.etQuestion)
        val etAnswer = dialogView.findViewById<EditText>(R.id.etAnswer)

        AlertDialog.Builder(this)
            .setTitle("Add Flashcard")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val question = etQuestion.text.toString()
                val answer = etAnswer.text.toString()
                if (question.isNotEmpty() && answer.isNotEmpty()) {
                    flashcard.add(Flashcard(question, answer))
                    currentIndex = flashcard.size - 1
                    isShowingAnswer = false
                    showCard()
                } else {
                    Toast.makeText(this, "Please enter both question and answer", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun editCard() {
        val flashcard = flashcard[currentIndex]
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_edit, null)
        val etQuestion = dialogView.findViewById<EditText>(R.id.etQuestion)
        val etAnswer = dialogView.findViewById<EditText>(R.id.etAnswer)
        etQuestion.setText(flashcard.question)
        etAnswer.setText(flashcard.answer)

        AlertDialog.Builder(this)
            .setTitle("Edit Flashcard")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                flashcard.question = etQuestion.text.toString()
                flashcard.answer = etAnswer.text.toString()
                isShowingAnswer = false
                showCard()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteCard() {
        if (flashcard.isNotEmpty()) {
            flashcard.removeAt(currentIndex)
            if (flashcard.isEmpty()) {
                flashcard.add(Flashcard("No cards available", "Please add new cards"))
            }
            currentIndex %= flashcard.size
            isShowingAnswer = false
            showCard()
        }
    }
}