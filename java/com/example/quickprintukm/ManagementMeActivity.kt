package com.example.quickprintukm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.example.quickprintukm.databinding.ActivityMePageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.quickprintukm.databinding.ActivityManagementMeBinding

class ManagementMeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManagementMeBinding

    // Modern Activity Result API for Edit Profile
    private val editProfileLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data
            binding.profileName.text = data?.getStringExtra("name")
            binding.profileID.text = data?.getStringExtra("matric")
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
                    binding.profileName.text = doc.getString("name")
                    binding.profileID.text = doc.getString("matric")
                }
            }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityManagementMeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadUserProfile()
        // Bottom navigation buttons
        binding.homeButton.setOnClickListener {
            startActivity(Intent(this, ManagementMainActivity::class.java))
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
            //Already on this page
        }
        // --- CardView Rows ---

        // Edit Profile
        binding.editProfileRow.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            intent.putExtra("name", binding.profileName.text.toString())
            intent.putExtra("matric", binding.profileID.text.toString())
            editProfileLauncher.launch(intent)
        }

        // Change Password
        binding.changePasswordRow.setOnClickListener {
            val intent = Intent(this, ChangePasswordActivity::class.java)
            startActivity(intent)
        }
        binding.logoutRow.setOnClickListener {

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

    }
}


