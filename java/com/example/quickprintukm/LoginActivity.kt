package com.example.quickprintukm

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.example.quickprintukm.databinding.ActivityLogInBinding
import androidx.appcompat.app.AppCompatActivity

import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        binding.createAccountTv.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.button.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passEt.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {

                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        if (email == "qimi@gmail.com" && pass == "abc123") {
                            startActivity(Intent(this, StaffDashboardActivity::class.java))
                        } else if (email == "staff@gmail.com" && pass == "abc123") {
                            startActivity(Intent(this, ManagementMainActivity::class.java))
                        }else {
                            startActivity(Intent(this, MainPageActivity::class.java))
                        }
                        finish()
                    }
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

            }
        }
    }


}