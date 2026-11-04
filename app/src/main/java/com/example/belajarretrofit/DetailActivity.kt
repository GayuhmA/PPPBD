package com.example.belajarretrofit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.belajarretrofit.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getIntExtra("EXTRA_ID", 0)
        val name = intent.getStringExtra("EXTRA_NAME") ?: "-"
        val age = intent.getIntExtra("EXTRA_AGE", 0)
        val salary = intent.getIntExtra("EXTRA_SALARY", 0)

        binding.tvId.text = "ID: $id"
        binding.tvName.text = "Name: $name"
        binding.tvAge.text = "Age: $age"
        binding.tvSalary.text = "Salary: $$salary"
    }
}