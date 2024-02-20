package com.education.brainbeast.ui.education.ui.helpers

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.DimenRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class HorizontalMarginItemDecoration(
    context: Context,
    @DimenRes margin: Int,
    @DimenRes mleft: Int
) : ItemDecoration() {
    private val marginLeft: Int
    private val mMargin: Int

    init {
        mMargin = context.resources.getDimension(margin).toInt()
        marginLeft = context.resources.getDimension(mleft).toInt()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.right = mMargin
        outRect.left = marginLeft
    }
}
