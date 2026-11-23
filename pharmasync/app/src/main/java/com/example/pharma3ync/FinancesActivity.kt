package com.example.pharma3ync

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class FinancesActivity : AppCompatActivity() {

    private lateinit var totalRevenueTextView: TextView
    private lateinit var mostSoldTextView: TextView
    private lateinit var employeeRevenueTextView: TextView
    private lateinit var filterDateEditText: EditText
    private lateinit var applyFilterButton: Button
    private lateinit var resetFilterButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finances)

        totalRevenueTextView = findViewById(R.id.totalRevenueTextView)
        mostSoldTextView = findViewById(R.id.mostSoldTextView)
        employeeRevenueTextView = findViewById(R.id.employeeRevenueTextView)
        filterDateEditText = findViewById(R.id.filterDateEditText)
        applyFilterButton = findViewById(R.id.applyFilterButton)
        resetFilterButton = findViewById(R.id.resetFilterButton)

        loadAllPurchases()

        filterDateEditText.setOnClickListener {
            showDatePicker()
        }

        applyFilterButton.setOnClickListener {
            val selectedDate = filterDateEditText.text.toString()
            if (selectedDate.isNotEmpty()) {
                loadFilteredPurchases(selectedDate)
            } else {
                Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show()
            }
        }

        resetFilterButton.setOnClickListener {
            filterDateEditText.text.clear()
            loadAllPurchases()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                filterDateEditText.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun loadAllPurchases() {
        updateFinanceData(MockDatabase.purchases)
    }

    private fun loadFilteredPurchases(date: String) {
        val filtered = MockDatabase.purchases.filter { it.date == date }
        updateFinanceData(filtered)
    }

    private fun updateFinanceData(purchaseList: List<LocalPurchase>) {
        var totalRevenue = 0.0
        val employeeRevenue = mutableMapOf<String, Double>()
        val medicineCount = mutableMapOf<String, Int>()

        for (purchase in purchaseList) {
            totalRevenue += purchase.price

            // Track revenue per employee
            // Try to find employee name, fallback to ID
            val empName = MockDatabase.employees.find { it.id == purchase.employeeId }?.name ?: "Unknown (${purchase.employeeId})"
            employeeRevenue[empName] = (employeeRevenue[empName] ?: 0.0) + purchase.price

            // Track most sold items
            medicineCount[purchase.medicineId] = (medicineCount[purchase.medicineId] ?: 0) + 1
        }

        // Find most sold medicine name
        val topMedId = medicineCount.maxByOrNull { it.value }?.key
        val topMedName = MockDatabase.medicines.find { it.id == topMedId }?.name ?: "N/A"
        mostSoldTextView.text = "Most Sold Item: $topMedName"

        // Format Employee Revenue List
        val empRevenueStr = employeeRevenue.entries.joinToString(separator = "\n") {
            "${it.key}: ${it.value} JD"
        }

        totalRevenueTextView.text = "Total Revenue: $totalRevenue JD"
        employeeRevenueTextView.text = empRevenueStr
    }
}