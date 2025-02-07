package com.example.composeapp.repository

import com.example.composeapp.data.entity.User
import kotlinx.coroutines.flow.Flow

import com.example.composeapp.data.dao.UserDAO

class UserRepository(private val userDao: UserDAO) {

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }


    suspend fun getUserById(id: Int): User? {
        return userDao.getUserById(id)
    }
}

