package com.example.e_commercemobapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.e_commercemobapp.model.User
import com.example.e_commercemobapp.repository.AuthRepository
import com.example.e_commercemobapp.util.Event

class AuthViewModel : ViewModel() {

    private val repo = AuthRepository()

    val authStatus = MutableLiveData<Event<Boolean>>()
    val errorMessage = MutableLiveData<Event<String>>()

    fun register(email: String, password: String, user: User) {
        repo.register(email, password, user) { success, error ->
            authStatus.postValue(Event(success))
            error?.let { errorMessage.postValue(Event(it)) }
        }
    }

    fun login(email: String, password: String) {
        repo.login(email, password) { success, error ->
            authStatus.postValue(Event(success))
            error?.let { errorMessage.postValue(Event(it)) }
        }
    }

    fun resetPassword(email: String) {
        repo.resetPassword(email) { success, error ->
            authStatus.postValue(Event(success))
            error?.let { errorMessage.postValue(Event(it)) }
        }
    }
}
