package com.example.quickprintukm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quickprintukm.databinding.ActivityStaffDashboardBinding

class StaffDashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStaffDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStaffDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up interactions for the profile section
        binding.editProfileBtn.setOnClickListener {
            // Handle Edit Profile click
        }

        binding.logoutBtn.setOnClickListener {
            // Handle Logout click, e.g., navigate to login screen
        }

        // Set up interactions for the order items
        // Since these are repeated views, you'd typically use a RecyclerView and Adapter,
        // but for structural consistency with the example, we'll set up listeners for the static views.
        binding.orderItem1.root.setOnClickListener {
            // Handle click for Order 1
        }
        binding.orderItem2.root.setOnClickListener {
            // Handle click for Order 2
        }
        binding.orderItem3.root.setOnClickListener {
            // Handle click for Order 3 (adjust if you only have 2 visible statically)
        }

        // Bottom navigation buttons
        binding.homeButton.setOnClickListener {
            // Navigate to the main/user home page, assuming staff user can access it too
            startActivity(Intent(this, StaffDashboardActivity::class.java))
            finish() // Optional: close this activity
        }

        binding.orderListButton.setOnClickListener {
            startActivity(Intent(this, StaffOrderListActivity::class.java))
        }

        binding.meButton.setOnClickListener {
            startActivity(Intent(this, StaffProfileActivity::class.java))
        }
    }
}