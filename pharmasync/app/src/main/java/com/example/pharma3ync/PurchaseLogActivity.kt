package com.example.pharma3ync

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pharma3ync.databinding.ActivityPurchaseLogBinding
import java.text.SimpleDateFormat
import java.util.*

class PurchaseLogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPurchaseLogBinding
    private lateinit var employeeId: String
    private var selectedMedicineId: String = ""
    private var availableQuantity: Int = 0
    private val medicineMap = mutableMapOf<String, Pair<String, Int>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPurchaseLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        employeeId = intent.getStringExtra("employeeId") ?: "emp_default"
        loadAvailableMedicines()

        binding.completePurchaseButton.setOnClickListener {
            processPurchase()
        }
    }

    private fun loadAvailableMedicines() {
        val availableMeds = MockDatabase.medicines.filter { it.quantity > 0 }

        if (availableMeds.isEmpty()) {
            showNoStockAvailable()
        } else {
            val medicineNames = mutableListOf<String>()
            medicineMap.clear()

            for (med in availableMeds) {
                medicineNames.add(med.name)
                medicineMap[med.name] = Pair(med.id, med.quantity)
            }
            setupMedicineSpinner(medicineNames)
        }
    }

    private fun showNoStockAvailable() {
        binding.apply {
            noStockText.visibility = View.VISIBLE
            medicineSpinner.visibility = View.GONE
            quantityInput.visibility = View.GONE
            customerNameInput.visibility = View.GONE
            completePurchaseButton.visibility = View.GONE
        }
    }

    private fun setupMedicineSpinner(medicineNames: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, medicineNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.medicineSpinner.adapter = adapter

        binding.medicineSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedName = medicineNames.getOrNull(position) ?: return
                medicineMap[selectedName]?.let { (id, quantity) ->
                    selectedMedicineId = id
                    availableQuantity = quantity
                    binding.quantityInput.hint = "Max: $quantity"
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun processPurchase() {
        val quantityStr = binding.quantityInput.text.toString()
        val quantity = quantityStr.toIntOrNull() ?: 0
        val customerName = binding.customerNameInput.text.toString().trim()

        when {
            selectedMedicineId.isEmpty() -> Toast.makeText(this, "Please select a medicine", Toast.LENGTH_SHORT).show()
            quantity <= 0 -> Toast.makeText(this, "Please enter a valid quantity", Toast.LENGTH_SHORT).show()
            quantity > availableQuantity -> Toast.makeText(this, "Quantity exceeds available stock", Toast.LENGTH_SHORT).show()
            customerName.isBlank() -> Toast.makeText(this, "Please enter customer name", Toast.LENGTH_SHORT).show()
            else -> showPriceInputDialog(customerName, binding.medicineSpinner.selectedItem.toString(), quantity)
        }
    }

    private fun showPriceInputDialog(customerName: String, medicineName: String, quantity: Int) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_price_input, null)
        val priceInput = dialogView.findViewById<android.widget.EditText>(R.id.editPrice)

        AlertDialog.Builder(this)
            .setTitle("Enter Price Per Unit")
            .setView(dialogView)
            .setPositiveButton("Complete Purchase") { _, _ ->
                val price = priceInput.text.toString().toDoubleOrNull() ?: 0.0
                if (price > 0) {
                    completePurchase(customerName, medicineName, quantity, price)
                } else {
                    Toast.makeText(this, "Please enter a valid price", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun completePurchase(customerName: String, medicineName: String, quantity: Int, price: Double) {
        binding.receiptTextView.visibility = View.GONE

        val currentDate = Date()
        val dateString = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate)


        val newPurchase = LocalPurchase(
            id = UUID.randomUUID().toString(),
            customerName = customerName,
            employeeId = employeeId,
            medicineId = selectedMedicineId,
            quantity = quantity,
            price = price,
            timestamp = currentDate,
            date = dateString
        )
        MockDatabase.purchases.add(newPurchase)


        val med = MockDatabase.medicines.find { it.id == selectedMedicineId }
        if (med != null) {
            med.quantity = med.quantity - quantity
        }


        printReceipt(customerName, medicineName, quantity, price)
        resetForm()
        loadAvailableMedicines()

        Toast.makeText(this, "Purchase Completed", Toast.LENGTH_SHORT).show()
    }

    private fun printReceipt(customerName: String, medicineName: String, quantity: Int, price: Double) {
        val vatRate = 0.15
        val subtotal = price * quantity
        val vatAmount = subtotal * vatRate
        val total = subtotal + vatAmount

        val receiptText = """
            PHARMACY RECEIPT
            --------------------------
            Date: ${SimpleDateFormat("yyyy-MM-dd HH:mm").format(Date())}
            Customer: $customerName
            Medicine: $medicineName
            Quantity: $quantity
            Unit Price: $${"%.2f".format(price)}
            --------------------------
            Subtotal: $${"%.2f".format(subtotal)}
            VAT (${(vatRate * 100).toInt()}%): $${"%.2f".format(vatAmount)}
            --------------------------
            TOTAL: $${"%.2f".format(total)}
            --------------------------
            Thank you for your purchase!
        """.trimIndent()

        binding.receiptTextView.text = receiptText
        binding.receiptTextView.visibility = View.VISIBLE
    }

    private fun resetForm() {
        binding.quantityInput.text?.clear()
        binding.customerNameInput.text?.clear()
    }
}