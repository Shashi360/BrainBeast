/*
 * Copyright (c) 2021. rogergcc
 */
package com.education.brainbeast.ui.education.ui.menusearch

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.education.brainbeast.ui.education.ui.model.MatchCourse
import com.education.brainbeast.ui.education.ui.model.MyMatchesCourses

internal class MyQrListRepository {
    private val getInfo: MediatorLiveData<MatchCourse> = MediatorLiveData<MatchCourse>()
    private var isLoadingGetList: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var isLoadingRepo: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var liveData: MutableLiveData<List<MatchCourse>>? = null
    fun getData(liveData: MutableLiveData<List<MatchCourse?>?>) {
        isLoadingGetList.value = true
        try {
            isLoadingGetList.value = false
            val myQrItem: List<MatchCourse> = MyMatchesCourses.get().data
            liveData.postValue(myQrItem)
        } catch (ex: Exception) {
            isLoadingGetList.setValue(false)
        }
    }

    companion object {
        private const val TAG = "MyQrListRepository"
    }
}
