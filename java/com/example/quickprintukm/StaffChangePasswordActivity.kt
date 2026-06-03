package com.example.quickprintukm

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quickprintukm.databinding.ActivityChangePasswordBinding
import com.example.quickprintukm.databinding.ActivityMePageBinding
import com.example.quickprintukm.databinding.ActivityPrintNowBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class StaffChangePasswordActivity : AppCompatActivity() {

    private lateinit var editOldPassword: EditText
    private lateinit var editNewPassword: EditText
    private lateinit var editConfirmPassword: EditText
    private lateinit var btnSavePassword: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityChangePasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.backArrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
            val intent = Intent(this, StaffProfileActivity::class.java)
            startActivity(intent)
        }

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize views
        editOldPassword = findViewById(R.id.editOldPassword)
        editNewPassword = findViewById(R.id.editNewPassword)
        editConfirmPassword = findViewById(R.id.editConfirmPassword)
        btnSavePassword = findViewById(R.id.btnSavePassword)

        btnSavePassword.setOnClickListener {
            val oldPass = editOldPassword.text.toString().trim()
            val newPass = editNewPassword.text.toString().trim()
            val confirmPass = editConfirmPassword.text.toString().trim()

            // Basic validation
            if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPass != confirmPass) {
                Toast.makeText(this, "New password and confirm password do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = auth.currentUser
            if (user != null) {
                val email = user.email
                if (email != null) {
                    // Reauthenticate the user first
                    val credential = EmailAuthProvider.getCredential(email, oldPass)
                    user.reauthenticate(credential).addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            // Update password
                            user.updatePassword(newPass).addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show()
                                    finish() // Close the activity
                                } else {
                                    Toast.makeText(this, "Failed to change password: ${updateTask.exception?.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        } else {
                            Toast.makeText(this, "Old password is incorrect", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "User email not found", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No authenticated user found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

