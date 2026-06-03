package com.example.quickprintukm

import android.os.Bundle
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RatingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        val back = findViewById<ImageView>(R.id.backArrow)
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val btnSubmit = findViewById<TextView>(R.id.btnNext)
        ratingBar.stepSize = 1f
        ratingBar.setIsIndicator(false)
        // Back button
        back.setOnClickListener { finish() }

        // Submit button
        btnSubmit.setOnClickListener {
            val rating = ratingBar.rating.toInt()
            if(rating == 0){
                Toast.makeText(this, "Please select a rating", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Toast.makeText(this, "Thanks! You rated: $rating star(s)", Toast.LENGTH_SHORT).show()

            // After submitting, close and return to Activity page
            finish()
        }
    }
}
