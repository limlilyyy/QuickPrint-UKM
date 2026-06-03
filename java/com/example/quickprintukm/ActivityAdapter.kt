package com.example.quickprintukm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class ActivityAdapter(
    private val items: List<Order>,
    private val onClick: (Order) -> Unit
) : RecyclerView.Adapter<ActivityAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtOrderId: TextView = view.findViewById(R.id.txtOrderId)
        val txtTime: TextView = view.findViewById(R.id.txtTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_simple, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = items[position]

        holder.txtOrderId.text = "Order ID: ${order.orderId}"
        holder.txtTime.text = SimpleDateFormat(
            "dd MMM yyyy, HH:mm",
            Locale.getDefault()
        ).format(Date(order.timestamp))

        holder.itemView.setOnClickListener {
            onClick(order)
        }
    }

    override fun getItemCount(): Int = items.size
}
