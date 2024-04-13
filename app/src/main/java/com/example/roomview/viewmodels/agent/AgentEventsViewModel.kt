package com.example.roomview.viewmodels.agent

import androidx.lifecycle.ViewModel
import com.example.roomview.model.Event
import com.example.roomview.model.User
import com.example.roomview.repository.Repository
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AgentEventsViewModel() : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val repository: Repository = Repository()

    private var currentUser: User? = null
    private val allUserList = mutableListOf<User>()
    private val allEventList = mutableListOf<Event>()
    private val allOwnEventList = mutableListOf<Event>()

    val ownValidEventList = mutableListOf<Event>()
    val ownExpiredEventList = mutableListOf<Event>()

    private suspend fun getAllEvents() {
        val response = repository.getEvents()

        if (response.isSuccessful) {
            allEventList.clear()
            response.body()?.let { allEventList.addAll(it) }
        }
    }

    private suspend fun getOwnEvents() {
        getAllEvents()
        getAllUsers()

        if (currentUser != null) {
            for (event in allEventList) {
                if (event.agentId.toInt() == currentUser!!.id) {
                    allOwnEventList.add(event)
                }
            }
        }

        allUserList.clear()
        allEventList.clear()
    }

    suspend fun getValidEvents() {
        getOwnEvents()

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

        for (event in allOwnEventList) {
            if (!event.isDeleted && inputFormat.parse(event.eventDate)!! >= Date()) {
                ownValidEventList.add(event)
            } else {
                ownExpiredEventList.add(event)
            }
        }

        allOwnEventList.clear()
    }

    private suspend fun getAllUsers(): Response<List<User>> {
        val response = repository.getUsers()

        if (response.isSuccessful) {
            allUserList.clear()
            response.body()?.let { allUserList.addAll(it) }
        }

        getCurrentUser()

        return response
    }

    private fun getCurrentUser() {
        val uid = auth.currentUser?.uid

        for (user in allUserList) {
            if (user.uid == uid) {
                currentUser = user
            }
        }
    }


}
