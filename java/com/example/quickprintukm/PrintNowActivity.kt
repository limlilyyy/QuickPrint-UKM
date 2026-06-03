package com.example.quickprintukm

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.quickprintukm.databinding.ActivityPrintNowBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PrintNowActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrintNowBinding

    private var pageCount = 1
    private var copiesCount = 1
    private var selectedOrientation = "Landscape"
    private var selectedSize = "A4"
    private var selectedColor = "Black & White"
    private var totalPrice = 0.0

    private var selectedFileUri: Uri? = null
    private var selectedFileName = ""

    private val pickFileLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                selectedFileUri = it
                selectedFileName = it.lastPathSegment ?: "Unknown File"
                binding.tvFileName.text = selectedFileName
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrintNowBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backArrow.setOnClickListener { finish() }

        // Page input
        binding.etPageCount.addTextChangedListener {
            val value = it.toString().toIntOrNull()

            pageCount = if (value != null && value >= 1) value else 1

            if (value == null || value < 1) {
                binding.etPageCount.setText("1")
                binding.etPageCount.setSelection(1)
            }

            calculatePrice()
        }


        binding.cardUpload.setOnClickListener {
            pickFileLauncher.launch("*/*")
        }

        binding.btnAdd.setOnClickListener {
            copiesCount++
            binding.tvCopiesCount.text = copiesCount.toString()
            calculatePrice()
        }

        binding.btnMinus.setOnClickListener {
            if (copiesCount > 1) {
                copiesCount--
                binding.tvCopiesCount.text = copiesCount.toString()
                calculatePrice()
            }
        }

        setupSpinners()
        calculatePrice()

        binding.btnNext.setOnClickListener {
            submitOrder()
        }
    }

    private fun calculatePrice() {
        val pricePerPage = if (selectedColor == "Color") 0.5 else 0.2
        totalPrice = pageCount * copiesCount * pricePerPage
    }

    private fun submitOrder() {
        if (selectedFileName.isEmpty()) {
            Toast.makeText(this, "Please select a file", Toast.LENGTH_SHORT).show()
            return
        }

        saveOrderToFirestore()
    }

    private fun saveOrderToFirestore() {
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val db = FirebaseFirestore.getInstance()
        val orderId = db.collection("orders").document().id

        val order = Order(
            orderId = orderId,
            userId = user.uid,
            fileName = selectedFileName,
            pageCount = pageCount,
            copies = copiesCount,
            orientation = selectedOrientation,
            paperSize = selectedSize,
            color = selectedColor,
            totalPrice = totalPrice,
            collectOption = "Pickup"
        )

        db.collection("orders").document(orderId)
            .set(order)
            .addOnSuccessListener {
                val intent = Intent(this, PickUpActivity::class.java)
                intent.putExtra("ORDER_ID", orderId)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
    }

    private fun setupSpinners() {
        setupSpinner(binding.spinnerOrientation, listOf("Portrait", "Landscape")) {
            selectedOrientation = it
        }

        setupSpinner(binding.spinnerSize, listOf("A5", "A4", "A3")) {
            selectedSize = it
        }

        setupSpinner(binding.spinnerColor, listOf("Color", "Black & White")) {
            selectedColor = it
            calculatePrice()
        }
    }

    private fun setupSpinner(
        spinner: android.widget.Spinner,
        items: List<String>,
        onSelect: (String) -> Unit
    ) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                onSelect(items[position])
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}
