package com.example.quickprintukm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quickprintukm.databinding.ActivityOrderDetailsBinding
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class OrderDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOrderDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orderId = intent.getStringExtra("ORDER_ID")
        orderId?.let { loadOrder(it) }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Order Details"

        binding.finishPrintedBtn.setOnClickListener {
            orderId?.let { updateStatus(it) }
        }

        binding.backBtn.setOnClickListener { finish() }
    }

    private fun loadOrder(orderId: String) {
        FirebaseFirestore.getInstance()
            .collection("orders")
            .document(orderId)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    binding.tvOrderId.text = doc.getString("orderId")
                    binding.tvCollectOptions.text = doc.getString("collectOption")

                    val date = Date(doc.getLong("timestamp")!!)
                    val formatter = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
                    binding.tvOrderDate.text = formatter.format(date)
                }
            }
    }

    private fun updateStatus(orderId: String) {
        FirebaseFirestore.getInstance()
            .collection("orders")
            .document(orderId)
            .update("status", "Printed")
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
