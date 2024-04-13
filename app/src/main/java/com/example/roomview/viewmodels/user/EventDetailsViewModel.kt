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
    private var agentId = ""

    var currentUser: User? = null
    var currentAgent = mutableStateOf<User?>(null)

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

    private suspend fun getEvent(eventId: Int): Response<Event> {
        val response = repository.getEvent(eventId)

        if (response.isSuccessful) {
            response.body()?.let { event = it }
        }

        return response
    }

    private suspend fun getEventMembers(eventId: Int) {

        val response = getAllEventMembers()

        if (response.isSuccessful) {
            joinedUserIdList.clear()
            for (eventMember in allEventMemberList) {
                if (eventMember.eventId == eventId) {
                    joinedUserIdList.add(eventMember.userId)
                }
            }
        }
    }

    suspend fun getJoinedUsers(eventId: Int) {
        val eventResponse = getEvent(eventId)
        getCurrentUser()
        getEventMembers(eventId)

        joinedUserList.clear()
        for (user in allUserList) {
            if (joinedUserIdList.contains(user.id)) {
                joinedUserList.add(user)
            }
        }

        if (currentUser != null && joinedUserIdList.isNotEmpty()) {
            joinStatus.value = joinedUserIdList.contains(currentUser!!.id)
        }

    }


    private suspend fun getAllUsers() {
        val response = repository.getUsers()

        if (response.isSuccessful) {
            allUserList.clear()
            response.body()?.let { allUserList.addAll(it) }
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

    private suspend fun getCurrentUser() {

        getAllUsers()

        val uid = auth.currentUser?.uid

        for (user in allUserList) {
            if (user.uid == uid) {
                currentUser = user
            }
        }
    }

    suspend fun getAgent(eventId: Int) {

        val eventResponse = repository.getEvent(eventId)

        if (eventResponse.isSuccessful) {
            agentId = eventResponse.body()!!.agentId
        }

        val userResponse = repository.getUsers()

        if (userResponse.isSuccessful) {
            val userList = userResponse.body()

            if (userList != null) {
                for (user in userList) {
                    if (user.id == agentId.toInt()) {
                        currentAgent.value = user
                    }
                }
            }
        }
    }

}