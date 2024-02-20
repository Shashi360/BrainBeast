package com.education.brainbeast.ui.education.ui.menusearch

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.education.brainbeast.databinding.ItemPagerCardBinding
import com.education.brainbeast.ui.education.ui.listeners.MatchCourseClickListener
import com.education.brainbeast.ui.education.ui.model.MatchCourse

class CourseTopicsViewPager(
    mCoursesList: List<MatchCourse>,
    context: Context?,
    listener: MatchCourseClickListener
) : RecyclerView.Adapter<CourseTopicsViewPager.ViewHolder?>() {
    private val mCoursesList: List<MatchCourse>
    private val matchCourseClickListener: MatchCourseClickListener

    init {
        LayoutInflater.from(context)
        this.mCoursesList = mCoursesList
        matchCourseClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val itemPagerCardBinding: ItemPagerCardBinding =
            ItemPagerCardBinding.inflate(inflater, parent, false)
        return ViewHolder(itemPagerCardBinding)
    }

    override fun getItemCount(): Int {
      return mCoursesList.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setBind(mCoursesList[position])
        holder.binding.cardViewCourse.setOnClickListener { _ ->
            matchCourseClickListener.onScrollPagerItemClick(
                mCoursesList[holder.adapterPosition], holder.binding.image
            )
        }
    }

    class ViewHolder(binding: ItemPagerCardBinding) : RecyclerView.ViewHolder(binding.root) {
        var binding: ItemPagerCardBinding

        init {
            this.binding = binding
        }

        fun setBind(matchCourse: MatchCourse) {
            binding.tvTitulo.text = matchCourse.name
            binding.tvCantidadCursos.text = matchCourse.numberOfCourses
            Glide.with(itemView.context)
                .load(matchCourse.imageResource)
                //.transform(new RoundedCorners(40))
                .transform(CenterCrop())
                .into(binding.image)
        }
    }
}
