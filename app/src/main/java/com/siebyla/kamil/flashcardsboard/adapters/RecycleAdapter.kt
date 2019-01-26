package com.siebyla.kamil.flashcardsboard.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.siebyla.kamil.flashcardsboard.R
import com.siebyla.kamil.flashcardsboard.models.Flashcard
import kotlinx.android.synthetic.main.single_row_new_flashcard.view.*

class RecycleAdapter(private val myDataset: ArrayList<Flashcard>) :
    RecyclerView.Adapter<RecycleAdapter.MyViewHolder>() {

    class MyViewHolder(private val textView: TextView) : RecyclerView.ViewHolder(textView)

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): RecycleAdapter.MyViewHolder {

        val flashcardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_row_new_flashcard, parent, false) as TextView

        return MyViewHolder(flashcardView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.flashcard_title_text.text = myDataset[position].title
        holder.itemView.flascard_content_text.text = myDataset[position].content
    }

    override fun getItemCount() = myDataset.size
}