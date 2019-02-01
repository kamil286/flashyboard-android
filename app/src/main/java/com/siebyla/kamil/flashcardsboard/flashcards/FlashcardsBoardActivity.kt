package com.siebyla.kamil.flashcardsboard.flashcards

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.siebyla.kamil.flashcardsboard.R
import com.siebyla.kamil.flashcardsboard.authorization.LoginActivity
import com.siebyla.kamil.flashcardsboard.authorization.RegisterActivity
import com.siebyla.kamil.flashcardsboard.models.Flashcard
import com.siebyla.kamil.flashcardsboard.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_flashcards_board.*
import kotlinx.android.synthetic.main.single_row_new_flashcard.*
import kotlinx.android.synthetic.main.single_row_new_flashcard.view.*

class FlashcardsBoardActivity : AppCompatActivity() {

    var userToReturn: User = User("","","")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcards_board)

        supportActionBar?.title = "FLASHCARDS"

        //attachListeners()
        verifyUserIsLoggedIn()
        fetchFlashcards()
    }

    private fun fetchFlashcards() {
        val ref = FirebaseDatabase.getInstance().getReference("/flashcards")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                data.children.forEach {
                    Log.d("Flashcards", it.toString())
                    val flashcard = it.getValue(Flashcard::class.java)
                    if (flashcard != null) {
                        adapter.add(FlashcardItem(flashcard))
                    }
                }
                recycler_view.adapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun getUserForFlashcard(userId: String) {
        val ref = FirebaseDatabase.getInstance().getReference("/users").child(userId)
        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(data: DataSnapshot) {
                data.children.forEach{
                    val user = it.getValue(User::class.java)
                    if (user != null && user.uid == userId) {
                        userToReturn = user
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_new_flashcard -> {
                val intent = Intent(this, CreateFlashcardActivity::class.java)
                startActivity(intent)
            }
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun attachListeners() {
        edit_flashcard_button.setOnClickListener {
            val intent = Intent(this, EditFlashcard::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("uid", "1")
            startActivity(intent)
        }

        delete_flashcard_button.setOnClickListener {
            val ref = FirebaseDatabase.getInstance().getReference("/flashcards")
            ref.child("1").removeValue()
                .addOnCompleteListener {
                    Toast.makeText(applicationContext, "Flashcard has been deleted!", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(applicationContext, "Cant delete flashcard because of error: $e", Toast.LENGTH_SHORT).show()
                }
        }
    }
}

class FlashcardItem(private val flashcard: Flashcard): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.flashcard_title_text.text = flashcard.title
        viewHolder.itemView.flascard_content_text.text = flashcard.content
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/flascardboard.appspot.com/o/images%2F1af0a3ab-72e8-4f41-b128-70f8efed5ab8?alt=media&token=47e40e0e-06c5-4795-8f31-2d8526e8799d").into(viewHolder.itemView.imageview_new_flashcard)
    }

    override fun getLayout(): Int {
        return  R.layout.single_row_new_flashcard
    }
}


