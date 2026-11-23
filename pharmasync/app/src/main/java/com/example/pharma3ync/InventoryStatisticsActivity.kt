package com.example.pharma3ync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class InventoryStatisticsActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MedicineAdapter
    private val lowStockThreshold = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventorystatistics)

        recyclerView = findViewById(R.id.recyclerViewInventory)
        adapter = MedicineAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun showOrderDialog(medicine: LocalMedicine) {
        val input = EditText(this)
        input.inputType = android.text.InputType.TYPE_CLASS_NUMBER
        input.hint = "Enter quantity to order"

        AlertDialog.Builder(this)
            .setTitle("Order ${medicine.name}")
            .setView(input)
            .setPositiveButton("Confirm") { _, _ ->
                val qty = input.text.toString().toIntOrNull()
                if (qty != null && qty > 0) {
                    val newOrder = LocalOrder(
                        id = UUID.randomUUID().toString(),
                        medicineId = medicine.id,
                        quantity = qty,
                        status = "pending"
                    )
                    MockDatabase.orders.add(newOrder)
                    Toast.makeText(this, "Order placed!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Invalid quantity", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private inner class MedicineAdapter : RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder>() {
        inner class MedicineViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val name: TextView = view.findViewById(R.id.textViewMedName)
            val strength: TextView = view.findViewById(R.id.textViewStrength)
            val shelf: TextView = view.findViewById(R.id.textViewShelf)
            val quantity: TextView = view.findViewById(R.id.textViewQuantity)
            val order: Button = view.findViewById(R.id.buttonOrder)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_medicine1, parent, false)
            return MedicineViewHolder(view)
        }

        override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
            val med = MockDatabase.medicines[position]
            holder.name.text = med.name
            holder.strength.text = "Strength: ${med.strength}"
            holder.shelf.text = "Shelf: ${med.shelf}"
            holder.quantity.text = "Quantity: ${med.quantity}"

            val colorRes = if (med.quantity < lowStockThreshold) R.color.red_stock else R.color.green_stock


            holder.quantity.setTextColor(ContextCompat.getColor(this@InventoryStatisticsActivity, colorRes))

            if (med.quantity < lowStockThreshold) {
                holder.order.visibility = View.VISIBLE
                holder.order.setOnClickListener { showOrderDialog(med) }
            } else {
                holder.order.visibility = View.GONE
            }
        }

        override fun getItemCount(): Int = MockDatabase.medicines.size
    }
}