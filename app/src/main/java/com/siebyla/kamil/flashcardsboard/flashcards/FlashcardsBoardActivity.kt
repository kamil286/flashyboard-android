package com.siebyla.kamil.flashcardsboard.flashcards

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.siebyla.kamil.flashcardsboard.R
import com.siebyla.kamil.flashcardsboard.adapters.FlashcardsAdapter
import com.siebyla.kamil.flashcardsboard.authorization.LoginActivity
import com.siebyla.kamil.flashcardsboard.authorization.RegisterActivity
import com.siebyla.kamil.flashcardsboard.models.Flashcard
import java.util.*

class FlashcardsBoardActivity : AppCompatActivity() {

    private var flashcardsList: MutableList<Flashcard>? = null
    private lateinit var adapter: FlashcardsAdapter
    private var listViewItems: ListView? = null

    companion object {
        const val TAG = "FlashcardsBoardActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flashcards_board)

        supportActionBar?.title = "FLASHCARDS"

        verifyUserIsLoggedIn()

        listViewItems = findViewById<View>(R.id.list_view_new_flashcard) as ListView

        val mDatabase = FirebaseDatabase.getInstance().reference
        flashcardsList = mutableListOf<Flashcard>()
        adapter = FlashcardsAdapter(this, flashcardsList!!)
        listViewItems!!.adapter = adapter
        mDatabase.orderByKey().addListenerForSingleValueEvent(itemListener)
    }

    private var itemListener: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            // Get Post object and use the values to update the UI
            addDataToList(dataSnapshot)
        }
        override fun onCancelled(databaseError: DatabaseError) {
            // Getting Item failed, log a message
            Log.w(TAG, "loadItem:onCancelled", databaseError.toException())
        }
    }
    private fun addDataToList(dataSnapshot: DataSnapshot) {
        val items = dataSnapshot.children.iterator()
        //Check if current database contains any collection
        if (items.hasNext()) {
            val flashcardsListIndex = items.next()
            val itemsIterator = flashcardsListIndex.children.iterator()
            //check if the collection has any to do items or not
            while (itemsIterator.hasNext()) {
                //get current item
                val currentItem = itemsIterator.next()
                val flashcardItem = Flashcard.create()
                //get current data in a map
                val map = currentItem.value as HashMap<String, Any>
                flashcardItem.objectId = currentItem.key
                flashcardItem.title = map["title"] as String?
                flashcardItem.content = map["content"] as String?
                flashcardsList!!.add(flashcardItem)
            }
        }
        //alert adapter that has changed
        adapter.notifyDataSetChanged()
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
}


