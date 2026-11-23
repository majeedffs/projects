package com.example.pharma3ync

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MedicineAdapter(
    private val onEditClick: (Map<String, Any>) -> Unit = {},
    private val onDeleteClick: (String) -> Unit = {}
) : RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder>() {

    private var medicines: List<Map<String, Any>> = emptyList()

    inner class MedicineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val formTextView: TextView = itemView.findViewById(R.id.medicineForm)
        val nameTextView: TextView = itemView.findViewById(R.id.medicineName)
        val quantityTextView: TextView = itemView.findViewById(R.id.medicineQuantity)
        val shelfTextView: TextView = itemView.findViewById(R.id.medicineShelf)
        val strengthTextView: TextView = itemView.findViewById(R.id.medicineStrength)
        val editButton: View = itemView.findViewById(R.id.editButton)
        val deleteButton: View = itemView.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicineViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_medicine, parent, false)
        return MedicineViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicineViewHolder, position: Int) {
        val medicine = medicines[position]

        holder.formTextView.text = "Form: ${medicine["form"]}"
        holder.nameTextView.text = medicine["name"].toString()
        holder.quantityTextView.text = "Qty: ${medicine["quantity"]}"
        holder.shelfTextView.text = "Shelf: ${medicine["shelf"]}"
        holder.strengthTextView.text = "Strength: ${medicine["strength"]}"

        holder.editButton.setOnClickListener { onEditClick(medicine) }
        holder.deleteButton.setOnClickListener { onDeleteClick(medicine["id"].toString()) }
    }

    override fun getItemCount() = medicines.size

    fun updateList(newList: List<Map<String, Any>>) {
        medicines = newList
        notifyDataSetChanged()
    }
}