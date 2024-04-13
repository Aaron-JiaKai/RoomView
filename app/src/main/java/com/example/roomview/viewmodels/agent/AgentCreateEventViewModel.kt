package com.example.roomview.viewmodels.agent

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.roomview.model.Event
import com.example.roomview.model.User
import com.example.roomview.repository.Repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class AgentCreateEventViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val repository = Repository()

    private val allUserList = mutableListOf<User>()
    var currentUser: User? = null

    val addressList = mutableListOf<String>()
    private val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    suspend fun createEvent(
        title: String,
        description: String,
        houseType: Int,
        imageUri: Uri?,
        eventDate: String,
        eventLocation: String,
        eventLat: Double,
        eventLong: Double,
        context: Context
    ): Response<Event>? {
        if (title == "" || description == "" || eventDate == "" || eventLocation == "") {
            Toast.makeText(
                context,
                "Ensure all fields are filled in",
                Toast.LENGTH_SHORT
            ).show()
            return null
        }

        Log.d("TESTING", eventDate)

        var imageUrl = ""
        if (imageUri != null) imageUrl = uploadImage(imageUri)

        val newEvent = Event(
            0,
            title,
            description,
            houseType,
            imageUrl,
            currentUser!!.id.toString(),
            eventDate,
            eventLocation,
            eventLat,
            eventLong,
            inputFormat.format(Date()),
            false
        )

        return repository.postEvent(newEvent)
    }

    private suspend fun uploadImage(imageUri: Uri): String {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val fileRef = storageRef.child("images/${UUID.randomUUID()}_${imageUri.lastPathSegment}")
        val uploadTask = fileRef.putFile(imageUri)
        val downloadUrl = try {
            uploadTask.await()
            fileRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            return ""
        }
        return downloadUrl
    }

    private suspend fun getAllUsers() {
        val response = repository.getUsers()

        if (response.isSuccessful) {
            allUserList.clear()
            response.body()?.let { allUserList.addAll(it) }
        }
    }

    suspend fun getCurrentUser() {
        getAllUsers()

        val uid = auth.currentUser?.uid

        for (user in allUserList) {
            if (user.uid == uid) {
                currentUser = user
            }
        }

        allUserList.clear()
    }
}


