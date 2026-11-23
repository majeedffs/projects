package com.example.pharma3ync

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pharma3ync.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.searchButton.setOnClickListener {
            val searchQuery = binding.searchInput.text.toString().trim()
            if (searchQuery.isNotEmpty()) {
                searchMedicine(searchQuery)
            } else {
                Toast.makeText(this, "Please enter a medicine name", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backButton.setOnClickListener { finish() }
    }

    private fun searchMedicine(name: String) {
        val med = MockDatabase.medicines.find { it.name.equals(name, ignoreCase = true) }

        if (med != null) {
            displayMedicine(med)
        } else {
            showNoResults()
        }
    }

    private fun displayMedicine(medicine: LocalMedicine) {
        binding.resultForm.text = "Form: ${medicine.form}"
        binding.resultName.text = "Name: ${medicine.name}"
        binding.resultQuantity.text = "Quantity: ${medicine.quantity}"
        binding.resultShelf.text = "Shelf: ${medicine.shelf}"
        binding.resultStrength.text = "Strength: ${medicine.strength}"

        binding.resultsContainer.visibility = View.VISIBLE
        binding.noResultsText.visibility = View.GONE
    }

    private fun showNoResults() {
        binding.resultsContainer.visibility = View.GONE
        binding.noResultsText.visibility = View.VISIBLE
    }
}