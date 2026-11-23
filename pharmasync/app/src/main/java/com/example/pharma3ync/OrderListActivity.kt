package com.example.pharma3ync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class OrderListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: OrderAdapter

    private var pendingOrders = mutableListOf<Pair<LocalOrder, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)

        recyclerView = findViewById(R.id.recyclerViewOrders)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadOrders()
        adapter = OrderAdapter()
        recyclerView.adapter = adapter
    }

    private fun loadOrders() {
        pendingOrders.clear()
        val allPending = MockDatabase.orders.filter { it.status == "pending" }

        for (order in allPending) {
            val medName = MockDatabase.medicines.find { it.id == order.medicineId }?.name ?: "Unknown"
            pendingOrders.add(Pair(order, medName))
        }
    }

    private fun markOrderAsDoneAndUpdateStock(order: LocalOrder) {

        val med = MockDatabase.medicines.find { it.id == order.medicineId }
        med?.let {
            it.quantity += order.quantity
        }


        MockDatabase.orders.remove(order) // Removing it since we only show pending

        Toast.makeText(this, "Stock updated!", Toast.LENGTH_SHORT).show()
        loadOrders()
        adapter.notifyDataSetChanged()
    }

    private inner class OrderAdapter : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
        inner class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val name: TextView = view.findViewById(R.id.textViewOrderMedName)
            val qty: TextView = view.findViewById(R.id.textViewOrderQuantity)
            val btnDone: Button = view.findViewById(R.id.buttonMarkOrdered)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
            return OrderViewHolder(view)
        }

        override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
            val (order, medName) = pendingOrders[position]
            holder.name.text = medName
            holder.qty.text = "Quantity: ${order.quantity}"
            holder.btnDone.setOnClickListener { markOrderAsDoneAndUpdateStock(order) }
        }

        override fun getItemCount(): Int = pendingOrders.size
    }
}