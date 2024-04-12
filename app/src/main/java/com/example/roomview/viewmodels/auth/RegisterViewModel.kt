package com.example.roomview.viewmodels.auth

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.roomview.model.User
import com.example.roomview.repository.Repository
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.util.UUID

class RegisterViewModel : ViewModel(

) {

    private val auth = FirebaseAuth.getInstance()
    private val repository: Repository = Repository()

    private var _message: MutableState<String> = mutableStateOf("")
    private var _success: MutableState<Boolean> = mutableStateOf(false)

    var message: MutableState<String> = _message
    var success: MutableState<Boolean> = _success

    suspend fun registerUser(
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        imageUri: Uri?,
        userType: Int
    ): AuthResult? {
        try {
            val data = auth.createUserWithEmailAndPassword(email, password).await()

            var imageUrl = ""
            if (imageUri != null) imageUrl = uploadImage(imageUri)

            val newUser = User(
                id = 0,
                uid = data?.user?.uid.toString(),
                firstName = firstName,
                lastName = lastName,
                email = email,
                imageUrl = imageUrl,
                userType = userType,
                dateAdded = LocalDateTime.now().toString()
            )

            repository.postUser(newUser)
            return data
        } catch (e: Exception) {
            _message.value = (e.localizedMessage)
            Log.d("DEBUG", e.localizedMessage)
            return null
        }
    }

    private suspend fun uploadImage(imageUri: Uri): String {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val fileRef =
            storageRef.child("images/profile/${UUID.randomUUID()}_${imageUri.lastPathSegment}")
        val uploadTask = fileRef.putFile(imageUri)
        val downloadUrl = try {
            uploadTask.await()
            fileRef.downloadUrl.await().toString()
        } catch (e: Exception) {
            _message.value = e.localizedMessage
            return ""
        }
        return downloadUrl
    }

    fun setMessage(value: String) {
        _message.value = value
    }
}
