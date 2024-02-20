package com.education.brainbeast.ui.education.ui.menuprofile

import androidx.lifecycle.LiveData

class UserRepository(private val userDao: UserDao) {

    fun getUsersDetails(): LiveData<User> {
        return userDao.getUsersDetails()
    }

    suspend fun insertUser(user: User) {
        userDao.insert(user)
    }

    suspend fun updateUser(user: User) {
        userDao.update(user)
    }

    suspend fun deleteUser(user: User) {
        userDao.delete(user)
    }
}
