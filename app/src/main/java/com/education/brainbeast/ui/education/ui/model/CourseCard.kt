/*
 * Copyright (c) 2020. rogergcc
 */
package com.education.brainbeast.ui.education.ui.model

class CourseCard(
    var id: Int,
    var imageCourse: Int,
    var courseTitle: String,
    var quantityCourses: String
) {
    var urlCourse: String? = null

    override fun equals(other: Any?): Boolean {
        // This is unavoidable, since equals() must accept an Object and not something more derived
        if (other is CourseCard) {
            // Note that I use equals() here too, otherwise, again, we will check for referential equality.
            // Using equals() here allows the Model class to implement it's own version of equality, rather than
            // us always checking for referential equality.
            return other.id == id
        }
        return false
    }
}
