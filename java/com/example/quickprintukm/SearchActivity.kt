package com.example.quickprintukm

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class SearchActivity : AppCompatActivity() {

    private lateinit var layoutResults: LinearLayout
    private lateinit var searchBox: EditText
    private lateinit var searchButton: ImageView
    private lateinit var backButton: ImageView

    // 🟣 List of available locations (sample data)
    private val locations = listOf(
        LocationItem("MyPRINT UKM", "UKM, Kolej Tun Hussein Onn, 43600 Bangi, Selangor", "3.4 km"),
        LocationItem("Kolej Pendeta Zaba", "Block D", "1.5 km"),
        LocationItem("Pusanika Print Centre", "Pusanika UKM", "2.0 km"),
        LocationItem("Kolej Keris Mas", "Ground Floor", "2.8 km"),
        LocationItem("FTSM", "Block E", "3.1 km"),
        LocationItem("FKAB", "Block a", "3.7 km"),
        LocationItem("Kolej Burhanuddin Helmi", "Block a", "2.1 km"),
        LocationItem("Kolej Rahim Kajai", "Block B", "2.5 km"),
        LocationItem("Kolej Aminuddin Baki", "Block D", "2.0 km"),
        LocationItem("Kolej Ibrahim Yakub", "Block F", "1.5 km"),
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        // 🔹 Initialize views
        layoutResults = findViewById(R.id.layoutResults)
        searchBox = findViewById(R.id.etSearch)
        searchButton = findViewById(R.id.searchConfirmBtn)
        backButton = findViewById(R.id.backButton)

        // 🔹 Go back button
        backButton.setOnClickListener {
            finish()
        }

        // 🔹 Show all locations initially
        showResults(locations)

        // 🔹 When user clicks search icon
        searchButton.setOnClickListener {
            val query = searchBox.text.toString().trim()
            performSearch(query)
        }

        // 🔹 Also search while typing
        searchBox.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                performSearch(s.toString())
            }
        })
    }

    // 🔹 Function to filter locations
    private fun performSearch(query: String) {
        val filteredList = if (query.isEmpty()) {
            locations
        } else {
            locations.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.address.contains(query, ignoreCase = true)
            }
        }

        showResults(filteredList)
    }

    // 🔹 Function to show locations dynamically
    private fun showResults(list: List<LocationItem>) {
        layoutResults.removeAllViews()

        if (list.isEmpty()) {
            val emptyText = TextView(this).apply {
                text = "No results found."
                textSize = 16f
                setPadding(8, 16, 8, 16)
            }
            layoutResults.addView(emptyText)
            return
        }

        for (location in list) {
            val itemView = layoutInflater.inflate(R.layout.activity_itemlocation, layoutResults, false)

            val tvName = itemView.findViewById<TextView>(R.id.tvName)
            val tvAddress = itemView.findViewById<TextView>(R.id.tvAddress)
            val tvDistance = itemView.findViewById<TextView>(R.id.tvDistance)

            tvName.text = location.name
            tvAddress.text = location.address
            tvDistance.text = location.distance

            // Set hover/pressed background
            itemView.setBackgroundResource(R.drawable.item_selected_bg)

            // 🔹 Return the selected location
            itemView.setOnClickListener {
                val intent = Intent()
                intent.putExtra("selectedLocationName", location.name)
                intent.putExtra("selectedLocationAddress", location.address)
                setResult(RESULT_OK, intent)
                finish()
            }

            layoutResults.addView(itemView)
        }
    }

    // 🔹 Simple data model
    data class LocationItem(val name: String, val address: String, val distance: String)
}
