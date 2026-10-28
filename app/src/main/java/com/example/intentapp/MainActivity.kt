package com.example.intentapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.intentapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setAccessibilityHeading(binding.txtTitle, true)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val adapterDisaster = DisasterAdapter(generateDummy()) { disaster ->
            Toast.makeText(
                this,
                getString(R.string.disaster_clicked, disaster.nameDisaster),
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.rvDisaster.adapter = adapterDisaster

        // Grid bergambar adalah hasil akhir modul. Pilihan tetap tersimpan saat rotasi.
        val isGrid = savedInstanceState?.getBoolean(STATE_IS_GRID, true) ?: true
        binding.layoutToggle.check(if (isGrid) R.id.btn_grid else R.id.btn_list)
        setDisasterLayout(isGrid)

        binding.layoutToggle.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                setDisasterLayout(checkedId == R.id.btn_grid)
            }
        }
    }

    private fun setDisasterLayout(isGrid: Boolean) {
        with(binding.rvDisaster) {
            val firstPosition = (layoutManager as? LinearLayoutManager)
                ?.findFirstVisibleItemPosition()
                ?.coerceAtLeast(0) ?: 0

            layoutManager = if (isGrid) {
                GridLayoutManager(this@MainActivity, 2)
            } else {
                LinearLayoutManager(this@MainActivity)
            }
            scrollToPosition(firstPosition)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(STATE_IS_GRID, binding.layoutToggle.checkedButtonId == R.id.btn_grid)
        super.onSaveInstanceState(outState)
    }

    fun generateDummy(): List<Disaster> = listOf(
        Disaster(nameDisaster = "Tsunami", disasterType = "Natural"),
        Disaster(nameDisaster = "Volcanic Eruption", disasterType = "Natural"),
        Disaster(nameDisaster = "Earthquake", disasterType = "Natural"),
        Disaster(nameDisaster = "Flood", disasterType = "Natural"),
        Disaster(nameDisaster = "Fire", disasterType = "Natural"),
        Disaster(
            nameDisaster = "Nuclear Accident",
            disasterType = "Man-made",
            imageResId = R.drawable.ic_disaster_man_made
        ),
        Disaster(
            nameDisaster = "Terrorist Attack",
            disasterType = "Man-made",
            imageResId = R.drawable.ic_disaster_man_made
        ),
        Disaster(
            nameDisaster = "War",
            disasterType = "Man-made",
            imageResId = R.drawable.ic_disaster_man_made
        )
    )

    private companion object {
        const val STATE_IS_GRID = "is_grid"
    }
}
