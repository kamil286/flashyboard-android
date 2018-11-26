package com.siebyla.kamil.flashcardsboard.flashcards

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.siebyla.kamil.flashcardsboard.R
import com.siebyla.kamil.flashcardsboard.models.Flashcard
import kotlinx.android.synthetic.main.activity_create_flashcard.*


class CreateFlashcardActivity : AppCompatActivity() {

    companion object {
        const val TAG = "CreateFlashcardActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_flashcard)

        supportActionBar?.title = "CREATE FLASHCARDS"

        button_save_flashcard.setOnClickListener {
            performCreateNewFlashcard()
        }
    }

    private fun performCreateNewFlashcard() {
        val title = edittext_flashcard_title.text.toString()
        val content = edittext_flashcard_content.text.toString()

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Please do not leave empty email/password input",
                Toast.LENGTH_SHORT).show()
            return
        }
        saveFlashCardToFirebaseDatabase()
    }

    private fun saveFlashCardToFirebaseDatabase() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/flashcards/$uid")
        val flashcard = Flashcard(
            uid, edittext_flashcard_title.text.toString(),
            edittext_flashcard_content.text.toString(), uid, ""
        )

        ref.setValue(flashcard)
            .addOnSuccessListener {
                Log.d(TAG, "Finally we saved the flascard to firebase database")

                val intent = Intent(this, FlashcardsBoardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to set value to database: ${it.message}")
            }
    }
}
