package com.education.brainbeast.ui.education.ui.room.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CourseEntityDataBase(
    @PrimaryKey val courseID: Int,
    @ColumnInfo(name = "courseName") val name: String?,
    @ColumnInfo(name = "courseID") val email: String?,
    @ColumnInfo(name = "coursePrice") val avatar: String?
)