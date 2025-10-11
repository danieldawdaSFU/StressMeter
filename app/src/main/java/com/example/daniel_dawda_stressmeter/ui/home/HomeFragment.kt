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
import java.io.InputStream
import java.io.OutputStream

class HomeFragment : Fragment() {

    data class StressData(val timestamp: Long, val stressLevel: Int)
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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
            FileOutputStream(file, true).apply { writeCsv(listOf(StressData(System.currentTimeMillis(), position))) }
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