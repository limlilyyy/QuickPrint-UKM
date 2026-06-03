package com.example.quickprintukm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderSimpleAdapter(
    private val orders: List<String>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<OrderSimpleAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = view.findViewById(android.R.id.text1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(android.R.layout.simple_list_item_1, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val orderId = orders[position]
        holder.text.text = "Order ID: $orderId"
        holder.itemView.setOnClickListener { onClick(orderId) }
    }

    override fun getItemCount() = orders.size
}
