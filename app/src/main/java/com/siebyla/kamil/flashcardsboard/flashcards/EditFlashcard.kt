package com.siebyla.kamil.flashcardsboard.flashcards

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.siebyla.kamil.flashcardsboard.R
import com.siebyla.kamil.flashcardsboard.models.Flashcard
import kotlinx.android.synthetic.main.activity_create_flashcard.*
import kotlinx.android.synthetic.main.activity_edit_flashcard.*

class EditFlashcard : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_flashcard)

        supportActionBar?.title = "EDIT FLASHCARDS"

        button_save_flashcard_edit_flashcard.setOnClickListener {
            performEditFlashcard()
        }
    }

    private fun performEditFlashcard() {
        val title = edittext_flashcard_title_edit_flashcard.text.toString()
        val content = edittext_flashcard_content_edit_flashcard.text.toString()

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Please do not leave empty title/content input",
                Toast.LENGTH_SHORT).show()
            return
        }
        saveFlashCardToFirebaseDatabase()
    }

    private fun saveFlashCardToFirebaseDatabase() {
        var uid = ""
        val bundle = intent.extras
        if (bundle != null) {
            uid = bundle.getString("uid")
        }
        val userId = FirebaseAuth.getInstance().uid.toString()
        val ref = FirebaseDatabase.getInstance().getReference("/flashcards")
        val flashcard = Flashcard(userId, uid, edittext_flashcard_title.text.toString(),  edittext_flashcard_content.text.toString())

        ref!!.child(uid).setValue(flashcard)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Flashcard has been updated!", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, FlashcardsBoardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener{e -> run {
                Toast.makeText(applicationContext, "I cant update flashcard because of error: $e", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
