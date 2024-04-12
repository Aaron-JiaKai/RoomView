package com.example.roomview.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("userId")
    var id: Int,
    var uid: String,
    var firstName: String,
    var lastName: String,
    var email: String,
    var userType: Int,
    @SerializedName("profileImage")
    var imageUrl: String,
    var dateAdded: String
)
