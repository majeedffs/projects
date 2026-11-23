package com.example.pharma3ync

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailInput = findViewById<EditText>(R.id.editTextEmail)
        val passwordInput = findViewById<EditText>(R.id.editTextPassword)
        val loginButton = findViewById<Button>(R.id.buttonLogin)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in both fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = MockDatabase.employees.find { it.email == email && it.password == password }

            if (user != null) {
                val intent = if (user.role == "admin") {
                    Intent(this, AdminHomeActivity::class.java)
                } else {
                    Intent(this, EmployeeHomeActivity::class.java)
                }
                intent.putExtra("name", user.name)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Incorrect email or password (try admin@test.com / 123456)", Toast.LENGTH_SHORT).show()
            }
        }
    }
}