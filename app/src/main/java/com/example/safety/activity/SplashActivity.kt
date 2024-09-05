package com.example.safety.activity

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.safety.R
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {
    private lateinit var zoomImageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        zoomImageView = findViewById(R.id.splash_imageView)
        animateZoomOut()
    }

    private fun animateZoomOut() {
        zoomImageView.animate()
            .scaleX(0.4f)
            .scaleY(0.4f)
            .setDuration(1000)
            .withEndAction {
                animateZoomIn()
            }
            .start()
    }

    private fun animateZoomIn() {
        zoomImageView.animate()
            .scaleX(30.0f)
            .scaleY(30.0f)
            .setDuration(500)
            .withEndAction {
                // Start the new activity here
                startNewActivity()
            }
            .start()
    }

    private fun startNewActivity() {
        val auth = Firebase.auth
        if (auth.currentUser!= null) {
            startActivity(Intent(this, MainActivity::class.java))
        }
        else
        {
            startActivity(Intent(this,SignInActivity::class.java))
        }

        finish()
    }
}