package com.education.brainbeast

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.education.brainbeast.MainActivity.Companion.TAG
import com.education.brainbeast.databinding.ActivitySignUpBinding
import com.education.brainbeast.ui.education.ui.menuprofile.User
import com.education.brainbeast.ui.education.ui.menuprofile.UserRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private var profileImageBitmap: Bitmap? = null
    private lateinit var genderSelection: Array<String>
    private var selectedGender: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgSignProfileUserImage.setOnClickListener {
            selectImageFromGallery()
        }

        binding.btnSignUp.setOnClickListener {
            signUp()
        }
        binding.tvSignProfileUserDob.setOnClickListener {
            showDatePickerDialog()
        }
        binding.tvSwitchToSignIn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        genderSelection = resources.getStringArray(R.array.gender_options)
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item, genderSelection
        )
        binding.signSpinnerGender.adapter = adapter
        binding.signSpinnerGender.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                selectedGender = parent.getItemAtPosition(position).toString()
                Log.d(TAG, "Selected gender: $selectedGender")
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Write code to perform some action
            }
        }
    }

    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val selectedImageUri = result.data?.data
                selectedImageUri?.let { uri ->
                    try {
                        profileImageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                        binding.imgSignProfileUserImage.setImageBitmap(profileImageBitmap)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }



    private fun signUp() {
        val name = binding.etSignProfileUserName.text.toString().trim()
        val ageText = binding.etSignProfileUserAge.text.toString().trim()
        val dob = binding.tvSignProfileUserDob.text.toString().trim()
        val mobile = binding.etSignProfileUserMobile.text.toString().trim()
        val email = binding.etSignProfileUserEmail.text.toString().trim()

        if (name.isEmpty() || ageText.isEmpty() || dob.isEmpty() || mobile.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val age = ageText.toIntOrNull()
        if (age == null || age <= 0) {
            Toast.makeText(this, "Please enter a valid age", Toast.LENGTH_SHORT).show()
            return
        }

        if (profileImageBitmap == null) {
            Toast.makeText(this, "Please select a profile image", Toast.LENGTH_SHORT).show()
            return
        }


        // Save user data to Room database
        val user = User(
            name = name,
            age = age,
            gender = selectedGender.toString(),
            dob = dob,
            mobile = mobile,
            email = email,
            profileImageUrl = saveProfileImageToStorage(profileImageBitmap!!)
        )

        // Save user to Room database
        saveUserToDatabase(user)

        // Navigate to the main activity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun saveProfileImageToStorage(bitmap: Bitmap): String {
        // Get the directory where you want to save the image
        val directory = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "profile_images")

        // Create the directory if it doesn't exist
        if (!directory.exists()) {
            directory.mkdirs()
        }

        // Generate a unique file name for the image
        val fileName = "profile_image_${System.currentTimeMillis()}.jpg"

        // Create a file object with the directory and file name
        val file = File(directory, fileName)

        // Create an output stream to write the bitmap data to the file
        val outputStream = FileOutputStream(file)

        // Compress the bitmap and write it to the file
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

        // Close the output stream
        outputStream.close()

        // Return the file path
        return file.absolutePath
    }


    private fun saveUserToDatabase(user: User) {
        // Get a reference to the database
        val db =
            Room.databaseBuilder(applicationContext, UserRoomDatabase::class.java, "user-db").build()

        // Insert the user into the database using a coroutine
        CoroutineScope(Dispatchers.IO).launch {
            db.userDao().deleteAll()
            db.userDao().insert(user)
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(selectedYear, selectedMonth, selectedDay)

                val dateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedDate.time)

                binding.tvSignProfileUserDob.text = formattedDate
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }
}
