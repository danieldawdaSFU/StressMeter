package com.example.daniel_dawda_stressmeter.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.example.daniel_dawda_stressmeter.R

// adapted GeeksForGeeks tutorial: https://www.geeksforgeeks.org/android/gridview-in-android-with-example/
class GridViewAdapter(context: Context, private val images: ArrayList<Int>):
    ArrayAdapter<Int?>(context, 0, images) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {

        var itemView = view
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.image_card, parent, false)
        }

        val imageView = itemView.findViewById<ImageView>(R.id.image_view)
        val image = getItem(position)

        // This one line from ChatGPT
        image?.let { imageView.setImageResource(it) }

        return itemView
    }
}