package com.education.brainbeast

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.education.brainbeast.databinding.ActivityMainBinding
import com.education.brainbeast.ui.education.ui.helpers.BottomNavigationBehavior
import com.education.brainbeast.ui.education.ui.menucourses.CoursesStaggedFragment
import com.education.brainbeast.ui.education.ui.menuhome.HomeCoursesFragment
import com.education.brainbeast.ui.education.ui.menujoinvideoconference.JoinVideoConferenceFragment
import com.education.brainbeast.ui.education.ui.menuprofile.UserProfileFragment
import com.education.brainbeast.ui.education.ui.menusearch.MatchesCoursesFragment
import com.education.brainbeast.ui.education.ui.utils.ConnectionLiveData
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


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

    private fun toggleDrawer() {
        val drawer = binding.drawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            drawer.openDrawer(GravityCompat.START)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setNavigationItemSelectedListener(this)
        connectionLiveData = ConnectionLiveData(this)
        connectionLiveData.observe(this) { isNetworkAvailable ->
            isNetworkAvailable?.let { internetConnected ->
                if (internetConnected) {
                    Toast.makeText(this, "Internet connected", Toast.LENGTH_SHORT).show()
                } else Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show()
            }
        }

        setupNavigation()
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

//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            if (destination.id == R.id.userProfileFragment) {
//                bottomNavigationView.visibility = View.GONE
//            } else {
//                bottomNavigationView.visibility = View.VISIBLE
//            }
//        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> findNavController(R.id.nav_host_fragment).navigate(R.id.homeCoursesFragment)
            R.id.nav_profile -> findNavController(R.id.nav_host_fragment).navigate(R.id.userProfileFragment)
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


