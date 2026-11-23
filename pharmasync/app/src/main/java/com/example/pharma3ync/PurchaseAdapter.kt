package com.example.pharma3ync

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class PurchaseAdapter(private val purchases: List<Purchase>) :
    RecyclerView.Adapter<PurchaseAdapter.PurchaseViewHolder>() {

    class PurchaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val customerText: TextView = itemView.findViewById(R.id.customerText)
        val detailsText: TextView = itemView.findViewById(R.id.detailsText)
        val dateText: TextView = itemView.findViewById(R.id.dateText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_purchase, parent, false)
        return PurchaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: PurchaseViewHolder, position: Int) {
        val purchase = purchases[position]

        holder.customerText.text = "Customer: ${purchase.customerName}"


        holder.detailsText.text = "Medicine: ${purchase.medicineId} (${purchase.quantity} units)"
        holder.dateText.text = SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault()).format(purchase.timestamp)
    }

    override fun getItemCount() = purchases.size
}