package com.example.quickprintukm

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationAdapter(
    private val notifList: List<NotificationItem>
) : RecyclerView.Adapter<NotificationAdapter.NotifViewHolder>() {

    class NotifViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTime: TextView = itemView.findViewById(R.id.txtTime)
        val txtMessage: TextView = itemView.findViewById(R.id.txtMessage)
        val imgNotification: ImageView = itemView.findViewById(R.id.imgNotification)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification_card, parent, false)
        return NotifViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotifViewHolder, position: Int) {
        val notif = notifList[position]
        holder.txtMessage.text = notif.message
        holder.txtTime.text = SimpleDateFormat("dd MMM HH:mm", Locale.getDefault())
            .format(Date(notif.timestamp))

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, OrderHistoryActivity::class.java)
            intent.putExtra("ORDER_ID", notif.orderId)
            holder.itemView.context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int = notifList.size
}


