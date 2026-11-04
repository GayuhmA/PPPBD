package com.example.belajarretrofit.model

import com.google.gson.annotations.SerializedName

data class Users(
    @SerializedName("status")
    val status: String?,

    @SerializedName("data")
    val data: List<Data>?,

    @SerializedName("message")
    val message: String?
)