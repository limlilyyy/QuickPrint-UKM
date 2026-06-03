package com.example.quickprintukm

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class PickUpActivity : AppCompatActivity() {

    private lateinit var tvSettings: TextView
    private lateinit var tvPrice: TextView
    private lateinit var layoutPickup: LinearLayout
    private lateinit var layoutDelivery: LinearLayout
    private lateinit var tvPickup: TextView
    private lateinit var tvDelivery: TextView
    private lateinit var tvDeliveryAddress: TextView
    private lateinit var btnSearchAddress: ImageView
    private lateinit var tvTotalPrice: TextView
    private lateinit var btnCheckout: TextView

    private val DELIVERY_FEE = 5.0

    private var orderId: String = ""
    private var basePrice = 0.0
    private var finalPrice = 0.0
    private var collectOption = "Pickup"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pickup)

        orderId = intent.getStringExtra("ORDER_ID") ?: ""

        tvSettings = findViewById(R.id.tvSettings)
        tvPrice = findViewById(R.id.tvPrice)
        tvPickup = findViewById(R.id.tvPickup)
        tvDelivery = findViewById(R.id.tvDelivery)
        layoutPickup = findViewById(R.id.layoutPickup)
        layoutDelivery = findViewById(R.id.layoutDelivery)
        tvDeliveryAddress = findViewById(R.id.tvDeliveryAddress)
        btnSearchAddress = findViewById(R.id.btnSearchAddress)
        tvTotalPrice = findViewById(R.id.tvTotalPrice)
        btnCheckout = findViewById(R.id.btnCheckout)

        val backArrow = findViewById<ImageView>(R.id.back_arrow)
        val rbReward = findViewById<RadioButton>(R.id.rbReward)
        val tvEdit = findViewById<TextView>(R.id.tvEdit)

        tvEdit.setOnClickListener {
            val intent = Intent(this, PrintNowActivity::class.java)
            intent.putExtra("ORDER_ID", orderId)
            startActivity(intent)
            finish()
        }

        backArrow.setOnClickListener {
            val intent = Intent(this, PrintNowActivity::class.java)
            intent.putExtra("ORDER_ID", orderId)
            startActivity(intent)
            finish()
        }

        // Load order data
        loadOrder()

        // Default: Pickup
        selectPickup()

        tvPickup.setOnClickListener { selectPickup() }
        tvDelivery.setOnClickListener { selectDelivery() }

        btnSearchAddress.setOnClickListener {
            startActivityForResult(
                Intent(this, SearchActivity::class.java),
                1001
            )
        }

        rbReward.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(this, "Reward points applied", Toast.LENGTH_SHORT).show()
            }
        }

        btnCheckout.setOnClickListener {
            updateOrderAndProceed()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadOrder() {
        FirebaseFirestore.getInstance()
            .collection("orders")
            .document(orderId)
            .get()
            .addOnSuccessListener { doc ->
                if (!doc.exists()) return@addOnSuccessListener

                val paper = doc.getString("paperSize") ?: "A4"
                val color = doc.getString("color") ?: "Black & White"
                val orientation = doc.getString("orientation") ?: "Portrait"
                val copies = doc.getLong("copies") ?: 1
                val pages = doc.getLong("pageCount") ?: 1
                val price = doc.getDouble("totalPrice") ?: 0.0

                // ✅ Update UI
                tvSettings.text =
                    "$paper • $color • $orientation • ${pages} Pages • ${copies} Copies"

                tvPrice.text = "RM %.2f".format(price)

                basePrice = price
                finalPrice = basePrice
                updatePriceUI()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load order", Toast.LENGTH_SHORT).show()
            }
    }



    private fun selectPickup() {
        collectOption = "Pickup"
        finalPrice = basePrice

        layoutPickup.visibility = View.VISIBLE
        layoutDelivery.visibility = View.GONE

        tvPickup.setBackgroundResource(R.drawable.tab_selected_bg)
        tvDelivery.setBackgroundResource(R.drawable.tab_unselected_bg)

        updatePriceUI()
    }

    private fun selectDelivery() {
        collectOption = "Delivery"
        finalPrice = basePrice + DELIVERY_FEE

        layoutDelivery.visibility = View.VISIBLE
        layoutPickup.visibility = View.GONE

        tvDelivery.setBackgroundResource(R.drawable.tab_selected_bg)
        tvPickup.setBackgroundResource(R.drawable.tab_unselected_bg)

        updatePriceUI()
    }

    private fun updatePriceUI() {
        tvTotalPrice.text = "RM %.2f".format(finalPrice)
    }

    private fun updateOrderAndProceed() {
        FirebaseFirestore.getInstance()
            .collection("orders")
            .document(orderId)
            .update(
                mapOf(
                    "collectOption" to collectOption,
                    "totalPrice" to finalPrice
                )
            )
            .addOnSuccessListener {
                val intent = Intent(this, PaymentActivity::class.java)
                intent.putExtra("ORDER_ID", orderId)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update order", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001 && resultCode == RESULT_OK) {
            val address = data?.getStringExtra("selectedLocationAddress")
            val name = data?.getStringExtra("selectedLocationName")

            if (!address.isNullOrEmpty() && !name.isNullOrEmpty()) {
                selectDelivery()
                tvDeliveryAddress.text = "$name, $address"
            }
        }
    }
}
