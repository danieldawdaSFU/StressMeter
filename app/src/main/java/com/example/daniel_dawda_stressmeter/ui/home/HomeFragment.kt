package com.example.daniel_dawda_stressmeter.ui.home

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.ImageView
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.daniel_dawda_stressmeter.R
import com.example.daniel_dawda_stressmeter.databinding.FragmentHomeBinding
import java.io.ByteArrayOutputStream

class HomeFragment : Fragment() {

    val prefKey = "key"
    val indexKey = "ind"
    val photoKey = "photo"
    data class StressData(val timestamp: Long, val stressLevel: Int?)
    private var _binding: FragmentHomeBinding? = null
    lateinit var list: ArrayList<Int>


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

    // 3 image sets
    var imageSet = 0;

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
        list = ArrayList<Int>()
        setImageList()

        // custom adapter to populate gridview
        var adapter = GridViewAdapter(requireContext(), list)
        gridView.adapter = adapter

        // get position of click from gridview and sends it and timestamp to csv
        gridView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            // from my myRuns2 submission
            // for saving photo
            // adapted from https://stackoverflow.com/questions/48437564/how-can-i-convert-bitmap-to-string-string-to-bitmap-in-kotlin
            val imageView = view.findViewById<ImageView>(R.id.image_view)
            val strm = ByteArrayOutputStream()
            val bitmap: Bitmap = imageView.drawable.toBitmap()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, strm)
            val b = strm.toByteArray()
            val photoString: String = Base64.encodeToString(b, Base64.DEFAULT)

            // save info
            saveInfo(stressMap[position], photoString)

            val intent = Intent(requireContext(), ConfirmationActivity::class.java)
            startActivity(intent)
        }

        binding.more.setOnClickListener {
            setImageList()
            adapter = GridViewAdapter(requireContext(), list)
            gridView.adapter = adapter
        }

        return root

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun saveInfo(index: Int?, photo: String) {
        val sharedPreferences = requireContext().getSharedPreferences(prefKey, MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        if (index != null) {
            editor.putInt(indexKey, index)
        }
        editor.putString(photoKey, photo)
        editor.apply()
    }

    // resets list and adds new images

    fun setImageList() {

        imageSet = (imageSet + 1) % 3
        list.clear()

        when (imageSet) {
            0 -> {
                // row 1
                list.add(R.drawable.psm_anxious)
                list.add(R.drawable.psm_hiking3)
                list.add(R.drawable.psm_stressed_person3)
                list.add(R.drawable.psm_lonely)
                // row 2
                list.add(R.drawable.psm_dog_sleeping)
                list.add(R.drawable.psm_running4)
                list.add(R.drawable.psm_alarm_clock)
                list.add(R.drawable.psm_headache)
                // row 3
                list.add(R.drawable.psm_baby_sleeping)
                list.add(R.drawable.psm_puppy)
                list.add(R.drawable.psm_stressed_cat)
                list.add(R.drawable.psm_angry_face)
                // row 4
                list.add(R.drawable.psm_bar)
                list.add(R.drawable.psm_running3)
                list.add(R.drawable.psm_neutral_child)
                list.add(R.drawable.psm_headache2)
            }
            1 -> {
                // row 1
                list.add(R.drawable.psm_talking_on_phone2)
                list.add(R.drawable.psm_stressed_person)
                list.add(R.drawable.psm_stressed_person12)
                list.add(R.drawable.psm_lonely)
                // row 2
                list.add(R.drawable.psm_gambling4)
                list.add(R.drawable.psm_clutter3)
                list.add(R.drawable.psm_reading_in_bed2)
                list.add(R.drawable.psm_stressed_person4)
                // row 3
                list.add(R.drawable.psm_lake3)
                list.add(R.drawable.psm_cat)
                list.add(R.drawable.psm_puppy3)
                list.add(R.drawable.psm_neutral_person2)
                // row 4
                list.add(R.drawable.psm_beach3)
                list.add(R.drawable.psm_peaceful_person)
                list.add(R.drawable.psm_alarm_clock2)
                list.add(R.drawable.psm_sticky_notes2)
            }
            2 -> {
                // row 1
                list.add(R.drawable.psm_mountains11)
                list.add(R.drawable.psm_wine3)
                list.add(R.drawable.psm_barbed_wire2)
                list.add(R.drawable.psm_clutter)
                // row 2
                list.add(R.drawable.psm_blue_drop)
                list.add(R.drawable.psm_to_do_list)
                list.add(R.drawable.psm_stressed_person7)
                list.add(R.drawable.psm_stressed_person6)
                // row 3
                list.add(R.drawable.psm_yoga4)
                list.add(R.drawable.psm_bird3)
                list.add(R.drawable.psm_stressed_person8)
                list.add(R.drawable.psm_exam4)
                // row 4
                list.add(R.drawable.psm_kettle)
                list.add(R.drawable.psm_lawn_chairs3)
                list.add(R.drawable.psm_to_do_list3)
                list.add(R.drawable.psm_work4)
            }


        }

    }

}