package com.example.quickprintukm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quickprintukm.databinding.ActivityStaffOrderListBinding
import com.google.firebase.firestore.FirebaseFirestore

class StaffOrderListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStaffOrderListBinding
    private val db = FirebaseFirestore.getInstance()
    private val orderList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStaffOrderListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // RecyclerView setup
        binding.orderRecycler.layoutManager = LinearLayoutManager(this)

        val adapter = OrderSimpleAdapter(orderList) { orderId ->
            val intent = Intent(this, OrderDetailsActivity::class.java)
            intent.putExtra("ORDER_ID", orderId)
            startActivity(intent)
        }

        binding.orderRecycler.adapter = adapter

        // Load orders from Firestore
        db.collection("orders")
            .get()
            .addOnSuccessListener { result ->
                orderList.clear()
                for (doc in result) {
                    orderList.add(doc.id)
                }
                adapter.notifyDataSetChanged()
            }

        // Segment buttons (UI only)
        binding.startPrintBtn.setOnClickListener {
            binding.startPrintBtn.alpha = 1f
            binding.finishPrintBtn.alpha = 0.5f
        }

        binding.finishPrintBtn.setOnClickListener {
            binding.startPrintBtn.alpha = 0.5f
            binding.finishPrintBtn.alpha = 1f
        }

        binding.startPrintBtn.performClick()

        // Bottom navigation
        binding.homeButton.setOnClickListener {
            startActivity(Intent(this, StaffDashboardActivity::class.java))
            finish()
        }

        binding.dashboardButton.setOnClickListener {
            // already here
        }

        binding.meButton.setOnClickListener {
            startActivity(Intent(this, StaffProfileActivity::class.java))
        }
    }
}
