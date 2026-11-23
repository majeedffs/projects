# PharmaSync – Android pharmacy app

Android app, June 2025. Kotlin with XML layouts, built in Android Studio. (The Android package is named `pharma3ync`.)

PharmaSync covers the daily work of a small pharmacy, with a separate view for the admin and for employees. It uses a local in-memory data layer, so it runs without any server.

## Features

**Employee view**
- Point of sale: sell medicines to a customer and record the purchase with a digital receipt
- Search medicines
- View stock and place orders
- Shift log

**Admin view**
- Staff management (add, edit and delete employees)
- Inventory statistics
- Finances: total revenue, revenue per employee and most-sold item, filterable by date
- Order list

**Login** routes each user to the admin or employee home screen based on their role.

## Structure

```
app/src/main/java/com/example/pharma3ync/
  LoginActivity.kt               role-based login
  AdminHomeActivity.kt           admin menu
  EmployeeHomeActivity.kt        employee menu
  PurchaseLogActivity.kt         point of sale and receipts
  InventoryActivity.kt           stock
  SearchActivity.kt              medicine search
  OrderListActivity.kt           orders
  ShiftLogActivity.kt            shift log
  EmployeeManagementActivity.kt  staff management
  InventoryStatisticsActivity.kt inventory statistics
  FinancesActivity.kt            revenue and best sellers
  MedicineAdapter.kt, PurchaseAdapter.kt   RecyclerView adapters
  MockDatabase.kt                local data layer with sample data
```

## How to run

1. Open the `pharmasync` folder in Android Studio.
2. Let Gradle sync (the project uses Android Gradle Plugin 8.10, Kotlin 2.0, JDK 17).
3. Run on an emulator or a device with Android 7.0 (API 24) or newer.
4. Sign in with one of the sample accounts in `MockDatabase.kt`:
   - Admin: `admin@test.com` / `123456`
   - Employee: `emp@test.com` / `123456`

The data lives in memory, so it resets when the app restarts.
