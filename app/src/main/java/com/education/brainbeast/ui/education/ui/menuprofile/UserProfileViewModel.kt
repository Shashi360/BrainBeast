package com.education.brainbeast.ui.education.ui.menuprofile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository: UserRepository

    val userData: LiveData<User>

    init {
        val userDao = UserRoomDatabase.getDatabase(application).userDao()
        userRepository = UserRepository(userDao)
        userData = userRepository.getUsersDetails()
    }

    fun saveUserData(user: User) {
        viewModelScope.launch {
            userRepository.insertUser(user)
        }
    }
}

