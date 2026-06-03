package com.example.quickprintukm

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.quickprintukm.databinding.ActivityStaffProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class StaffProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStaffProfileBinding
    // Modern Activity Result API for Edit Profile
    private val editProfileLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            binding.tvStaffName.text = data?.getStringExtra("name")
            binding.tvStaffId.text = data?.getStringExtra("matric")
        }
    }
    private fun loadUserProfile() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    binding.tvStaffName.text = doc.getString("name")
                    binding.tvStaffName.text = doc.getString("matric")
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStaffProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set staff details (static for demonstration)
        binding.tvStaffName.text = "Ali"
        binding.tvStaffId.text = "A201134"

        // Set up interactions for the menu items
        binding.editProfileItem.setOnClickListener {
            val intent = Intent(this, StaffEditProfileActivity::class.java)
            intent.putExtra("name", binding.tvStaffName.text.toString())
            intent.putExtra("matric", binding.tvStaffId.text.toString())
            editProfileLauncher.launch(intent)
        }

        binding.changePasswordItem.setOnClickListener {
            val intent = Intent(this, StaffChangePasswordActivity::class.java)
            startActivity(intent)
        }

        binding.logoutItem.setOnClickListener {
            val dialog = android.app.AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setCancelable(true)
                .setPositiveButton("Yes") { _, _ ->
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                .setNegativeButton("Cancel") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                .create()

            dialog.show()
        }

        // Bottom navigation buttons (Assuming this staff member uses the Staff Dashboard Navigation)
        binding.homeButton.setOnClickListener {
            // Navigate to the main/user home page
            startActivity(Intent(this, StaffDashboardActivity::class.java))
        }

        binding.orderListButton.setOnClickListener {
            // Navigate to the staff order list
            startActivity(Intent(this, StaffOrderListActivity::class.java))
        }

        binding.meButton.setOnClickListener {
            // Already on Me page, do nothing or refresh
        }
    }
}