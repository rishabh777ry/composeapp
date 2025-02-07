package com.example.composeapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.data.dao.UserDAO
import com.example.composeapp.data.entity.User
import com.example.composeapp.repository.UserRepository
import kotlinx.coroutines.launch
import com.example.composeapp.data.database.UserDatabase

class UserViewModel(application: Application) : AndroidViewModel(application) {
    var userID = 0

    // Use the UserDatabase singleton to get the DAO
    private val userDao: UserDAO = UserDatabase.getDatabase(application).userDao()

    private val userRepository = UserRepository(userDao)


      fun insertUser(user: User) {
        viewModelScope.launch {
            val userId = userRepository.insertUser(user)
            userID = user.id
        }
    }
    fun getUser(user: Int = userID ) {
        viewModelScope.launch {
            userRepository.getUserById(user)
        }
    }

}
