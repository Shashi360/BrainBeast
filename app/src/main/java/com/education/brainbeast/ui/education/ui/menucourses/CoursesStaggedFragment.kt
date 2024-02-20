/*
 * Copyright (c) 2021. rogergcc
 */
package com.education.brainbeast.ui.education.ui.menucourses

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.education.brainbeast.R
import com.education.brainbeast.databinding.FragmentCoursesStaggedBinding
import com.education.brainbeast.ui.education.ui.helpers.GridSpacingItemDecoration
import com.education.brainbeast.ui.education.ui.listeners.CoursesItemClickListener
import com.education.brainbeast.ui.education.ui.model.CourseCard

class CoursesStaggedFragment : Fragment(), CoursesItemClickListener {
    private lateinit var binding: FragmentCoursesStaggedBinding
    private var mContext: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCoursesStaggedBinding.inflate(layoutInflater)
        mContext = this.context
        val view: View = binding.root
        binding.edtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                //For this example only use search option
                //U can use a other view with activityResult
                performSearch()
                Toast.makeText(
                    mContext,
                    "You are searching for " + binding.edtSearch.text.toString().trim(),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnEditorActionListener true
            }
            false
        }
        val layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        binding.rvCourses.layoutManager = layoutManager
        binding.rvCourses.clipToPadding = false
        binding.rvCourses.setHasFixedSize(true)

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.horizontal_card)
        binding.rvCourses.addItemDecoration(GridSpacingItemDecoration(2, spacingInPixels, true, 0))
        val courseCards: ArrayList<CourseCard> = ArrayList()
        courseCards.add(
            CourseCard(
                1,
                R.drawable.course_design_thinking,
                "Design Thinking",
                "19 courses"
            )
        )
        courseCards.add(
            CourseCard(
                2,
                R.drawable.course_design_coding,
                "Software Development",
                "14 courses"
            )
        )
        courseCards.add(
            CourseCard(
                3,
                R.drawable.course_design_marketing,
                "Marketing",
                "24 courses"
            )
        )
        courseCards.add(
            CourseCard(
                4,
                R.drawable.course_design_securityexpert,
                "Security Expert",
                "18 courses"
            )
        )
        courseCards.add(
            CourseCard(
                5,
                R.drawable.course_design_whatisthisshit,
                "Locations",
                "21 courses"
            )
        )
        courseCards.add(CourseCard(6, R.drawable.course_coding, "Big Data", "10 courses"))
        val adapter = CourseRecyclerAdapter(courseCards, this)

//        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.card_margin);
//        binding.rvCourses.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        binding.rvCourses.adapter = adapter
        return view
    }

    private fun performSearch() {
        binding.edtSearch.clearFocus()
        val `in` = mContext!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        `in`.hideSoftInputFromWindow(binding.edtSearch.windowToken, 0)
        //...perform search
    }

    override fun onDashboardCourseClick(courseCard: CourseCard?, imageView: ImageView?) {
        if (courseCard != null) {
            Toast.makeText(mContext, courseCard.courseTitle, Toast.LENGTH_SHORT).show()
        }
    }

}