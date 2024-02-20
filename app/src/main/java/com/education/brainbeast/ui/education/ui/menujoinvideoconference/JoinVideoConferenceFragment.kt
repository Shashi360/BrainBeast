package com.education.brainbeast.ui.education.ui.menujoinvideoconference

import android.app.Application
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import com.education.brainbeast.databinding.FragmentJoinVideoMeetingBinding
import com.education.brainbeast.databinding.ItemMeetingDetailsBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Random
import androidx.room.OnConflictStrategy.Companion as OnConflictStrategy1

// https://github.com/suyalamritanshu/Video-Calling-App/tree/main
class JoinVideoConferenceFragment : Fragment() {

    private lateinit var binding: FragmentJoinVideoMeetingBinding
    private lateinit var meetingViewModel: MeetingViewModel
    private lateinit var meetingsAdapter: MeetingsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJoinVideoMeetingBinding.inflate(inflater, container, false)

//        createNotificationChannel()

        // Schedule notification for 15 minutes before meeting time
//        scheduleNotification()
        return binding.root
    }

//    private fun scheduleNotification(meetingTimeInMillis: Long) {
//        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val notificationIntent = Intent(requireContext(), NotificationReceiver::class.java)
//        val pendingIntent = PendingIntent.getBroadcast(
//            requireContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
//        )
//
//        // Schedule notification for 14 minutes before meeting time
//        val notificationTime = meetingTimeInMillis - (14 * 60 * 1000)
//        alarmManager.setExact(
//            AlarmManager.RTC_WAKEUP,
//            notificationTime,
//            pendingIntent
//        )
//    }


//    private fun createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                "scheduled_meetings_channel_id",
//                "Scheduled Meetings Channel",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            val notificationManager =
//                requireContext().getSystemService(NotificationManager::class.java)
//            notificationManager.createNotificationChannel(channel)
//        }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        meetingViewModel = ViewModelProvider(this)[MeetingViewModel::class.java]

        // Set up RecyclerView
        meetingsAdapter = MeetingsAdapter(
            onItemClick = { meeting ->
                redirectToGoogleMeet(meeting.meetingId)
            },
            onDeleteClick = { meeting ->
                meetingViewModel.deleteMeeting(meeting)
            }
        )

        binding.rvVideoMeeting.layoutManager = LinearLayoutManager(requireContext())
        binding.rvVideoMeeting.adapter = meetingsAdapter

        // Set onClickListener for btn_create_join_meeting
        binding.btnCreateJoinMeeting.setOnClickListener {
            openDateTimePicker()
        }

        // Set onClickListener for share button
        binding.share.setOnClickListener {
            val meetingId = "your_meeting_id_here"
            val googleMeetLink = "https://meet.google.com/$meetingId"

            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Join the meeting with Google Meet: $googleMeetLink")
                type = "text/plain"
            }

            // Start an activity to share the Google Meet link
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }


        // Observe changes in meeting list
        meetingViewModel.allMeetings.observe(viewLifecycleOwner) { meetings ->
            meetingsAdapter.submitList(meetings)
        }
    }

    private fun openDateTimePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)

                val timePickerDialog =
                    TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                        // Handle the selected date and time
                        val selectedDateTime = Calendar.getInstance().apply {
                            set(
                                selectedYear,
                                selectedMonth,
                                selectedDay,
                                selectedHour,
                                selectedMinute
                            )
                        }

                        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

                        val formattedDate = dateFormat.format(selectedDateTime.time)
                        val formattedTime = timeFormat.format(selectedDateTime.time)

                        val dateTimeString = "$formattedDate $formattedTime"

                        // Save the selected meeting date and time to the Room database
                        saveMeetingDateTime(dateTimeString)
                    }, hour, minute, false)

                timePickerDialog.show()

            }, year, month, day)

        datePickerDialog.datePicker.minDate = System.currentTimeMillis() // Set min date as today
        datePickerDialog.show()
    }

    private fun saveMeetingDateTime(dateTimeInMillis: String) {
        // Perform Room database operations to save the meeting date and time
        meetingViewModel.saveMeetingDateTime(dateTimeInMillis)
    }


    private fun redirectToGoogleMeet(meetingId: String) {
        // Launch Google Meet app or web browser with meeting link
        val googleMeetLink = "https://meet.google.com/$meetingId"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(googleMeetLink))
        startActivity(intent)
    }
}

