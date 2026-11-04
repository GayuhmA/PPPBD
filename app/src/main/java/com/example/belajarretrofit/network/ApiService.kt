package com.example.belajarretrofit.network

import com.example.belajarretrofit.model.Users
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("employees")
    fun getAllUsers(): Call<Users>
}