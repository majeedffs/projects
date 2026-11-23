package com.example.pharma3ync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class EmployeeManagementActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EmployeeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee_management)

        recyclerView = findViewById(R.id.recyclerEmployees)
        adapter = EmployeeAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.buttonAddEmployee).setOnClickListener {
            showEmployeeDialog()
        }
    }

    private fun showEmployeeDialog(existing: User? = null) {
        val nameInput = EditText(this).apply { hint = "Name" }
        val emailInput = EditText(this).apply { hint = "Email" }
        val passwordInput = EditText(this).apply { hint = "Password" }
        val roleInput = EditText(this).apply { hint = "Role (admin/employee)" }

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
            addView(nameInput)
            addView(emailInput)
            addView(passwordInput)
            addView(roleInput)
        }

        existing?.let {
            nameInput.setText(it.name)
            emailInput.setText(it.email)
            passwordInput.setText(it.password)
            roleInput.setText(it.role)
        }

        AlertDialog.Builder(this)
            .setTitle(if (existing == null) "Add Employee" else "Edit Employee")
            .setView(layout)
            .setPositiveButton("Save") { _, _ ->
                if (existing != null) {
                    // Remove old ref to add new one (simple update)
                    MockDatabase.employees.remove(existing)
                }
                val newUser = User(
                    id = existing?.id ?: UUID.randomUUID().toString(),
                    name = nameInput.text.toString(),
                    email = emailInput.text.toString(),
                    password = passwordInput.text.toString(),
                    role = roleInput.text.toString()
                )
                MockDatabase.employees.add(newUser)
                adapter.notifyDataSetChanged()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteEmployee(emp: User) {
        MockDatabase.employees.remove(emp)
        adapter.notifyDataSetChanged()
        Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
    }

    private inner class EmployeeAdapter : RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder>() {
        inner class EmployeeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val name: TextView = view.findViewById(R.id.textViewName)
            val email: TextView = view.findViewById(R.id.textViewEmail)
            val role: TextView = view.findViewById(R.id.textViewRole)
            val edit: Button = view.findViewById(R.id.buttonEdit)
            val delete: Button = view.findViewById(R.id.buttonDelete)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmployeeViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_employee, parent, false)
            return EmployeeViewHolder(view)
        }

        override fun onBindViewHolder(holder: EmployeeViewHolder, position: Int) {
            val emp = MockDatabase.employees[position]
            holder.name.text = emp.name
            holder.email.text = emp.email
            holder.role.text = emp.role
            holder.edit.setOnClickListener { showEmployeeDialog(emp) }
            holder.delete.setOnClickListener { deleteEmployee(emp) }
        }

        override fun getItemCount(): Int = MockDatabase.employees.size
    }
}