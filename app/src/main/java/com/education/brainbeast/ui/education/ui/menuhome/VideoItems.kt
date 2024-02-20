package com.education.brainbeast.ui.education.ui.menuhome

sealed class VideoItem {
    data class HomeCoursePopularVideoID(val videoId: String) : VideoItem()
    data class HomeCourseTutorialsVideoID(val videoId: String) : VideoItem()
}
