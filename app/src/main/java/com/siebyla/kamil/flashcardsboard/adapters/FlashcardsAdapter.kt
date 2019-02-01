//package com.siebyla.kamil.flashcardsboard.adapters
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.BaseAdapter
//import android.widget.ImageView
//import android.widget.TextView
//import com.siebyla.kamil.flashcardsboard.R
//import com.siebyla.kamil.flashcardsboard.models.Flashcard
//import com.squareup.picasso.Picasso
//
//class FlashcardsAdapter(context: Context, flashcards: MutableList<Flashcard>): BaseAdapter() {
//
//    private val mInflater: LayoutInflater = LayoutInflater.from(context)
//    private var itemList = flashcards
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
//        val objectId: String = itemList[position].objectId as String
//        val title: String = itemList[position].title as String
//        val content: String = itemList[position].content as String
//
//        val view: View
//        val vh: ListRowHolder
//        if (convertView == null) {
//            view = mInflater.inflate(R.layout.single_row_new_flashcard, parent, false)
//            vh = ListRowHolder(view)
//            view.tag = vh
//        } else {
//            view = convertView
//            vh = view.tag as ListRowHolder
//        }
//        vh.title.text = title
//        vh.content.text = content
//        Picasso.get().load(R.drawable.avatar1).into(vh.image)
//        return view
//    }
//
//    override fun getItem(position: Int): Any {
//        return  itemList[position]
//    }
//
//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }
//
//    override fun getCount(): Int {
//        return itemList.size
//    }
//
//    private class ListRowHolder(row: View?) {
//        val title: TextView = row!!.findViewById(R.id.flashcard_title_text) as TextView
//        val content: TextView = row!!.findViewById(R.id.flascard_content_text) as TextView
//        val image: ImageView = row!!.findViewById(R.id.imageview_new_flashcard) as ImageView
//    }
//}
