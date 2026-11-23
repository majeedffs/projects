package com.example.pharma3ync

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class AdminHomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        val name = intent.getStringExtra("name") ?: "Admin"
        val greeting = findViewById<TextView>(R.id.textViewGreeting)
        val dateTime = findViewById<TextView>(R.id.textViewDateTime)

        val date = SimpleDateFormat("EEEE, MMMM d - HH:mm", Locale.getDefault()).format(Date())

        greeting.text = "Hello, $name"
        dateTime.text = date

        findViewById<Button>(R.id.buttonEmployees).setOnClickListener {
            startActivity(Intent(this, EmployeeManagementActivity::class.java))
        }

        findViewById<Button>(R.id.buttonFinances).setOnClickListener {
            startActivity(Intent(this, FinancesActivity::class.java))
        }

        findViewById<Button>(R.id.buttonInventory).setOnClickListener {
            startActivity(Intent(this, InventoryStatisticsActivity::class.java))
        }

        findViewById<Button>(R.id.buttonOrders).setOnClickListener {
            startActivity(Intent(this, OrderListActivity::class.java))
        }

        val btnLogout = findViewById<Button>(R.id.btnLogout)
        btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}