package com.education.brainbeast.ui.education.ui.menuprofile

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String = "",
    val age: Int = 0,
    var gender: String = "",
    val dob: String = "",
    val mobile: String = "",
    val email: String = "",
    var profileImageUrl: String? = null
)



