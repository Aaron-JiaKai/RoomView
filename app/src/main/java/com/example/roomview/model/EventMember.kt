package com.example.roomview.model

import com.google.gson.annotations.SerializedName

data class EventMember(
    @SerializedName("eventMemberId")
    var id: Int,
    var eventId: Int,
    var userId: Int
)
