package com.example.daniel_dawda_stressmeter.ui.stress_meter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.daniel_dawda_stressmeter.R
import com.example.daniel_dawda_stressmeter.ui.home.HomeFragment

// adapted GeeksForGeeks tutorial: https://www.geeksforgeeks.org/android/gridview-in-android-with-example/
class StressAdapter(context: Context, private val list: List<StressMeterFragment.StressDataList>):
    ArrayAdapter<StressMeterFragment.StressDataList?>(context, 0, list) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {

        var itemView = view
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        }

        // find views in list
        val indexView = itemView.findViewById<TextView>(R.id.index_item)
        val timeView = itemView.findViewById<TextView>(R.id.time_item)
        val levelView = itemView.findViewById<TextView>(R.id.level_item)

        // extract data from list position
        val (index, timestamp, level) = list[position]
        indexView.text = index.toString()
        timeView.text = timestamp
        levelView.text = level.toString()

        return itemView
    }
}