package com.example.quickprintukm

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quickprintukm.databinding.ActivityRewardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
class RewardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRewardBinding
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private var currentPoints = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRewardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backArrow.setOnClickListener { finish() }

        val userId = auth.currentUser?.uid ?: return

        loadUserData(userId)

        binding.btnCheckIn.setOnClickListener {
            checkIn(userId)
        }

        binding.btnRedeem1.setOnClickListener {
            redeemVoucher(userId, 1)
        }

        binding.btnRedeem2.setOnClickListener {
            redeemVoucher(userId, 2)
        }
    }

    // 🔹 Load points + voucher status
    private fun loadUserData(userId: String) {
        val userRef = db.collection("users").document(userId)

        userRef.get().addOnSuccessListener { doc ->
            if (!doc.exists()) {
                userRef.set(
                    mapOf(
                        "points" to 100,
                        "lastCheckIn" to "",
                        "voucher1Redeemed" to false,
                        "voucher2Redeemed" to false
                    )
                )
                currentPoints = 100
                binding.txtPoints.text = "100"
                return@addOnSuccessListener
            }

            currentPoints = doc.getLong("points")?.toInt() ?: 0
            binding.txtPoints.text = currentPoints.toString()

            // Hide redeemed vouchers
            if (doc.getBoolean("voucher1Redeemed") == true) {
                binding.voucherLayout1.visibility = View.GONE
            }

            if (doc.getBoolean("voucher2Redeemed") == true) {
                binding.voucherLayout2.visibility = View.GONE
            }

            val today = getTodayDate()
            if (doc.getString("lastCheckIn") == today) {
                binding.btnCheckIn.isEnabled = false
                binding.btnCheckIn.text = "Checked In ✔"
            }
        }
    }

    // 🔹 Redeem voucher logic
    private fun redeemVoucher(userId: String, voucherNumber: Int) {

        if (currentPoints < 100) {
            Toast.makeText(this, "Not enough points", Toast.LENGTH_SHORT).show()
            return
        }

        val userRef = db.collection("users").document(userId)
        val voucherField = if (voucherNumber == 1) "voucher1Redeemed" else "voucher2Redeemed"

        userRef.get().addOnSuccessListener { doc ->
            if (doc.getBoolean(voucherField) == true) {
                Toast.makeText(this, "Voucher already redeemed", Toast.LENGTH_SHORT).show()
                return@addOnSuccessListener
            }

            val newPoints = currentPoints - 100

            userRef.update(
                mapOf(
                    "points" to newPoints,
                    voucherField to true
                )
            ).addOnSuccessListener {
                currentPoints = newPoints
                binding.txtPoints.text = newPoints.toString()

                if (voucherNumber == 1) {
                    binding.voucherLayout1.visibility = View.GONE
                } else {
                    binding.voucherLayout2.visibility = View.GONE
                }

                Toast.makeText(this, "Voucher redeemed successfully!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 🔹 Daily check-in
    private fun checkIn(userId: String) {
        val userRef = db.collection("users").document(userId)
        val today = getTodayDate()

        userRef.get().addOnSuccessListener { doc ->
            if (doc.getString("lastCheckIn") == today) return@addOnSuccessListener

            val newPoints = currentPoints + 1

            userRef.update(
                mapOf(
                    "points" to newPoints,
                    "lastCheckIn" to today
                )
            ).addOnSuccessListener {
                currentPoints = newPoints
                binding.txtPoints.text = newPoints.toString()
                binding.btnCheckIn.isEnabled = false
                binding.btnCheckIn.text = "Checked In ✔"
            }
        }
    }

    private fun getTodayDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }
}
