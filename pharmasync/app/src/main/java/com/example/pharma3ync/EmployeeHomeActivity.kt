package com.example.pharma3ync

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class EmployeeHomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_home)

        val name = intent.getStringExtra("name") ?: "Employee"
        val greeting = findViewById<TextView>(R.id.textViewGreeting)
        val dateTime = findViewById<TextView>(R.id.textViewDateTime)

        val date = SimpleDateFormat("EEEE, MMMM d - HH:mm", Locale.getDefault()).format(Date())

        greeting.text = "Hello, $name"
        dateTime.text = date

        // Inventory
        findViewById<Button>(R.id.buttonInventory).setOnClickListener {
            startActivity(Intent(this, InventoryActivity::class.java))
        }

        // Orders -> PurchaseLogActivity
        findViewById<Button>(R.id.buttonOrders).setOnClickListener {
            // Need to pass an ID here. Since we are offline, we'll find the ID based on the name passed in Intent
            // or just default to the first employee if not found for testing.
            val user = MockDatabase.employees.find { it.name == name }
            val intent = Intent(this, PurchaseLogActivity::class.java)
            intent.putExtra("employeeId", user?.id ?: "2")
            startActivity(intent)
        }

        // Search
        findViewById<Button>(R.id.buttonEmployees).setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        // Shift Log
        findViewById<Button>(R.id.buttonFinances).setOnClickListener {
            val user = MockDatabase.employees.find { it.name == name }
            val intent = Intent(this, ShiftLogActivity::class.java)
            intent.putExtra("employeeId", user?.id ?: "2")
            startActivity(intent)
        }

        // Logout
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}