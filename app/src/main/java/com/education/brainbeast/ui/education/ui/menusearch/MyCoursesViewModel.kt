/*
 * Copyright (c) 2021. rogergcc
 */
package com.education.brainbeast.ui.education.ui.menusearch

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.education.brainbeast.ui.education.ui.model.MatchCourse


internal class MyCoursesViewModel(private val repository: MyQrListRepository) : ViewModel() {
    private var mLiveDataQrList: MutableLiveData<List<MatchCourse?>?> = MutableLiveData()

//    val dataQrListVM: Unit
//        get() {
//            repository.getData(mLiveDataQrList)
//        }
}
