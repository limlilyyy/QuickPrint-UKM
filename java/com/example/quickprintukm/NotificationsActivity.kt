package com.example.quickprintukm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quickprintukm.databinding.ActivityNotificationsBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class NotificationsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private val notificationList = mutableListOf<NotificationItem>()
    private lateinit var adapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeButton.setOnClickListener {
            startActivity(Intent(this, MainPageActivity::class.java))
            overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left)
        }
        binding.activityButton.setOnClickListener {
            startActivity(Intent(this, ActivityPageActivity::class.java))
            overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left)
        }
        binding.meButton.setOnClickListener {
            startActivity(Intent(this, MePageActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        recyclerView = findViewById(R.id.recyclerViewNotifications)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = NotificationAdapter(notificationList)
        recyclerView.adapter = adapter

        loadNotifications()
    }

    private fun loadNotifications() {
        FirebaseFirestore.getInstance()
            .collection("notifications")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                notificationList.clear()

                for (doc in result) {
                    val orderId = doc.getString("orderId") ?: continue
                    val message = doc.getString("message") ?: ""
                    val timestamp = doc.getLong("timestamp") ?: 0L

                    notificationList.add(
                        NotificationItem(
                            id = doc.id,
                            orderId = orderId,
                            message = message,
                            timestamp = timestamp
                        )
                    )
                }
                adapter.notifyDataSetChanged()
            }
    }
}
