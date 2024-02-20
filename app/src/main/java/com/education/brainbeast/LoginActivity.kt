package com.education.brainbeast

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.education.brainbeast.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        overridePendingTransition(0, 0)
        val relativeLayout = findViewById<View>(R.id.login_container)
        val animation: Animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        relativeLayout.startAnimation(animation)

        binding.buttonLogin.setOnClickListener {
            val mobileNumber = binding.etUserMobile.text.toString().trim()
            val otp = binding.codeBox.text.toString().trim()

            if (isValidMobileNumber(mobileNumber) && isValidOTP(otp)) {
                Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, SignUpActivity::class.java))
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Please enter a valid mobile number and OTP",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun isValidMobileNumber(mobileNumber: String): Boolean {
        return mobileNumber.length == 10 && mobileNumber.matches(Regex("\\d+"))
    }

    private fun isValidOTP(otp: String): Boolean {
        return otp.length == 4 && otp.matches(Regex("\\d+"))
    }
}