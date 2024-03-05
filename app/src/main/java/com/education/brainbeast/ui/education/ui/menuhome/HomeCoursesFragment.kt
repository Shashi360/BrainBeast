package com.education.brainbeast.ui.education.ui.menuhome

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.education.brainbeast.R
import com.education.brainbeast.databinding.FragmentHomeCoursesBinding
import com.education.brainbeast.databinding.ItemYoutubeVideoBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView


class HomeCoursesFragment : Fragment() {
    private lateinit var binding: FragmentHomeCoursesBinding
    private lateinit var youtubePlayerView: YouTubePlayerView

    private fun setUpUI() {
        val percentage = resources.getString(R.string.percentage_course, 75)
        binding.tvPercentage.text = percentage
    }

    private val popularVideoID = listOf(
        HomeCoursePopularVideoID("vhfzN69ALpY"),
        HomeCoursePopularVideoID("1RuNijeB_yc"),
        HomeCoursePopularVideoID("IdeqQE_POXo")
        // Add more video IDs as needed
    )
    private val tutorialsVideoID = listOf(
        HomeCourseTutorialsVideoID("S6QHTZNbE7E"),
        HomeCourseTutorialsVideoID("izzJqv9cfJg"),
        HomeCourseTutorialsVideoID("xmYXGZPouek")
        // Add more video IDs as needed
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHomeCoursesBinding.inflate(layoutInflater)
        val view: View = binding.root

        shareAndEarn()
        popularCourseSellAll()
        tutorialsSellAll()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRecyclerView()
    }


    private fun popularCourseSellAll() {
        binding.tvHomePopularCourseSeeAll.setOnClickListener {
            findNavController().navigate(R.id.action_homeCoursesFragment_to_coursesStaggedFragment)
        }
    }


    private fun tutorialsSellAll() {
        binding.tvHomeTutorialsSeeAll.setOnClickListener {
            findNavController().navigate(R.id.action_homeCoursesFragment_to_searchMatchesCoursesFragment)
        }
    }

    private fun shareAndEarn() {
        binding.rlHomeShareAndEarn.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Check out this cool app!")
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Hey, I found this awesome app that you might like. Download it now: [YOUR_APP_PLAY_STORE_LINK]"
                )
            }
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }
    }

    private fun initializeRecyclerView() {
        // Initialize RecyclerView adapters
        val popularVideosAdapter =
            YouTubeVideoAdapter(popularVideoID.map { VideoItem.HomeCoursePopularVideoID(it.videoId) })
        val tutorialVideosAdapter =
            YouTubeVideoAdapter(tutorialsVideoID.map { VideoItem.HomeCourseTutorialsVideoID(it.videoId) })

        // Set RecyclerView adapters
        binding.rvPopularCourse.adapter = popularVideosAdapter
        binding.rvTutorialsCourse.adapter = tutorialVideosAdapter

        // Set LinearLayoutManager for RecyclerViews
        binding.rvPopularCourse.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvTutorialsCourse.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    // initializing adapter class -------------------------------------------------------------->

    class YouTubeVideoAdapter(private val videos: List<VideoItem>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemYoutubeVideoBinding.inflate(inflater, parent, false)

            return when (viewType) {
                TYPE_POPULAR -> PopularViewHolder(binding)
                TYPE_TUTORIAL -> TutorialViewHolder(binding)
                else -> throw IllegalArgumentException("Invalid view type")
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val videoItem = videos[position]

            when (holder) {
                is PopularViewHolder -> holder.bind(videoItem as VideoItem.HomeCoursePopularVideoID)
                is TutorialViewHolder -> holder.bind(videoItem as VideoItem.HomeCourseTutorialsVideoID)
            }
        }

        override fun getItemCount(): Int = videos.size

        override fun getItemViewType(position: Int): Int {
            return when (videos[position]) {
                is VideoItem.HomeCoursePopularVideoID -> TYPE_POPULAR
                is VideoItem.HomeCourseTutorialsVideoID -> TYPE_TUTORIAL
            }
        }

        inner class PopularViewHolder(private val binding: ItemYoutubeVideoBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(popularVideo: VideoItem.HomeCoursePopularVideoID) {
                initializeYouTubePlayer(binding.youtubePlayerView, popularVideo.videoId)
            }
        }

        inner class TutorialViewHolder(private val binding: ItemYoutubeVideoBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(tutorialVideo: VideoItem.HomeCourseTutorialsVideoID) {
                initializeYouTubePlayer(binding.youtubePlayerView, tutorialVideo.videoId)
            }
        }

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
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        youtubePlayerView.release()
    }
}