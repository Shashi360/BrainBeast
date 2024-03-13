package com.education.brainbeast.ui.education.ui.menuprofile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.education.brainbeast.MainActivity
import com.education.brainbeast.R
import com.education.brainbeast.databinding.FragmentUserProfileBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Locale


class UserProfileFragment : Fragment() {

    private lateinit var _binding: FragmentUserProfileBinding
    private val binding get() = _binding
    private lateinit var userProfileViewModel: UserProfileViewModel
    private val imagePERMISSION = 100
    private var selectedGender: String = ""
    private lateinit var selectedProfileImageUri: User
    private lateinit var user: User


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        binding.imgProfileBack.setOnClickListener {
            val intent = Intent(requireActivity(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        binding.imgProfileUserGmail.setOnClickListener {
            openGmail()
        }
        binding.imgProfileUserLinkedin.setOnClickListener {
            openLinkedIn()
        }
        binding.imgProfileUserWhatsapp.setOnClickListener {
            openWhatsApp()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userProfileViewModel = ViewModelProvider(this)[UserProfileViewModel::class.java]

        // Observe LiveData from ViewModel and update UI accordingly
        userProfileViewModel.userData.observe(viewLifecycleOwner) { userData ->
            user = userData
            updateUI(userData)
        }
        setupListeners()


//        // Observe LiveData from ViewModel and update UI accordingly
//        userProfileViewModel.userData.observe(viewLifecycleOwner) { userData ->
//            // Update UI with user data
//            updateUI(userData)
//        }
    }

    private fun setupListeners() {
        // Click listener for selecting an image from the gallery
        binding.imgProfileUserImage.setOnClickListener {
            checkStoragePermission()
        }
        binding.etProfileUserDob.setOnClickListener {
            showDatePickerDialog()
        }

        // Click listener for the save button
        binding.imgProfileEdit.setOnClickListener {
            // Get the updated user information from the UI
            val updatedUser = getUserDataFromUI()

            if (validateUserData(updatedUser)) {
                // Save the updated user information to the database
                userProfileViewModel.saveUserData(updatedUser)
                Toast.makeText(
                    requireActivity(),
                    "User data saved successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showDatePickerDialog() {
        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date of Birth")
            .build()

        datePicker.addOnPositiveButtonClickListener { selectedDate ->
            // Convert the selected date to the desired format (DD/MM/YYYY)
            val formattedDate =
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate)
            // Update the EditText with the selected date
            binding.etProfileUserDob.setText(formattedDate)
        }

        datePicker.show(requireFragmentManager(), "DateOfBirthPicker")
    }

    private fun validateUserData(user: User): Boolean {
        var isValid = true

        if (user.name.isBlank()) {
            Toast.makeText(requireActivity(), "Name cannot be empty", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (user.age <= 0) {
            Toast.makeText(requireActivity(), "Invalid age", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (user.gender.isBlank()) {
            Toast.makeText(requireActivity(), "Gender cannot be empty", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (user.dob.isBlank()) {
            Toast.makeText(requireActivity(), "Date of Birth cannot be empty", Toast.LENGTH_SHORT)
                .show()
            isValid = false
        }

        if (user.mobile.isBlank()) {
            Toast.makeText(requireActivity(), "Mobile number cannot be empty", Toast.LENGTH_SHORT)
                .show()
            isValid = false
        } else if (!isValidMobile(user.mobile)) {
            Toast.makeText(requireActivity(), "Invalid mobile number", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        if (user.email.isBlank()) {
            Toast.makeText(requireActivity(), "Email cannot be empty", Toast.LENGTH_SHORT).show()
            isValid = false
        } else if (!isValidEmail(user.email)) {
            Toast.makeText(requireActivity(), "Invalid email", Toast.LENGTH_SHORT).show()
            isValid = false
        }

        return isValid
    }

    private fun isValidMobile(mobile: String): Boolean {
        val mobileRegex = Regex("^\\d{10}$") // Regex pattern for a 10-digit mobile number
        return mobile.matches(mobileRegex)
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex =
            Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$") // Regex pattern for email validation
        return email.matches(emailRegex)
    }


    private fun openGalleryForImage() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, IMAGE_PICK_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri? = data.data
            if (imageUri != null) {
                // Ensure you have an instance of the User class
                selectedProfileImageUri = (userProfileViewModel.userData.value ?: User())
                // Update the profile image URL of the current user
                selectedProfileImageUri.profileImageUrl = imageUri.toString()
                // Save the updated user data
                userProfileViewModel.saveUserData(selectedProfileImageUri)
                // Load the selected image into the ImageView using Glide
                Glide.with(requireContext())
                    .load(imageUri)
                    .into(binding.imgProfileUserImage)
            } else {
                Toast.makeText(requireActivity(), "Try again", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun getUserDataFromUI(): User {
        // Retrieve user information from the UI components
        val userName = binding.etProfileUserName.text.toString().trim()
        val age = binding.etProfileUserAge.text.toString().toIntOrNull()
            ?: 0 // Convert to Int, default to 0 if conversion fails
        val dob = binding.etProfileUserDob.text.toString()
        val mobileNumber = binding.etProfileUserMobile.text.toString()
        val email = binding.etProfileUserEmail.text.toString()
        val profileImgUrl = selectedProfileImageUri

//        val spinner = binding.spinnerGender
//
//        // Create ArrayAdapter and set it to the Spinner
//        ArrayAdapter.createFromResource(
//            requireContext(),
//            R.array.gender_options,
//            android.R.layout.simple_spinner_dropdown_item
//        ).also { adapter ->
//            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//            spinner.adapter = adapter
//        }
//        // Set up a listener to handle item selections
//        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                // Retrieve the selected item
//                selectedGender = parent.getItemAtPosition(position).toString()
//
//                // Update the User object with the selected gender
//                val currentUser = userProfileViewModel.userData.value ?: User()
//                currentUser.gender = selectedGender
//
//                // Save the updated user data to the Room database
//                userProfileViewModel.saveUserData(currentUser)
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>) {
//                // Handle no item selected case
//            }
//        }

        // Create a User object with the retrieved information
        return User(
            name = userName,
            age = age,
            gender = selectedGender,
            dob = binding.etProfileUserDob.text.toString(),
            mobile = mobileNumber,
            email = email,
            profileImageUrl = profileImgUrl.toString()
        )
    }


    companion object {
        private const val IMAGE_PICK_REQUEST_CODE = 100
    }


    private fun updateUI(userData: User) {
        // Update UI components with user data fetched from the database
        binding.apply {
            // Update TextViews
            etProfileUserName.setText(userData.name)
            etProfileUserAge.setText(userData.age.toString())
            etProfileUserDob.setText(userData.dob)
            etProfileUserMobile.setText(userData.mobile)
            etProfileUserEmail.setText(userData.email)
            tvProfileUserNameHeader.text = userData.name
            etProfileUserGender.text = Editable.Factory.getInstance().newEditable(userData.gender)
        }

        // Load the profile image using Glide
        Glide.with(requireContext())
            .load(userData.profileImageUrl)
            .placeholder(R.drawable.ic_user) // Placeholder until the image is loaded
            .error(R.drawable.ic_user) // Error placeholder if image loading fails
            .into(binding.imgProfileUserImage)

    }


    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            // Request the permission
            requestStoragePermission()
        } else {
            // Permission is already granted
            openGalleryForImage()
        }
    }

    private fun requestStoragePermission() {
        requestPermissions(
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            imagePERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == imagePERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open gallery
                openGalleryForImage()
            } else {
                // Permission denied
                Toast.makeText(requireActivity(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openGmail() {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setClassName(
            "com.google.android.gm",
            "com.google.android.gm.ConversationListActivityGmail"
        )
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(requireActivity(), "Please install Gmail app first", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun openWhatsApp() {
        val packageManager = requireActivity().packageManager
        val i = Intent(Intent.ACTION_VIEW)
        try {
            val url = "https://api.whatsapp.com/send?phone=" + "+917760593100"
            i.`package` = "com.whatsapp"
            i.data = Uri.parse(url)
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i)
            } else {
                throw Exception()
            }
        } catch (e: Exception) {
            Toast.makeText(
                requireActivity(),
                "Please install WhatsApp app first",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openLinkedIn() {
        val linkedInProfileUrl = "https://www.linkedin.com/in/YOUR_PROFILE_URL"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkedInProfileUrl))
        intent.setPackage("com.linkedin.android")
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(
                requireActivity(),
                "Please install LinkedIn app first",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}