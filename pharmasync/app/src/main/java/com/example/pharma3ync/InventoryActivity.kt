package com.example.pharma3ync

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pharma3ync.databinding.ActivityInventoryBinding
import java.util.*

class InventoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInventoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        loadMedicines()

        binding.addButton.setOnClickListener { showAddDialog() }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = MedicineAdapter(
            onEditClick = { medMap ->
                // Convert Map back to LocalMedicine to edit
                val id = medMap["id"].toString()
                val medObj = MockDatabase.medicines.find { it.id == id }
                if(medObj != null) showEditDialog(medObj)
            },
            onDeleteClick = { docId -> confirmDelete(docId) }
        )
    }

    private fun loadMedicines() {
        // Convert our LocalMedicine objects to the Map format your Adapter expects
        val mapList = MockDatabase.medicines.map { med ->
            mapOf(
                "id" to med.id,
                "name" to med.name,
                "form" to med.form,
                "quantity" to med.quantity,
                "shelf" to med.shelf,
                "strength" to med.strength,
                "price" to med.price
            )
        }
        (binding.recyclerView.adapter as MedicineAdapter).updateList(mapList)
    }

    private fun showAddDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_medicine, null)
        AlertDialog.Builder(this)
            .setTitle("Add New Medicine")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                saveMedicine(
                    form = dialogView.findViewById<EditText>(R.id.editForm).text.toString(),
                    name = dialogView.findViewById<EditText>(R.id.editName).text.toString(),
                    quantity = dialogView.findViewById<EditText>(R.id.editQuantity).text.toString(),
                    shelf = dialogView.findViewById<EditText>(R.id.editShelf).text.toString(),
                    strength = dialogView.findViewById<EditText>(R.id.editStrength).text.toString(),
                    price = dialogView.findViewById<EditText>(R.id.editPrice).text.toString()
                )
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun saveMedicine(form: String, name: String, quantity: String, shelf: String, strength: String, price: String) {
        val newMed = LocalMedicine(
            id = UUID.randomUUID().toString(),
            name = name,
            form = form,
            quantity = quantity.toIntOrNull() ?: 0,
            shelf = shelf,
            strength = strength,
            price = price.toDoubleOrNull() ?: 0.0
        )
        MockDatabase.medicines.add(newMed)
        loadMedicines()
        Toast.makeText(this, "Added!", Toast.LENGTH_SHORT).show()
    }

    private fun showEditDialog(med: LocalMedicine) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_medicine, null).apply {
            findViewById<EditText>(R.id.editForm).setText(med.form)
            findViewById<EditText>(R.id.editName).setText(med.name)
            findViewById<EditText>(R.id.editQuantity).setText(med.quantity.toString())
            findViewById<EditText>(R.id.editShelf).setText(med.shelf)
            findViewById<EditText>(R.id.editStrength).setText(med.strength)
            findViewById<EditText>(R.id.editPrice).setText(med.price.toString())
        }

        AlertDialog.Builder(this)
            .setTitle("Edit Medicine")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                med.form = dialogView.findViewById<EditText>(R.id.editForm).text.toString()
                med.name = dialogView.findViewById<EditText>(R.id.editName).text.toString()
                med.quantity = dialogView.findViewById<EditText>(R.id.editQuantity).text.toString().toInt()
                med.shelf = dialogView.findViewById<EditText>(R.id.editShelf).text.toString()
                med.strength = dialogView.findViewById<EditText>(R.id.editStrength).text.toString()
                med.price = dialogView.findViewById<EditText>(R.id.editPrice).text.toString().toDouble()

                loadMedicines()
                Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun confirmDelete(id: String) {
        MockDatabase.medicines.removeAll { it.id == id }
        loadMedicines()
        Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show()
    }
}