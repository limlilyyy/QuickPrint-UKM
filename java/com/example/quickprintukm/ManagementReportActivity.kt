package com.example.quickprintukm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quickprintukm.databinding.ActivityManagementReportBinding

class ManagementReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManagementReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagementReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupMenuActions()
        setupBottomNavigation()
    }

    /**
     * Handle Manage System menu clicks
     */
    private fun setupMenuActions() {

        binding.manageMReportRow.setOnClickListener {
            // Navigate to Manage Price page
            //startActivity(Intent(this, ManagePriceActivity::class.java))
        }

        binding.manageYReportRow.setOnClickListener {
            // Navigate to Settings page
            //startActivity(Intent(this, SystemSettingsActivity::class.java))
        }

        binding.staffCustomerRow.setOnClickListener {
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
            startActivity(Intent(this, ManagementPageActivity::class.java))
            finish()
        }

        binding.reportButton.setOnClickListener {
            //Already on this page
        }

        binding.meButton.setOnClickListener {
            startActivity(Intent(this, ManagementMeActivity::class.java))
            finish()
        }
    }
}
