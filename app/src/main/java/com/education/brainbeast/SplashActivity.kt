/*
 * Copyright (c) 2021. roger
 */
package com.education.brainbeast



import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

//Ref - https://github.com/MtsRovari/SplashScreen/tree/master

class SplashScreen : AppCompatActivity() {
    private var isFirstAnimation = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val hold = AnimationUtils.loadAnimation(this, R.anim.hold)

        val translateScale = AnimationUtils.loadAnimation(this, R.anim.translate_scale)
        val imageView = findViewById<ImageView>(R.id.header_icon)
        translateScale.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                if (!isFirstAnimation) {
                    imageView.clearAnimation()
                    val intent = Intent(
                        this@SplashScreen,
                        LoginActivity::class.java
                    )
                    startActivity(intent)
                    finish()
                }
                isFirstAnimation = true
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        hold.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                imageView.clearAnimation()
                imageView.startAnimation(translateScale)
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        imageView.startAnimation(hold)
    }
}