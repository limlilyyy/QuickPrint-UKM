package com.example.quickprintukm

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quickprintukm.databinding.ActivityMainPageBinding

class MainPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupHeaderAnimation()
        setupPrinterAnimation()
        setupPromoAnimation()
        setupButtons()
        setupBottomNavigation()
    }

    private fun setupHeaderAnimation() {
        binding.tvWelcome.alpha = 0f
        binding.tvWelcome.animate()
            .alpha(1f)
            .setDuration(1200)
            .start()
    }

    private fun setupPrinterAnimation() {
        ObjectAnimator.ofFloat(binding.imgPrinter, "translationY", -20f, 20f).apply {
            duration = 1400
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            start()
        }
    }

    private fun setupPromoAnimation() {
        // Floating animation
        ObjectAnimator.ofFloat(binding.promoCard, "translationY", 0f, -12f).apply {
            duration = 1600
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            start()
        }

        // Subtle pulse
        ObjectAnimator.ofFloat(binding.promoCard, "scaleX", 1f, 1.02f).apply {
            duration = 1600
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            start()
        }

        ObjectAnimator.ofFloat(binding.promoCard, "scaleY", 1f, 1.02f).apply {
            duration = 1600
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            start()
        }
    }

    private fun setupButtons() {
        binding.printBtn.setOnClickListener {
            startActivity(Intent(this, PrintNowActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.promoCard.setOnClickListener {
            startActivity(Intent(this, RewardActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    private fun setupBottomNavigation() {
        binding.homeButton.setOnClickListener {
            // already on home
        }

        binding.activityButton.setOnClickListener {
            startActivity(Intent(this, ActivityPageActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.notificationButton.setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        binding.meButton.setOnClickListener {
            startActivity(Intent(this, MePageActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
}
