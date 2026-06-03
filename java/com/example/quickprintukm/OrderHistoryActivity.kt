package com.example.quickprintukm

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class OrderHistoryActivity : AppCompatActivity() {

    private lateinit var tvOrderId: TextView
    private lateinit var tvOrderDate: TextView
    private lateinit var tvCollectOptions: TextView
    private lateinit var tvReceipt: TextView
    private lateinit var tvPages: TextView
    private lateinit var tvCopies: TextView
    private lateinit var tvOrientation: TextView
    private lateinit var tvPaperSize: TextView
    private lateinit var tvColor: TextView
    private lateinit var tvFileName: TextView
    private lateinit var tv_price: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)

        tvOrderId = findViewById(R.id.tv_order_id)
        tvOrderDate = findViewById(R.id.tv_order_date)
        tvCollectOptions = findViewById(R.id.tv_collect_options)
        tvReceipt = findViewById(R.id.tv_receipt)
        tvPages = findViewById(R.id.tv_pages)
        tvCopies = findViewById(R.id.tv_copies)
        tvOrientation = findViewById(R.id.tv_orientation)
        tvPaperSize = findViewById(R.id.tv_paper_size)
        tvColor = findViewById(R.id.tv_color)
        tvFileName = findViewById(R.id.tvFileName)
        tv_price = findViewById(R.id.tv_price)

        findViewById<ImageView>(R.id.backArrow).setOnClickListener { finish() }
        findViewById<View>(R.id.back_btn).setOnClickListener { finish() }

        val orderId = intent.getStringExtra("ORDER_ID") ?: return
        loadOrderFromFirestore(orderId)
    }

    private fun loadOrderFromFirestore(orderId: String) {
        FirebaseFirestore.getInstance()
            .collection("orders")
            .document(orderId)
            .get()
            .addOnSuccessListener { doc ->
                if (!doc.exists()) return@addOnSuccessListener

                tvOrderId.text = orderId
                tvCollectOptions.text = doc.getString("collectOption") ?: "-"
                tvReceipt.text = doc.getString("receiptFileName") ?: "Not uploaded"
                tvFileName.text = doc.getString("fileName") ?: "Not uploaded"

                val totalPrice = doc.getDouble("totalPrice") ?: 0.0
                tv_price.text = "RM %.2f".format(totalPrice)

                val timestamp = doc.getLong("timestamp") ?: 0L
                tvOrderDate.text = SimpleDateFormat(
                    "dd MMM yyyy HH:mm",
                    Locale.getDefault()
                ).format(Date(timestamp))

                val pages = doc.getLong("pageCount") ?: 1
                val copies = doc.getLong("copies") ?: 1

                tvPages.text = "$pages pages"
                tvCopies.text = copies.toString()
                tvOrientation.text = doc.getString("orientation") ?: "-"
                tvPaperSize.text = doc.getString("paperSize") ?: "-"
                tvColor.text = doc.getString("color") ?: "-"
            }
    }

}
