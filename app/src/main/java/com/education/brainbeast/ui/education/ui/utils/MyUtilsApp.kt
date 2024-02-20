/*
 * Copyright (c) 2021. rogergcc
 */
package com.education.brainbeast.ui.education.ui.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

object MyUtilsApp {
    fun showToast(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun showLog(TAG: String?, message: String) {
        Log.d(TAG, "showLog: $message")
    }
}
