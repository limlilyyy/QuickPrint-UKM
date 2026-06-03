package com.example.quickprintukm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quickprintukm.databinding.ActivityManagementManageBinding

class ManagementPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManagementManageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagementManageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupMenuActions()
        setupBottomNavigation()
    }

    /**
     * Handle Manage System menu clicks
     */
    private fun setupMenuActions() {

        binding.managePriceRow.setOnClickListener {
            // Navigate to Manage Price page
            //startActivity(Intent(this, ManagePriceActivity::class.java))
        }

        binding.settingsRow.setOnClickListener {
            // Navigate to Settings page
            //startActivity(Intent(this, SystemSettingsActivity::class.java))
        }

        binding.rewardPromoRow.setOnClickListener {
            // Navigate to Reward & Promotion page
            //startActivity(Intent(this, RewardPromotionActivity::class.java))
        }
    }

    /**
     * Handle Bottom Navigation actions
     */
    private fun setupBottomNavigation() {

        binding.homeButton.setOnClickListener {
            startActivity(Intent(this, ManagementMainActivity::class.java))
            finish()
        }

        binding.manageButton.setOnClickListener {
            //Already on this page
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
