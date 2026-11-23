package com.example.pharma3ync

import java.util.*


data class User(val id: String, val name: String, val email: String, val password: String, val role: String)
data class LocalMedicine(var id: String, var name: String, var quantity: Int, var form: String, var strength: String, var shelf: String, var price: Double)
data class LocalOrder(val id: String, val medicineId: String, val quantity: Int, var status: String)


data class LocalPurchase(
    val id: String,
    val customerName: String,
    val employeeId: String,
    val medicineId: String,
    val quantity: Int,
    val price: Double,
    val timestamp: Date,
    val date: String
)

object MockDatabase {
    val employees = mutableListOf(
        User("1", "Big Boss", "admin@test.com", "123456", "admin"),
        User("2", "John Doe", "emp@test.com", "123456", "employee")
    )

    val medicines = mutableListOf(
        LocalMedicine("101", "Panadol", 50, "Pill", "500mg", "A1", 5.0),
        LocalMedicine("102", "Advil", 10, "Pill", "200mg", "A2", 8.5),
        LocalMedicine("103", "Cough Syrup", 5, "Liquid", "100ml", "B1", 12.0)
    )

    val orders = mutableListOf<LocalOrder>()
    val purchases = mutableListOf<LocalPurchase>()
}