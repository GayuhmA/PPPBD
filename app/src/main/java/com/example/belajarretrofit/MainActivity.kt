package com.example.belajarretrofit

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.belajarretrofit.databinding.ActivityMainBinding
import com.example.belajarretrofit.model.Users
import com.example.belajarretrofit.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val client = ApiClient.getInstance()
        val response = client.getAllUsers()
        val userList = ArrayList<String>()

        response.enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful && response.body() != null) {
                    val usersData = response.body()?.data

                    if (usersData != null) {
                        for (i in usersData) {
                            i.employeeName?.let { userList.add(it) }
                        }

                        val listAdapter = ArrayAdapter(
                            this@MainActivity,
                            android.R.layout.simple_list_item_1,
                            userList
                        )
                        binding.lvNama.adapter = listAdapter
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Gagal mengambil data: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<Users>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "Koneksi error: ${t.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}