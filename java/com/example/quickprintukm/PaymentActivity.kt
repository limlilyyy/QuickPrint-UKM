package com.example.quickprintukm

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.firebase.firestore.FirebaseFirestore

class PaymentActivity : AppCompatActivity() {

    private lateinit var backArrow: ImageView
    private lateinit var cardUpload: CardView
    private lateinit var tvRepFileName: TextView

    private var selectedReceiptUri: Uri? = null

    private val pickReceiptLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedReceiptUri = it
                val fileName = it.lastPathSegment ?: "Receipt File"
                tvRepFileName.text = fileName
                Toast.makeText(this, "File selected: $fileName", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        backArrow = findViewById(R.id.back_arrow)
        cardUpload = findViewById(R.id.cardUpload)
        tvRepFileName = findViewById(R.id.tvRepFileName)

        backArrow.setOnClickListener {
            finish()
        }

        cardUpload.setOnClickListener {
            pickReceiptLauncher.launch("*/*")
        }

        val btnDone = findViewById<TextView>(R.id.btnDone)
        btnDone.setOnClickListener {

            val orderId = intent.getStringExtra("ORDER_ID")
            if (orderId.isNullOrEmpty()) {
                Toast.makeText(this, "Order ID missing", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val receiptName = tvRepFileName.text.toString()

            if (receiptName == "No file selected" || receiptName.isEmpty()) {
                Toast.makeText(this, "Please upload receipt", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = FirebaseFirestore.getInstance()

            // 1️⃣ Update order
            db.collection("orders").document(orderId)
                .update(
                    mapOf(
                        "status" to "Paid",
                        "receiptFileName" to receiptName
                    )
                )
                .addOnSuccessListener {

                    // 2️⃣ Create notification
                    val notification = hashMapOf(
                        "orderId" to orderId,
                        "message" to "Order successfully placed",
                        "timestamp" to System.currentTimeMillis()
                    )

                    db.collection("notifications")
                        .add(notification)
                        .addOnSuccessListener {

                            // ✅ Navigate ONLY after everything succeeds
                            startActivity(
                                Intent(this, NotificationsActivity::class.java)
                            )
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                this,
                                "Failed to create notification",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Failed to update order",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }


    }
}

