package com.education.brainbeast.ui.education.ui.menusearch

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.education.brainbeast.R
import com.education.brainbeast.databinding.FragmentMatchesCoursesBinding
import com.education.brainbeast.databinding.ItemYoutubeVideoBinding
import com.education.brainbeast.ui.education.ui.listeners.MatchCourseClickListener
import com.education.brainbeast.ui.education.ui.menuhome.HomeCoursePopularVideoID
import com.education.brainbeast.ui.education.ui.menuhome.VideoItem
import com.education.brainbeast.ui.education.ui.model.MatchCourse
import com.education.brainbeast.ui.education.ui.utils.GridSpacingItemDecoration
import com.education.brainbeast.ui.education.ui.utils.MyUtilsApp
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class MatchesCoursesFragment : Fragment(), MatchCourseClickListener {
    private lateinit var binding: FragmentMatchesCoursesBinding
    private lateinit var mContext: Context
    private lateinit var youtubePlayerView: YouTubePlayerView


    private val popularVideoID = listOf(
        HomeCoursePopularVideoID("vhfzN69ALpY"),
        HomeCoursePopularVideoID("1RuNijeB_yc"),
        HomeCoursePopularVideoID("IdeqQE_POXo"),
        HomeCoursePopularVideoID("vhfzN69ALpY"),
        HomeCoursePopularVideoID("1RuNijeB_yc"),
        HomeCoursePopularVideoID("IdeqQE_POXo"),
        HomeCoursePopularVideoID("vhfzN69ALpY"),
        HomeCoursePopularVideoID("1RuNijeB_yc"),
        HomeCoursePopularVideoID("IdeqQE_POXo"),
        HomeCoursePopularVideoID("vhfzN69ALpY"),
        HomeCoursePopularVideoID("1RuNijeB_yc"),
        HomeCoursePopularVideoID("IdeqQE_POXo")
        // Add more video IDs as needed
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMatchesCoursesBinding.inflate(layoutInflater)
        val view: View = binding.root
        mContext = this.requireContext()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRecyclerView()
    }

    private fun initializeRecyclerView() {
        // Initialize RecyclerView adapters
        val popularVideosAdapter =
            YouTubeVideoAdapter(popularVideoID.map {
                VideoItem.HomeCoursePopularVideoID(
                    it.videoId
                )
            })

        // Set RecyclerView adapters
        binding.rvCourseStagged.adapter = popularVideosAdapter

        val spacing = resources.getDimensionPixelSize(R.dimen.grid_spacing)

// Set GridLayoutManager for RecyclerView
        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvCourseStagged.layoutManager = layoutManager

// Apply item decoration
        binding.rvCourseStagged.addItemDecoration(GridSpacingItemDecoration(2, spacing, true))

    }

    override fun onScrollPagerItemClick(courseCard: MatchCourse?, imageView: ImageView?) {
        Log.d(TAG, "onScrollPagerItemClick: $courseCard ")
        if (courseCard != null) {
            MyUtilsApp.showToast(mContext, courseCard.name)
        }
    }
    // initializing adapter class -------------------------------------------------------------->

    class YouTubeVideoAdapter(private val videos: List<VideoItem>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemYoutubeVideoBinding.inflate(inflater, parent, false)

            return when (viewType) {
                TYPE_POPULAR -> PopularViewHolder(binding)
//                TYPE_TUTORIAL -> TutorialViewHolder(binding)
                else -> throw IllegalArgumentException("Invalid view type")
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val videoItem = videos[position]

            when (holder) {
                is PopularViewHolder -> holder.bind(videoItem as VideoItem.HomeCoursePopularVideoID)
//                is TutorialViewHolder -> holder.bind(videoItem as VideoItem.HomeCourseTutorialsVideoID)
            }
        }

        override fun getItemCount(): Int = videos.size

        override fun getItemViewType(position: Int): Int {
            return when (videos[position]) {
                is VideoItem.HomeCoursePopularVideoID -> TYPE_POPULAR
//        is VideoItem.HomeCourseTutorialsVideoID -> TYPE_TUTORIAL
                else -> super.getItemViewType(position)
            }
        }


        inner class PopularViewHolder(private val binding: ItemYoutubeVideoBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(popularVideo: VideoItem.HomeCoursePopularVideoID) {
                initializeYouTubePlayer(binding.youtubePlayerView, popularVideo.videoId)
            }
        }

//        inner class TutorialViewHolder(private val binding: ItemYoutubeVideoBinding) :
//            RecyclerView.ViewHolder(binding.root) {
//
//            fun bind(tutorialVideo: VideoItem.HomeCourseTutorialsVideoID) {
//                initializeYouTubePlayer(binding.youtubePlayerView, tutorialVideo.videoId)
//            }
//        }

        private fun initializeYouTubePlayer(youTubePlayerView: YouTubePlayerView, videoId: String) {
            youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(videoId, 0f)
                }
            })
        }

        companion object {
            private const val TYPE_POPULAR = 0
            private const val TYPE_TUTORIAL = 1
            private const val TAG = "MatchesCoursesFragment"
        }
    }
}