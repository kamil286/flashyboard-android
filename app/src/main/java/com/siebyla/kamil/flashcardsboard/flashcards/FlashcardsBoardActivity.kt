package com.siebyla.kamil.flashcardsboard.flashcards

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.siebyla.kamil.flashcardsboard.R
import com.siebyla.kamil.flashcardsboard.authorization.RegisterActivity
import com.siebyla.kamil.flashcardsboard.models.Flashcard
import com.siebyla.kamil.flashcardsboard.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_create_flashcard.view.*
import kotlinx.android.synthetic.main.single_row_new_flashcard.view.*

class FlashcardsBoardActivity : AppCompatActivity() {

    companion object {
        const val TAG = "CreateFlashcardActivity"
        val USERS: MutableList<User> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcards_board)

        supportActionBar?.title = "FLASHCARDS"

        verifyUserIsLoggedIn()
        fetchUsers()
        fetchFlashcards(this)
    }

    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, RegisterActivity::class.java)
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

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    Log.d(TAG, it.toString())
                    val user = it.getValue(User::class.java)
                    if (user != null) {
                        USERS.add(user)
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) { }
        })
    }

    private fun fetchFlashcards(context: Context) {
        val ref = FirebaseDatabase.getInstance().getReference("/flashcards")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach {
                    Log.d(TAG, it.toString())
                    val flashcard = it.getValue(Flashcard::class.java)
                    if (flashcard != null) {
                        val connectedUser = USERS.find { x -> x.uid == flashcard.userUid }
                        if (connectedUser == null) {
                            Log.d(TAG, "Something wen wrong with connected user!")
                        }
                        adapter.add(FlashcardItem(flashcard, connectedUser!!.profileImageUrl))
                    }
                }
                //TODO: Ivestigate why adapter is not attached to recycler view.
                //this is place where solution could be: http://www.chansek.com/RecyclerView-no-adapter-attached-skipping-layout
                val recyclerView = R.id.recyclerview_new_flashcard as RecyclerView
                val manager = LinearLayoutManager(context)
                recyclerView.layoutManager = manager
                recyclerView.setHasFixedSize(true)
                recyclerView.adapter = adapter
            }
            override fun onCancelled(p0: DatabaseError) { }
        })
    }
}

class FlashcardItem(private val flashcard: Flashcard, private val imageUrl: String): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.edittext_flashcard_title.setText(flashcard.title)
        viewHolder.itemView.edittext_flashcard_content.setText(flashcard.content)
        Picasso.get().load(imageUrl).into(viewHolder.itemView.imageview_new_flashcard)
    }

    override fun getLayout(): Int {
        return R.layout.single_row_new_flashcard
    }
}