// MeetingViewModel.kt
class MeetingViewModel(application: Application) : AndroidViewModel(application) {

    fun saveMeetingDateTime(dateTimeInMillis: String) {
        viewModelScope.launch {
            meetingRepository.saveMeetingDateTime(dateTimeInMillis)
        }
    }

    fun deleteMeeting(meeting: Meeting) {
        viewModelScope.launch {
            meetingRepository.deleteMeeting(meeting)
        }
    }

    private val meetingRepository: MeetingRepository
    val allMeetings: LiveData<List<Meeting>>

    init {
        val meetingDao = MeetingRoomDatabase.getDatabase(application).meetingDao()
        meetingRepository = MeetingRepository(meetingDao)
        allMeetings = meetingRepository.allMeetings
    }

    // Add functions to insert, update, delete meetings as needed
}

// MeetingRepository.kt
class MeetingRepository(private val meetingDao: MeetingDao) {

    val allMeetings: LiveData<List<Meeting>> = meetingDao.getAllMeetings()

    // Add functions to insert, update, delete meetings as needed
    suspend fun saveMeetingDateTime(dateTimeInMillis: String) {
        // Perform Room database insertion operation
        // Use DAO (Data Access Object) to insert data into the database
        meetingDao.insertMeetingDateTime(
            Meeting(
                meetingId = generateMeetingId(),
                dateTimeInMillis = dateTimeInMillis
            )
        )
    }

    suspend fun deleteMeeting(meeting: Meeting) {
        meetingDao.deleteMeeting(meeting)
    }
}

fun generateMeetingId(): String {
    val timestamp = System.currentTimeMillis() // Current timestamp
    val random = Random().nextInt(1000) // Random number between 0 and 999
    return "$timestamp-$random" // Combine timestamp and random number
}

// MeetingRoomDatabase.kt
@Database(entities = [Meeting::class], version = 1, exportSchema = false)
abstract class MeetingRoomDatabase : RoomDatabase() {

    abstract fun meetingDao(): MeetingDao

    companion object {
        @Volatile
        private var INSTANCE: MeetingRoomDatabase? = null

        fun getDatabase(context: Context): MeetingRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MeetingRoomDatabase::class.java,
                    "meeting_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

// MeetingDao.kt
@Dao
interface MeetingDao {

    @Query("SELECT * FROM meeting_table ORDER BY dateTimeInMillis DESC")
    fun getAllMeetings(): LiveData<List<Meeting>>

    // Add functions to insert, update, delete meetings as needed
    @Insert(onConflict = OnConflictStrategy1.REPLACE)
    suspend fun insertMeetingDateTime(meetingDateTime: Meeting)

    @Delete()
    suspend fun deleteMeeting(meetingDateTime: Meeting)

}

// Meeting.kt
@Entity(tableName = "meeting_table")
data class Meeting(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "meeting_id") val meetingId: String,
    @ColumnInfo(name = "dateTimeInMillis") val dateTimeInMillis: String
)


// MeetingsAdapter.kt
class MeetingsAdapter(
    private val onItemClick: (Meeting) -> Unit,
    private val onDeleteClick: (Meeting) -> Unit
) :
    ListAdapter<Meeting, MeetingsAdapter.MeetingViewHolder>(MeetingDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingViewHolder {
        val binding =
            ItemMeetingDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MeetingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MeetingViewHolder, position: Int) {
        val meeting = getItem(position)
        holder.bind(meeting)
    }

    inner class MeetingViewHolder(private val binding: ItemMeetingDetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val meeting = getItem(position)
                    onItemClick(meeting)
                }
            }
            binding.imgItemMeetingDelete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val meeting = getItem(position)
                    onDeleteClick(meeting)
                }
            }
        }

        fun bind(meeting: Meeting) {
            binding.apply {
                tvItemMeetingId.text = meeting.meetingId
                tvItemMeetingDateTime.text = meeting.dateTimeInMillis
            }
        }
    }


    class MeetingDiffCallback : DiffUtil.ItemCallback<Meeting>() {
        override fun areItemsTheSame(oldItem: Meeting, newItem: Meeting): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Meeting, newItem: Meeting): Boolean {
            return oldItem == newItem
        }
    }
}






