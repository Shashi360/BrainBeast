/*
 * Copyright (c) 2020. rogergcc
 */
package com.education.brainbeast.ui.education.ui.listeners

import android.widget.ImageView
import com.education.brainbeast.ui.education.ui.model.CourseCard

interface CoursesItemClickListener {
    fun onDashboardCourseClick(
        courseCard: CourseCard?,
        imageView: ImageView?
    ) // Should use imageview to make the shared animation between the two activity
}
