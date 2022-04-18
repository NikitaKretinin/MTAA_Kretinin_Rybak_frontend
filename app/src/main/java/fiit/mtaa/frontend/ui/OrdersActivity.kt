package fiit.mtaa.frontend.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import fiit.mtaa.frontend.R
import fiit.mtaa.frontend.data.adapters.MealAdapter
import fiit.mtaa.frontend.data.adapters.OrderAdapter
import fiit.mtaa.frontend.data.datasources.MealDatasource
import fiit.mtaa.frontend.data.datasources.OrderDatasource
import fiit.mtaa.frontend.data.model.Meal
import fiit.mtaa.frontend.data.model.Order
import fiit.mtaa.frontend.data.model.User
import java.io.File

class OrdersActivity() : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.orders_page)

        lateinit var ordersDataset: List<Order>
        // Initialize data.
        if (isOnline(this@OrdersActivity)) {
            try {
                ordersDataset = OrderDatasource().loadUndoneOrders()
                saveObject(this@OrdersActivity, ordersDataset, "ordersData.ser")
            } catch (e: Exception) {
                println(e.localizedMessage)
                ErrorOutput(this@OrdersActivity, e)
                this@OrdersActivity.onBackPressed() // return to the previous screen
                return
            }
        } else {
            val userDataFile = File(this@OrdersActivity.filesDir, "ordersData.ser")
            val fileExists = userDataFile.exists()

            if (fileExists) {
                ordersDataset = loadObject(this@OrdersActivity, "ordersData.ser") as List<Order>
            } else {
                Toast.makeText(this@OrdersActivity, "No info about orders found in cache",
                    Toast.LENGTH_LONG).show()
                this@OrdersActivity.onBackPressed() // return to the previous screen
                return
            }
        }

        val adapter = OrderAdapter(this, ordersDataset)

        val recyclerView = findViewById<RecyclerView>(R.id.menu_rv)
        recyclerView.adapter = adapter

        val btmReturn = findViewById<Button>(R.id.return_btn)

        btmReturn.setOnClickListener {
            this@OrdersActivity.onBackPressed() // return to the previous screen
        }
    }
}