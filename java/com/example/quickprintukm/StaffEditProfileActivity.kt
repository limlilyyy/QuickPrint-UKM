package com.example.quickprintukm

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quickprintukm.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class StaffEditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    // Modern Activity Result API for Edit Profile


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Back arrow
        binding.backArrow.setOnClickListener {
            finish()
        }

        // Pre-fill data
        binding.editName.setText(intent.getStringExtra("name"))
        binding.editMatric.setText(intent.getStringExtra("matric"))

        // Save button
        binding.saveButton.setOnClickListener {
            saveProfile()
        }
    }

    private fun saveProfile() {
        val name = binding.editName.text.toString().trim()
        val matric = binding.editMatric.text.toString().trim()
        val userId = auth.currentUser?.uid

        if (name.isEmpty() || matric.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val data = hashMapOf(
            "name" to name,
            "matric" to matric
        )

        db.collection("users")
            .document(userId)
            .set(data, com.google.firebase.firestore.SetOptions.merge())
            .addOnSuccessListener {

                // Return updated data to MePage
                val resultIntent = Intent()
                resultIntent.putExtra("name", name)
                resultIntent.putExtra("matric", matric)
                setResult(Activity.RESULT_OK, resultIntent)

                Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save profile", Toast.LENGTH_SHORT).show()
            }
    }
}