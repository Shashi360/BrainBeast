/*
 * Copyright (c) 2021. rogergcc
 */
package com.education.brainbeast.ui.education.ui.menucourses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.education.brainbeast.databinding.ItemCardBinding
import com.education.brainbeast.ui.education.ui.listeners.CoursesItemClickListener
import com.education.brainbeast.ui.education.ui.model.CourseCard


class CourseRecyclerAdapter(
    mData: List<CourseCard>,
    listener: CoursesItemClickListener
) : RecyclerView.Adapter<CourseRecyclerAdapter.ViewHolder?>() {
    private val mData: List<CourseCard>
    private val coursesItemClickListener: CoursesItemClickListener

    init {
        this.mData = mData
        coursesItemClickListener = listener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(viewGroup.context)
        val itemCardBinding: ItemCardBinding =
            ItemCardBinding.inflate(layoutInflater, viewGroup, false)
        return ViewHolder(itemCardBinding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val pos: Int = viewHolder.adapterPosition
        //Set ViewTag
        viewHolder.itemView.tag = pos
        viewHolder.setBind(mData[position])

        //2nd intent card only bottom margin in xml  and only top margin in adapter- it works
        viewHolder.itemView.setOnClickListener { _: View? ->
            coursesItemClickListener.onDashboardCourseClick(
                mData[position], viewHolder.itemCardBinding.cardViewImage
            )
        }
    }

    override fun getItemId(position: Int): Long {
        val courseCard: CourseCard = mData[position]
        return courseCard.id.toLong()
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    class ViewHolder(cardBinding: ItemCardBinding) :
        RecyclerView.ViewHolder(cardBinding.root) {
        var itemCardBinding: ItemCardBinding

        init {
            itemCardBinding = cardBinding

            //this.itemRecyclerMealBinding.
        }

        fun setBind(courseCard: CourseCard) {
            itemCardBinding.cardViewImage.setImageResource(courseCard.imageCourse)
            itemCardBinding.stagItemCourse.text = courseCard.courseTitle
            itemCardBinding.stagItemQuantityCourse.text = courseCard.quantityCourses
//            itemCardBinding.cardViewImage.setBackgroundColor(
//                itemView.context.resources.getColor(R.color.color1)
//            )
        }
    }
}
