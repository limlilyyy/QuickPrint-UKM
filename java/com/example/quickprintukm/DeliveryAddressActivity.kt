package com.example.quickprintukm

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DeliveryAddressActivity : AppCompatActivity() {

    private lateinit var savedContainer: LinearLayout
    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_address)

        val btnAdd = findViewById<View>(R.id.btnAddAddress)
        val form = findViewById<View>(R.id.addressForm)

        val name = findViewById<EditText>(R.id.inputName)
        val phone = findViewById<EditText>(R.id.inputPhone)
        val address = findViewById<EditText>(R.id.inputAddress)
        val city = findViewById<EditText>(R.id.inputCity)
        val state = findViewById<EditText>(R.id.inputState)
        val postcode = findViewById<EditText>(R.id.inputPostcode)
        val btnSave = findViewById<View>(R.id.btnSaveAddress)

        savedContainer = findViewById(R.id.savedAddressContainer)

        // Load saved addresses
        loadAddresses()

        btnAdd.setOnClickListener {
            form.visibility = View.VISIBLE
        }

        btnSave.setOnClickListener {

            if (userId == null) {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val data = hashMapOf(
                "name" to name.text.toString(),
                "phone" to phone.text.toString(),
                "address" to address.text.toString(),
                "city" to city.text.toString(),
                "state" to state.text.toString(),
                "postcode" to postcode.text.toString()
            )

            db.collection("users")
                .document(userId)
                .collection("addresses")
                .add(data)
                .addOnSuccessListener {
                    Toast.makeText(this, "Address saved", Toast.LENGTH_SHORT).show()
                    addAddressView(data)
                    form.visibility = View.GONE

                    name.text.clear()
                    phone.text.clear()
                    address.text.clear()
                    city.text.clear()
                    state.text.clear()
                    postcode.text.clear()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                }

        }

        findViewById<View>(R.id.arrow).setOnClickListener {
            finish()
        }
    }

    private fun loadAddresses() {
        if (userId == null) return

        db.collection("users")
            .document(userId)
            .collection("addresses")
            .get()
            .addOnSuccessListener { documents ->
                for (doc in documents) {
                    addAddressView(doc.data)
                }
            }
    }

    private fun addAddressView(data: Map<String, Any>) {
        val textView = TextView(this)
        textView.text = """
            ${data["name"]}
            ${data["phone"]}
            ${data["address"]},
             ${data["city"]},
              ${data["state"]}
            ${data["postcode"]}
        """.trimIndent()

        textView.textSize = 16f
        textView.setPadding(24, 24, 24, 24)
        textView.setBackgroundResource(R.drawable.rounded_corner)

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 16, 0, 16)
        textView.layoutParams = params

        savedContainer.addView(textView)
    }
}

