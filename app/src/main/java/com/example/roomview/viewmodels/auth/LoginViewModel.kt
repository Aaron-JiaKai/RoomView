package com.example.roomview.viewmodels.auth

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.roomview.model.User
import com.example.roomview.repository.Repository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


class LoginViewModel: ViewModel() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val repository = Repository()
    private val _message = mutableStateOf("")
    private val allUserList = mutableListOf<User>()

    val message: MutableState<String> = _message;

    suspend fun login(email: String, password: String, selectedIndex: Int): AuthResult? {

        var currentUser: User? = null

        if (email == "") {
            _message.value = "Email is empty"
            return null
        }

        for (user in allUserList) {
            if (email == user.email) {
                currentUser = user
                break
            }
        }

        if (currentUser == null) {
            _message.value = "Cannot get account information right now. Please try again later."
            return null
        }

        if (currentUser.userType != 1 && selectedIndex == 1) {
            _message.value = "This account does not belong to an agent"
            return null
        }

        return try {
            val data = Firebase.auth
                .signInWithEmailAndPassword(email, password)
                .await()
            data
        } catch (e: Exception) {
            _message.value = e.localizedMessage ?: ""
            return null
        }
    }

    suspend fun getAllUsers() {
        val response = repository.getUsers()

        if (response.isSuccessful) {
            response.body()?.let { allUserList.addAll(it) }
        }
    }

}