package com.example.roomview.repository

import com.example.roomview.api.RetrofitInstance
import com.example.roomview.model.Event
import com.example.roomview.model.EventMember
import com.example.roomview.model.Timeslot
import com.example.roomview.model.User
import retrofit2.Response

class Repository {

    // Events
    suspend fun getEvents(): Response<List<Event>> {
        return RetrofitInstance.api.getEvents()
    }

    suspend fun getEvent(eventId: Int): Response<Event> {
        return RetrofitInstance.api.getEvent(eventId)
    }

    suspend fun postEvent(event: Event): Response<Event> {
        return RetrofitInstance.api.postEvent(event)
    }

    suspend fun putEvent(eventId: Int, event: Event): Response<Event> {
        return RetrofitInstance.api.putEvent(eventId, event)
    }

    // Users
    suspend fun getUsers(): Response<List<User>> {
        return RetrofitInstance.api.getUsers()
    }

    suspend fun getUser(userId: Int): Response<User> {
        return RetrofitInstance.api.getUser(userId)
    }

    suspend fun postUser(user: User): Response<User> {
        return RetrofitInstance.api.postUser(user)
    }

    suspend fun putUser(userId: Int, user: User): Response<User> {
        return RetrofitInstance.api.putUser(userId, user)
    }

    // Timeslots
    suspend fun getTimeslots(): Response<List<Timeslot>> {
        return RetrofitInstance.api.getTimeslots()
    }

    suspend fun getTimeslot(timeslotId: Int): Response<Timeslot> {
        return RetrofitInstance.api.getTimeslot(timeslotId)
    }

    suspend fun postTimeslot(timeslot: Timeslot): Response<Timeslot> {
        return RetrofitInstance.api.postTimeslot(timeslot)
    }

    suspend fun putTimeslot(timeslotId: Int, timeslot: Timeslot): Response<Timeslot> {
        return RetrofitInstance.api.putTimeslot(timeslotId, timeslot)
    }

    suspend fun deleteTimeslot(timeslotId: Int): Response<Timeslot> {
        return RetrofitInstance.api.deleteTimeslot(timeslotId)
    }

    // Event Members
    suspend fun getEventMembers(): Response<List<EventMember>> {
        return RetrofitInstance.api.getEventMembers()
    }

    suspend fun getEventMember(eventMemberId: Int): Response<EventMember> {
        return RetrofitInstance.api.getEventMember(eventMemberId)
    }

    suspend fun postEventMember(eventMember: EventMember): Response<EventMember> {
        return RetrofitInstance.api.postEventMember(eventMember)
    }

    suspend fun putEventMember(eventMemberId: Int, eventMember: EventMember): Response<EventMember> {
        return RetrofitInstance.api.putEventMember(eventMemberId, eventMember)
    }

    suspend fun deleteEventMember(eventMemberId: Int): Response<EventMember> {
        return RetrofitInstance.api.deleteEventMember(eventMemberId)
    }
}