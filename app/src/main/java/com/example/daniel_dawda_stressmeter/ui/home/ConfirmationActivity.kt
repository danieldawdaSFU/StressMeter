package com.example.daniel_dawda_stressmeter.ui.home

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import androidx.appcompat.app.AppCompatActivity
import com.example.daniel_dawda_stressmeter.databinding.ActivityConfirmationBinding
import com.example.daniel_dawda_stressmeter.ui.home.HomeFragment.StressData
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ConfirmationActivity: AppCompatActivity() {

    val prefKey = "key"
    val indexKey = "ind"
    val photoKey = "photo"
    var stress: Int? = null
    var photoPrefString: String? = null

    private lateinit var binding: ActivityConfirmationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityConfirmationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadInfo()

        binding.confirm.setOnClickListener {
            val file = File(filesDir, "stress_data.csv")
            FileOutputStream(file, true).apply { writeCsv(listOf(StressData(System.currentTimeMillis(), stress))) }
            finishAffinity()
        }

        binding.cancel.setOnClickListener {
            finish()
        }
    }

    fun loadInfo() {
        val sharedPreferences = getSharedPreferences(prefKey, MODE_PRIVATE)

        stress = sharedPreferences.getInt(indexKey, 0)

        photoPrefString = sharedPreferences.getString(photoKey, null)

        // for photo
        // adapted from https://stackoverflow.com/questions/45733975/string-to-bitmap-in-kotlin
        if (photoPrefString != null) {
            val imageBytes = Base64.decode(photoPrefString, 0)
            val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            binding.confImage.setImageBitmap(image)
        }

    }

    // csv write adapted from https://www.baeldung.com/kotlin/csv-files
    fun OutputStream.writeCsv(sData: List<StressData>) {
        val writer = bufferedWriter()

        sData.forEach {
            writer.write("${it.timestamp},${it.stressLevel}")
            writer.newLine()
        }
        writer.flush()
    }
}