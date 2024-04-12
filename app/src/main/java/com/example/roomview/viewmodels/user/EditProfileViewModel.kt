package com.example.roomview.viewmodels.user

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.roomview.model.User
import com.example.roomview.repository.Repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.util.UUID

class EditProfileViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val repository: Repository = Repository()

    private val allUserList = mutableListOf<User>()
    var currentUser: User? = null

    fun getCurrentUser() {
        val uid = auth.currentUser?.uid

        for (user in allUserList) {
            if (user.uid == uid) {
                currentUser = user
            }
        }
    }

    suspend fun getUsers() {
        val response = repository.getUsers()

        if (response.isSuccessful) {
            allUserList.clear()
            response.body()?.let { allUserList.addAll(it) }
        }
    }

    fun clearList() {
        allUserList.clear()
        currentUser = null
    }

    suspend fun updateProfile(
        firstName: String, lastName: String, imageUri: Uri?
    ) {
        if (currentUser == null) return

        var updatedUser: User = currentUser!!

        if (imageUri != null) {
            updatedUser.imageUrl = uploadImage(imageUri) ?: ""
        }

        if (!firstName.isBlank()) {
            updatedUser.firstName = firstName
        }

        if (!lastName.isBlank()) {
            updatedUser.lastName = lastName
        }

        putUser(updatedUser)
    }

    private suspend fun putUser(newUser: User) {

        val id = currentUser!!.id

        val response = repository.putUser(id, newUser)

        Log.d("DEBUG", response.code().toString())
    }

    private suspend fun uploadImage(imageUri: Uri): String? {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val fileRef = storageRef.child("images/${UUID.randomUUID()}_${imageUri.lastPathSegment}")
        val uploadTask = fileRef.putFile(imageUri)
        val downloadUrl = try {
            uploadTask.await()
            fileRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            null
        }
        return downloadUrl
    }

}