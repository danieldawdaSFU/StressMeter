package com.example.daniel_dawda_stressmeter.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.daniel_dawda_stressmeter.R
import com.example.daniel_dawda_stressmeter.databinding.FragmentHomeBinding
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class HomeFragment : Fragment() {

    data class StressData(val timestamp: Long, val stressLevel: Int?)
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // to map positions to stress levels
    val stressMap: Map<Int, Int> = mapOf(
        0 to 6,
        1 to 8,
        2 to 14,
        3 to 16,
        4 to 5,
        5 to 7,
        6 to 13,
        7 to 15,
        8 to 2,
        9 to 4,
        10 to 10,
        11 to 12,
        12 to 1,
        13 to 3,
        14 to 9,
        15 to 11
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val gridView: GridView = binding.gridView
        val list = ArrayList<Int>()

        for (i in 0..15) {
            list.add(R.drawable.psm_alarm_clock)
        }

        val adapter = GridViewAdapter(requireContext(), list)
        gridView.adapter = adapter

        // get position of click from gridview and sends it and timestamp to csv
        gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val file = File(requireContext().filesDir, "stress_data.csv")
            FileOutputStream(file, true).apply { writeCsv(listOf(StressData(System.currentTimeMillis(), stressMap[position]))) }
            Toast.makeText(requireContext(), "Clicked index: $position", Toast.LENGTH_SHORT).show()
        }

        return root

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // csv read and write adapted from https://www.baeldung.com/kotlin/csv-files
    fun OutputStream.writeCsv(sData: List<StressData>) {
        val writer = bufferedWriter()

        sData.forEach {
            writer.write("${it.timestamp},${it.stressLevel}")
            writer.newLine()
        }
        writer.flush()
    }
}