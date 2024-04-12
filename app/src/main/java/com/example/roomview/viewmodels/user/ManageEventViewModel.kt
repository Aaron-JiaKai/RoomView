package com.example.roomview.viewmodels.user

import androidx.lifecycle.ViewModel
import com.example.roomview.model.Event
import com.example.roomview.model.EventMember
import com.example.roomview.model.User
import com.example.roomview.repository.Repository
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ManageEventViewModel: ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val repository: Repository = Repository()

    private val allUserList = mutableListOf<User>()
    private val allEventList = mutableListOf<Event>()

    private val allEventMemberList = mutableListOf<EventMember>()
    private var currentUser: User? = null

    private val joinedEventIdList = mutableListOf<Int>()
    private val joinedEventList = mutableListOf<Event>()

    val joinedValidEventList = mutableListOf<Event>()
    val joinedExpiredEventList = mutableListOf<Event>()


    val otherEventList = mutableListOf<Event>()


    private suspend fun getAllEvents() {
        val response = repository.getEvents()

        if (response.isSuccessful) {
            allEventList.clear()
            response.body()?.let { allEventList.addAll(it) }
        }
    }

    private suspend fun getJoinedEvents() {
        getAllEvents()
        getAllEventMembers()
        getAllUsers()

        if (currentUser != null) {
            for (eventMember in allEventMemberList) {
                if (eventMember.userId == currentUser!!.id) {
                    joinedEventIdList.add(eventMember.eventId)
                }
            }
        }

        for (event in allEventList) {
            if (joinedEventIdList.contains(event.id)) {
                joinedEventList.add(event)
            } else {
                otherEventList.add(event)
            }
        }

        allEventMemberList.clear()
        allUserList.clear()
        joinedEventIdList.clear()
        allEventList.clear()
    }

    suspend fun getJoinedValidEvents() {
        getJoinedEvents()

        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

        for (event in joinedEventList) {
            if (!event.isDeleted && inputFormat.parse(event.eventDate)!! >= Date()) {
                joinedValidEventList.add(event)
            } else {
                joinedExpiredEventList.add(event)
            }
        }

        joinedEventList.clear()
    }

    private suspend fun getAllEventMembers(): Response<List<EventMember>> {
        val response = repository.getEventMembers()

        if (response.isSuccessful) {
            allEventMemberList.clear()
            response.body()?.let { allEventMemberList.addAll(it) }
        }

        return response
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