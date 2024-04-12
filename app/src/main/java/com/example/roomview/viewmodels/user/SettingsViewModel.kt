package com.example.roomview.viewmodels.user

import androidx.lifecycle.ViewModel
import com.example.roomview.model.User
import com.example.roomview.repository.Repository
import com.google.firebase.auth.FirebaseAuth

class SettingsViewModel: ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val repository: Repository = Repository()

    private val allUserList = mutableListOf<User>()
    var currentUser: User? = null

    fun signOut() {
        auth.signOut()
    }

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

}