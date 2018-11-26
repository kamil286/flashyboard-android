package com.siebyla.kamil.flashcardsboard.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.siebyla.kamil.flashcardsboard.R
import com.siebyla.kamil.flashcardsboard.models.Flashcard
import com.squareup.picasso.Picasso

class FlashcardsAdapter(context: Context, private val dataSource: ArrayList<Flashcard>): BaseAdapter() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val rowView = inflater.inflate(R.layout.single_row_new_flashcard, p2, false)
        val titleTextView = rowView.findViewById(R.id.flashcard_title_text) as TextView
        val contentTextView = rowView.findViewById(R.id.flascard_content_text) as TextView
        val flashcardImageView = rowView.findViewById(R.id.imageview_new_flashcard) as ImageView

        val flashcard = getItem(p0) as Flashcard
        titleTextView.text = flashcard.title
        contentTextView.text = flashcard.content
        Picasso.get().load(flashcard.imageUrl).into(flashcardImageView)

        return  rowView
    }

    override fun getItem(p0: Int): Any {
        return  dataSource[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return dataSource.size
    }
}