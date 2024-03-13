package com.education.brainbeast

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.room.Room
import com.education.brainbeast.databinding.ActivityMainBinding
import com.education.brainbeast.ui.education.ui.helpers.BottomNavigationBehavior
import com.education.brainbeast.ui.education.ui.menucourses.CoursesStaggedFragment
import com.education.brainbeast.ui.education.ui.menuhome.HomeCoursesFragment
import com.education.brainbeast.ui.education.ui.menujoinvideoconference.JoinVideoConferenceFragment
import com.education.brainbeast.ui.education.ui.menuprofile.UserProfileFragment
import com.education.brainbeast.ui.education.ui.menuprofile.UserRoomDatabase
import com.education.brainbeast.ui.education.ui.menusearch.MatchesCoursesFragment
import com.education.brainbeast.ui.education.ui.utils.ConnectionLiveData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


//UI design - https://dribbble.com/shots/6482664-Design-Course-App-UI

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var connectionLiveData: ConnectionLiveData

    private val mOnNavigationItemSelectedListener =
        NavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeCoursesFragment -> loadFragment(HomeCoursesFragment())
                R.id.coursesStaggedFragment -> loadFragment(CoursesStaggedFragment())
                R.id.searchMatchesCoursesFragment -> loadFragment(MatchesCoursesFragment())
                R.id.joinVideoCallFragment -> loadFragment(JoinVideoConferenceFragment())
                R.id.userProfileFragment -> loadFragment(UserProfileFragment())
            }

            // Close the drawer after an item is selected
            binding.drawerLayout.closeDrawer(GravityCompat.START)

            title = item.title
            true
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the toolbar as ActionBar
        setSupportActionBar(binding.toolbar)

        // Initialize ActionBarDrawerToggle
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        // Set the DrawerListener
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Set the navigation item selection listener
        binding.navView.setNavigationItemSelectedListener(this)

        // Observe network connection
        connectionLiveData = ConnectionLiveData(this)
        connectionLiveData.observe(this) { isNetworkAvailable ->
            isNetworkAvailable?.let { internetConnected ->
                val message = if (internetConnected) "Internet connected" else "No Internet"
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        // Setup navigation
        setupNavigation()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                toggleDrawer() // Toggle drawer when toolbar home button is clicked
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun toggleDrawer() {
        val drawer = binding.drawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            drawer.openDrawer(GravityCompat.START)
        }
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle(title)
        binding.toolbar.title = title // Set the title on the toolbar
    }

    private fun setupNavigation() {
        val layoutParams = binding.appBarMain.bottomNavigationView.layoutParams
        if (layoutParams is CoordinatorLayout.LayoutParams) {
            layoutParams.behavior = BottomNavigationBehavior()
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> findNavController(R.id.nav_host_fragment).navigate(R.id.homeCoursesFragment)
//            R.id.nav_profile -> findNavController(R.id.nav_host_fragment).navigate(R.id.userProfileFragment)
            // R.id.nav_setting -> {}
            R.id.nav_share_app -> shareApp()
            R.id.nav_logout -> logoutUser()
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            R.anim.fragment_fade_enter,
            R.anim.fragment_fade_exit,
            R.anim.fragment_fade_enter,
            R.anim.fragment_fade_exit
        )
        transaction.replace(R.id.nav_host_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun logoutUser() {
        // Get a reference to the database
        val db =
            Room.databaseBuilder(applicationContext, UserRoomDatabase::class.java, "user-db")
                .build()

        // Insert the user into the database using a coroutine
        CoroutineScope(Dispatchers.IO).launch {
            db.userDao().deleteAll()
        }
        // Redirect user to login screen
        val intent = Intent(this, SplashScreen::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun shareApp() {
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

    companion object {
        val TAG = MainActivity::class.simpleName
    }
}


