package com.example.belajarretrofit

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.belajarretrofit.databinding.ActivityMainBinding
import com.example.belajarretrofit.model.Data
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

        val rawUsersList = ArrayList<Data>()
        val userNamesList = ArrayList<String>()

        response.enqueue(object : Callback<Users> {
            override fun onResponse(call: Call<Users>, response: Response<Users>) {
                if (response.isSuccessful && response.body() != null) {
                    val usersData = response.body()?.data ?: emptyList()

                    rawUsersList.clear()
                    userNamesList.clear()

                    for (user in usersData) {
                        rawUsersList.add(user)
                        userNamesList.add(user.employeeName ?: "Tanpa Nama")
                    }

                    val listAdapter = ArrayAdapter(
                        this@MainActivity,
                        android.R.layout.simple_list_item_1,
                        userNamesList
                    )
                    binding.lvNama.adapter = listAdapter

                    binding.lvNama.setOnItemClickListener { _, _, position, _ ->
                        val selectedUser = rawUsersList[position]

                        val intent = Intent(this@MainActivity, DetailActivity::class.java).apply {
                            putExtra("EXTRA_ID", selectedUser.id ?: 0)
                            putExtra("EXTRA_NAME", selectedUser.employeeName ?: "-")
                            putExtra("EXTRA_AGE", selectedUser.employeeAge ?: 0)
                            putExtra("EXTRA_SALARY", selectedUser.employeeSalary ?: 0)
                        }
                        startActivity(intent)
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
                Toast.makeText(this@MainActivity, "Koneksi Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}