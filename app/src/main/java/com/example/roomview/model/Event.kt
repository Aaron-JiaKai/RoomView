package com.example.roomview.model

import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("eventId")
    var id: Int,
    var title: String,
    var description: String,
    var houseType: Int,
    var imageUrl: String,
    var agentId: String,
    var eventDate: String,
    var eventLocation: String,
    var eventLat: Double,
    var eventLong: Double,
    var dateAdded: String,
    var isDeleted: Boolean
)