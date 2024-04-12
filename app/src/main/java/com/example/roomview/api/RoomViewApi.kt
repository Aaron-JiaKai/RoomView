package com.example.roomview.api

import com.example.roomview.model.Event
import com.example.roomview.model.EventMember
import com.example.roomview.model.Timeslot
import com.example.roomview.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RoomViewApi {

    // Events
    @GET("events")
    suspend fun getEvents(): Response<List<Event>>

    @GET("events/{eventId}")
    suspend fun getEvent(
        @Path("eventId") eventId: Int
    ): Response<Event>

    @POST("events")
    suspend fun postEvent(
        @Body event: Event
    ): Response<Event>

    @PUT("events/{eventId}")
    suspend fun putEvent(
        @Path("eventId") eventId: Int,
        @Body event: Event
    ): Response<Event>

    // Users
    @GET("users")
    suspend fun getUsers(): Response<List<User>>

    @GET("users/{userId}")
    suspend fun getUser(
        @Path("userId") userId: Int
    ): Response<User>

    @POST("users")
    suspend fun postUser(
        @Body user: User
    ): Response<User>

    @PUT("users/{userId}")
    suspend fun putUser(
        @Path("userId") userId: Int,
        @Body user: User
    ): Response<User>

    // Timeslots
    @GET("timeslots")
    suspend fun getTimeslots(): Response<List<Timeslot>>

    @GET("timeslots/{timeslotId}")
    suspend fun getTimeslot(
        @Path("timeslotId") timeslotId: Int
    ): Response<Timeslot>

    @POST("timeslots")
    suspend fun postTimeslot(
        @Body timeslot: Timeslot
    ): Response<Timeslot>

    @PUT("timeslots/{timeslotId}")
    suspend fun putTimeslot(
        @Path("timeslotId") timeslotId: Int,
        @Body timeslot : Timeslot
    ): Response<Timeslot>

    @DELETE("timeslots/{timeslotId}")
    suspend fun deleteTimeslot(
        @Path("timeslotId") timeslotId: Int
    ): Response<Timeslot>

    // EventMembers
    @GET("eventMembers")
    suspend fun getEventMembers(): Response<List<EventMember>>

    @GET("eventMembers/{eventMemberId}")
    suspend fun getEventMember(
        @Path("eventMemberId") eventMemberId: Int
    ): Response<EventMember>

    @POST("eventMembers")
    suspend fun postEventMember(
        @Body eventMember: EventMember
    ): Response<EventMember>

    @PUT("eventMembers/{eventMemberId}")
    suspend fun putEventMember(
        @Path("eventMemberId") eventMemberId: Int,
        @Body eventMember : EventMember
    ): Response<EventMember>

    @DELETE("eventMembers/{eventMemberId}")
    suspend fun deleteEventMember(
        @Path("eventMemberId") eventMemberId: Int
    ): Response<EventMember>
}