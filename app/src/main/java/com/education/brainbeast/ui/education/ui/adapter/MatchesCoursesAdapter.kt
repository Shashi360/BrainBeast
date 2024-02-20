/*
 * Copyright (c) 2020. rogergcc
 */
package com.education.brainbeast.ui.education.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.education.brainbeast.databinding.ItemShopCardBinding
import com.education.brainbeast.ui.education.ui.listeners.MatchCourseClickListener
import com.education.brainbeast.ui.education.ui.model.MatchCourse

class MatchesCoursesAdapter(mData: List<MatchCourse>, listener: MatchCourseClickListener) :
    RecyclerView.Adapter<MatchesCoursesAdapter.ViewHolder?>() {
    private val mData: List<MatchCourse>
    private val matchCourseClickListener: MatchCourseClickListener

    init {
        this.mData = mData
        matchCourseClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val itemCardBinding: ItemShopCardBinding =
            ItemShopCardBinding.inflate(inflater, parent, false)
        return ViewHolder(itemCardBinding)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setBind(mData[position])
        holder.itemView.setOnClickListener { _: View? ->
            matchCourseClickListener.onScrollPagerItemClick(
                mData[holder.adapterPosition], holder.itemCardBinding.image
            )
        }
    }

    class ViewHolder(cardBinding: ItemShopCardBinding) :
        RecyclerView.ViewHolder(cardBinding.root) {
        var itemCardBinding: ItemShopCardBinding

        init {
            itemCardBinding = cardBinding
        }

        fun setBind(matchCourse: MatchCourse) {
            itemCardBinding.tvTitulo.text = matchCourse.name
            itemCardBinding.tvCantidadCursos.text = matchCourse.numberOfCourses
            Glide.with(itemView.context)
                .load(matchCourse.imageResource)
                .into(itemCardBinding.image)
        }
    }
}
