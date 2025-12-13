//package com.example.e_commercemobapp.viewmodel
//
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.example.e_commercemobapp.model.User
//import com.example.e_commercemobapp.repository.AuthRepository
//
//class AuthViewModel : ViewModel() {
//
//    private val repo = AuthRepository()
//
//    val authStatus = MutableLiveData<Boolean>()
//    val errorMessage = MutableLiveData<String?>()
//
//    fun login(email: String, password: String) {
//        repo.loginUser(email, password) { success, error ->
//            authStatus.postValue(success)
//            errorMessage.postValue(error)
//        }
//    }
//
//    fun register(email: String, password: String, user: User) {
//        repo.registerUser(email, password, user) { success, error ->
//            authStatus.postValue(success)
//            errorMessage.postValue(error)
//        }
//    }
//
//    fun resetPassword(email: String) {
//        repo.resetPassword(email) { success, error ->
//            authStatus.postValue(success)
//            errorMessage.postValue(error)
//        }
//    }
//}
package com.example.e_commercemobapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.e_commercemobapp.model.User
import com.example.e_commercemobapp.repository.AuthRepository

class AuthViewModel : ViewModel() {

    private val repo = AuthRepository()
    val authStatus = MutableLiveData<Boolean?>()
    val errorMessage = MutableLiveData<String?>()

    fun login(email: String, password: String) {
        repo.loginUser(email, password) { s, e ->
            authStatus.postValue(s)
            errorMessage.postValue(e)
        }
    }

    fun register(email: String, password: String, user: User) {
        repo.registerUser(email, password, user) { s, e ->
            authStatus.postValue(s)
            errorMessage.postValue(e)
        }
    }

    fun resetPassword(email: String) {
        repo.resetPassword(email) { s, e ->
            authStatus.postValue(s)
            errorMessage.postValue(e)
        }
    }

    fun clearStatus() {
        authStatus.value = null
    }
}
