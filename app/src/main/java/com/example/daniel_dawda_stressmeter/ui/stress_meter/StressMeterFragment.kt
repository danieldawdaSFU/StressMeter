package com.example.daniel_dawda_stressmeter.ui.stress_meter

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.daniel_dawda_stressmeter.R
import com.example.daniel_dawda_stressmeter.databinding.FragmentSlideshowBinding
import com.example.daniel_dawda_stressmeter.ui.home.GridViewAdapter
import com.example.daniel_dawda_stressmeter.ui.home.HomeFragment.StressData
import lecho.lib.hellocharts.model.Axis
import lecho.lib.hellocharts.model.Line
import lecho.lib.hellocharts.model.LineChartData
import lecho.lib.hellocharts.model.PointValue
import lecho.lib.hellocharts.view.LineChartView
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import lecho.lib.hellocharts.formatter.AxisValueFormatter
import lecho.lib.hellocharts.gesture.ZoomType
import lecho.lib.hellocharts.model.AxisValue
import lecho.lib.hellocharts.model.Viewport
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.min
import kotlin.text.trim


class StressMeterFragment : Fragment() {

    private var _binding: FragmentSlideshowBinding? = null
    private var is_file = true

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var stressList: List<StressDataList>
    private lateinit var chartValues: List<PointValue>

    // object that goes into listview
    data class StressDataList(val index: Int, val timestamp: String, val stressLevel: Int)

    // adapted from https://developer.android.com/reference/android/icu/text/SimpleDateFormat
    val sdf = SimpleDateFormat("MM:dd HH:mm:ss", Locale.getDefault())

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

        stressList = emptyList()
        chartValues = emptyList()

        try {
            val file = File(requireContext().filesDir, "stress_data.csv")
            stressList = readCsv(FileInputStream(file), false) as List<StressDataList>
            chartValues = readCsv(FileInputStream(file), true) as List<PointValue>
        } catch (e: FileNotFoundException) {
            Toast.makeText(requireContext(), "No data collected yet", Toast.LENGTH_SHORT).show()
        }


        val adapter = StressAdapter(requireContext(), stressList)
        listView.adapter = adapter

        // line chart adapted from https://github.com/lecho/hellocharts-android Usage
        val chart : LineChartView = binding.lineChart

        val line = Line(chartValues).setColor(Color.BLUE).setCubic(true)
        val lines = listOf(line)

        val data = LineChartData()
        data.lines = lines

        chart.lineChartData = data


        // create and label axis
        // x axis
        val xAxis = Axis()
        xAxis.setName("Instance")

        // y axis
        val yAxis = Axis()
        yAxis.setName("Stress Level")

        data.axisXBottom = xAxis
        data.axisYLeft = yAxis

        // makes all points viewable
        // these 3 sections adapted from ChatGPT
        // 2. Set chart interaction options
        chart.isInteractive = true
        chart.zoomType = ZoomType.HORIZONTAL
        chart.isZoomEnabled = true
        chart.isScrollEnabled = true

        // 3. Control the visible viewport (like a camera window)
        val maxViewport = Viewport(chart.maximumViewport)
        chart.maximumViewport = maxViewport

        // You can set how much of it is visible at once:
        val visibleViewport = Viewport(maxViewport)
        visibleViewport.left = 0f
        visibleViewport.right = min(10f, stressList.size.toFloat())
        visibleViewport.top = 16f
        visibleViewport.bottom = 1f
        chart.currentViewport = visibleViewport

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // csv read and write adapted from https://www.baeldung.com/kotlin/csv-files
    fun readCsv(inputStream: InputStream, chart : Boolean): List<Any> {
        val reader = inputStream.bufferedReader()
        // read into chart data
        if (chart) {
            return reader.lineSequence()
                .filter { it.isNotBlank() }
                .mapIndexed { index, value ->
                    val (timeStamp, stressLevel) = value.split(',', ignoreCase = false, limit = 2)
                    PointValue(index.toFloat(), stressLevel.trim().toFloat())
                }.toList()
        }
        // read into StressDataList
        else {
            return reader.lineSequence()
                .filter { it.isNotBlank() }
                .mapIndexed { index, value ->
                    val (timeStamp, stressLevel) = value.split(',', ignoreCase = false, limit = 2)
                    StressDataList(index, sdf.format(Date(timeStamp.trim().toLong())), stressLevel.trim().toInt())
                }.toList()
        }
    }
}
