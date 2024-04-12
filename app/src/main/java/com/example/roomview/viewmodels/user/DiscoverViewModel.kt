package com.example.roomview.viewmodels.user

import androidx.lifecycle.ViewModel
import com.example.roomview.model.Event
import com.example.roomview.repository.Repository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DiscoverViewModel : ViewModel() {

    private val repository = Repository()

    private val allEventList = mutableListOf<Event>()
    val eventsList = mutableListOf<Event>()

    private suspend fun getEvents() {
        val response = repository.getEvents()

        if (response.isSuccessful) {
            response.body()?.let { allEventList.addAll(it) }
        }
    }

    suspend fun getValidEvents() {
        getEvents()

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

        for (event in allEventList) {
            if (!event.isDeleted && inputFormat.parse(event.eventDate)!! >= Date()) {
                eventsList.add(event)
            }
        }
    }

}