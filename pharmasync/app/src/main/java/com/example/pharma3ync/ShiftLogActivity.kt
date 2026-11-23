package com.example.pharma3ync

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pharma3ync.databinding.ActivityShiftLogBinding
import java.text.SimpleDateFormat
import java.util.*

class ShiftLogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShiftLogBinding
    private lateinit var employeeId: String
    private val allPurchases = mutableListOf<Purchase>()
    private val filteredPurchases = mutableListOf<Purchase>()
    private lateinit var adapter: PurchaseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShiftLogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        employeeId = intent.getStringExtra("employeeId") ?: run {
            Toast.makeText(this, "Employee ID not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupRecyclerView()
        loadAllPurchases()

        binding.filterButton.setOnClickListener {
            showDatePicker()
        }

        binding.clearFilterButton.setOnClickListener {
            showAllPurchases()
        }
    }

    private fun setupRecyclerView() {
        adapter = PurchaseAdapter(filteredPurchases)
        binding.purchasesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.purchasesRecyclerView.adapter = adapter
    }

    private fun loadAllPurchases() {
        allPurchases.clear()


        val userPurchases = MockDatabase.purchases.filter { it.employeeId == employeeId }

        
        val sortedPurchases = userPurchases.sortedByDescending { it.timestamp }

        sortedPurchases.forEach { doc ->
            allPurchases.add(
                Purchase(
                    id = doc.id,
                    customerName = doc.customerName,
                    medicineId = doc.medicineId,
                    quantity = doc.quantity,
                    price = doc.price,
                    timestamp = doc.timestamp,
                    date = doc.date
                )
            )
        }
        showAllPurchases()
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, day ->
                val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, day)
                filterPurchasesByDate(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun filterPurchasesByDate(date: String) {
        filteredPurchases.clear()
        filteredPurchases.addAll(allPurchases.filter { it.date == date })
        updateUI()
        Toast.makeText(this, "Showing purchases for $date", Toast.LENGTH_SHORT).show()
    }

    private fun showAllPurchases() {
        filteredPurchases.clear()
        filteredPurchases.addAll(allPurchases)
        updateUI()
        Toast.makeText(this, "Showing all purchases", Toast.LENGTH_SHORT).show()
    }

    private fun updateUI() {
        val customerCount = filteredPurchases.distinctBy { it.customerName }.size
        val totalMedicinesSold = filteredPurchases.sumOf { it.quantity }
        val totalRevenue = filteredPurchases.sumOf { it.quantity * it.price }

        binding.summaryText.text = """
            Customers: $customerCount
            Medicines Sold: $totalMedicinesSold
            Total Revenue: $${"%.2f".format(totalRevenue)}
        """.trimIndent()

        adapter.notifyDataSetChanged()
    }
}


data class Purchase(
    val id: String,
    val customerName: String,
    val medicineId: String,
    val quantity: Int,
    val price: Double,
    val timestamp: Date,
    val date: String
)