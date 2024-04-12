package com.example.roomview.model

import com.google.gson.annotations.SerializedName

data class Timeslot(
    @SerializedName("timeslotId")
    var id: Int,
    var eventId: Int,
    var startTime: String,
    var endTime: String,
    var userId: Int?,
    var dateAdded: String
)
