package com.example.quickprintukm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quickprintukm.databinding.ActivityActivityPageBinding

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ActivityPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity_page)

        val binding = ActivityActivityPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeButton.setOnClickListener {
            startActivity(Intent(this, MainPageActivity::class.java))
            overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left)
        }
        binding.notificationButton.setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
        binding.meButton.setOnClickListener {
            startActivity(Intent(this, MePageActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewActivity)
        recyclerView.layoutManager = LinearLayoutManager(this)

        FirebaseFirestore.getInstance()
            .collection("orders")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->

                val orderList = mutableListOf<Order>()

                for (doc in result) {
                    val timestamp = doc.getLong("timestamp") ?: 0L

                    orderList.add(
                        Order(
                            orderId = doc.id,
                            timestamp = timestamp
                        )
                    )
                }

                recyclerView.adapter = ActivityAdapter(orderList) { order ->
                    val intent = Intent(this, RatingActivity::class.java)
                    intent.putExtra("ORDER_ID", order.orderId)
                    startActivity(intent)
                }
            }
    }
}

