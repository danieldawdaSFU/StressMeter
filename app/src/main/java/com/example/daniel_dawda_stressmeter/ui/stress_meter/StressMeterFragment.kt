package com.example.daniel_dawda_stressmeter.ui.stress_meter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.daniel_dawda_stressmeter.R
import com.example.daniel_dawda_stressmeter.databinding.FragmentSlideshowBinding
import com.example.daniel_dawda_stressmeter.ui.home.GridViewAdapter
import com.example.daniel_dawda_stressmeter.ui.home.HomeFragment.StressData
import lecho.lib.hellocharts.model.Line
import lecho.lib.hellocharts.view.LineChartView
import java.io.File
import java.io.FileInputStream
import java.io.InputStream


class StressMeterFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val stressMeterViewModel =
            ViewModelProvider(this).get(StressMeterViewModel::class.java)

        _binding = FragmentSlideshowBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val listView: ListView = binding.stressList

        val file = File(requireContext().filesDir, "stress_data.csv")
        val stressList: List<StressData> = readCsv(FileInputStream(file))

        val adapter = StressAdapter(requireContext(), stressList as ArrayList<StressData>)
        listView.adapter = adapter

        // line chart
        val chart : LineChartView = binding.lineChart
        val line : Line = Line()


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // csv read and write adapted from https://www.baeldung.com/kotlin/csv-files
    fun readCsv(inputStream: InputStream): List<StressData> {
        val reader = inputStream.bufferedReader()
        return reader.lineSequence()
            .filter { it.isNotBlank() }
            .map {
                val (timeStamp, stressLevel) = it.split(',', ignoreCase = false, limit = 2)
                StressData(timeStamp.trim().toLong(), stressLevel.trim().toInt())
            }.toList()
    }
}