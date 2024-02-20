/*
 * Copyright (c) 2020. rogergcc
 */
package com.education.brainbeast.ui.education.ui.model

class MatchCourse(
    val id: Int,
    val name: String,
    val numberOfCourses: String,
    val imageResource: Int
) {

    override fun toString(): String {
        return "MatchCourse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", numberOfCourses='" + numberOfCourses + '\'' +
                ", imageResource=" + imageResource +
                '}'
    }
}
