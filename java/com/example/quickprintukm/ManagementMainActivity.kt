package com.example.quickprintukm

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quickprintukm.databinding.ActivityManagementMainBinding

class ManagementMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManagementMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagementMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Fade in the welcome text
        binding.tvWelcome.alpha = 0f
        binding.tvWelcome.animate()
            .alpha(1f)
            .setDuration(1500)
            .start()

        // Bounce animation for printer image
        val bounce = ObjectAnimator.ofFloat(binding.imgPrinter, "translationY", -30f, 0f)
        bounce.duration = 800
        bounce.repeatCount = ObjectAnimator.INFINITE
        bounce.repeatMode = ObjectAnimator.REVERSE
        bounce.start()

        setupBottomNavigation()
    }
    private fun setupBottomNavigation() {

        binding.homeButton.setOnClickListener {
            //startActivity(Intent(this, StaffDashboardActivity::class.java))
            finish()
        }

        binding.manageButton.setOnClickListener {
            startActivity(Intent(this, ManagementPageActivity::class.java))
            finish()
        }

        binding.reportButton.setOnClickListener {
            startActivity(Intent(this, ManagementReportActivity::class.java))
            finish()
        }

        binding.meButton.setOnClickListener {
            startActivity(Intent(this, ManagementMeActivity::class.java))
            finish()
        }
    }
}

