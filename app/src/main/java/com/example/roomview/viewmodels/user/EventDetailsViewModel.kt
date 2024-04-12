package com.example.roomview.viewmodels.user

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.roomview.model.Event
import com.example.roomview.model.EventMember
import com.example.roomview.model.User
import com.example.roomview.repository.Repository
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Response


class EventDetailsViewModel : ViewModel() {

    private val repository: Repository = Repository()
    private val auth = FirebaseAuth.getInstance()

    private var allEventMemberList = mutableListOf<EventMember>()
    private var allUserList = mutableListOf<User>()
    private var joinedUserIdList = mutableListOf<Int>()
    private var currentUser: User? = null

    var event: Event? = null
    var joinedUserList = mutableListOf<User>()

    val joinStatus = mutableStateOf(false)

    suspend fun joinEvent(eventId: Int): Response<EventMember>? {

        getCurrentUser()

        if (currentUser != null) {
            val newEventMember = EventMember(0, eventId, currentUser!!.id)

            return repository.postEventMember(newEventMember)
        }

        return null
    }

    suspend fun leaveEvent(eventId: Int): Response<EventMember>? {
        getCurrentUser()
        var currentEventMember: EventMember? = null
        if (currentUser != null && allEventMemberList.isNotEmpty()) {
            for (eventMember in allEventMemberList) {
                if (eventMember.eventId == eventId && eventMember.userId == currentUser!!.id) {
                    currentEventMember = eventMember
                }
            }
            if (currentEventMember != null) {
                return repository.deleteEventMember(currentEventMember.id)
            }
        }
        return null
    }

    suspend fun getEvent(eventId: Int) {
        val response = repository.getEvent(eventId)

        if (response.isSuccessful) {
            response.body()?.let { event = it }
        }
    }

    suspend fun getEventMembers(eventId: Int) {

        val response = getAllEventMembers()

        if (response.isSuccessful) {
            for (eventMember in allEventMemberList) {
                if (eventMember.eventId == eventId) {
                    joinedUserIdList.add(eventMember.userId)
                }
            }
        }
    }

    suspend fun getJoinedUsers() {

        val response = repository.getUsers()

        if (response.isSuccessful) {
            allUserList.clear()
            response.body()?.let { allUserList.addAll(it) }

            for (user in allUserList) {
                if (joinedUserIdList.contains(user.id)) {
                    joinedUserList.add(user)
                }
            }

            getCurrentUser()

            if (currentUser != null && joinedUserIdList.isNotEmpty()) {
                joinStatus.value = joinedUserIdList.contains(currentUser!!.id)
            }

        }
    }

    private suspend fun getAllEventMembers(): Response<List<EventMember>> {
        val response = repository.getEventMembers()

        if (response.isSuccessful) {
            allEventMemberList.clear()
            response.body()?.let { allEventMemberList.addAll(it) }
        }
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