package fiit.mtaa.frontend.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import fiit.mtaa.frontend.R
import fiit.mtaa.frontend.data.adapters.MealAdapter
import fiit.mtaa.frontend.data.datasources.MealDatasource
import fiit.mtaa.frontend.data.model.Meal
import fiit.mtaa.frontend.data.model.User
import java.io.File

class MenuActivity() : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_page)

        lateinit var mealsDataset: List<Meal>
        // Initialize data.
        if (isOnline(this@MenuActivity)) {
            mealsDataset = MealDatasource().loadMeals()
            saveObject(this@MenuActivity, mealsDataset, "menuData.ser")
        } else {
            val userDataFile = File(this@MenuActivity.filesDir, "menuData.ser")
            val fileExists = userDataFile.exists()

            if (fileExists) {
                mealsDataset = loadObject(this@MenuActivity, "menuData.ser") as List<Meal>
            } else {
                Toast.makeText(this@MenuActivity, "No info about menu found in cache",
                    Toast.LENGTH_LONG).show()
                this@MenuActivity.onBackPressed() // return to the previous screen
                return
            }
        }

        val adapter = MealAdapter(this, mealsDataset, role)

        val recyclerView = findViewById<RecyclerView>(R.id.menu_rv)
        recyclerView.adapter = adapter

        val btmReset = findViewById<Button>(R.id.reset_btn)
        val btnProceed = findViewById<Button>(R.id.proceed_btn)

        btmReset.setOnClickListener {
            for (item in mealsDataset) {
                item.count = 0
            }
            adapter.notifyDataSetChanged()
            Toast.makeText(this@MenuActivity, "Reset", Toast.LENGTH_LONG).show()
        }

        btnProceed.setOnClickListener {
            if (isOnline(this@MenuActivity)) {
                val orderMeals: ArrayList<Meal> = arrayListOf()
                for (meal in mealsDataset) {
                    while (meal.count > 0) {
                        orderMeals.add(meal)
                        meal.count -= 1
                    }
                }
                if (orderMeals.isEmpty()) {
                    Toast.makeText(this@MenuActivity, "No meals in order!", Toast.LENGTH_LONG).show()
                } else {
                    val mealsNames: ArrayList<String> = arrayListOf()
                    val mealsPrices: ArrayList<Int> = arrayListOf()
                    val mealsId: ArrayList<Int> = arrayListOf()
                    var price: Int = 0
                    for (meal in orderMeals) {
                        price += meal.price
                        mealsId.add(meal.id)
                        mealsNames.add(meal.name)
                        mealsPrices.add(meal.price)
                    }
                    adapter.notifyDataSetChanged()
                    val intent = Intent(this@MenuActivity, OrderConfirmationActivity::class.java)
                    intent.putExtra("MealsID", mealsId)
                    intent.putExtra("Meals", mealsNames)
                    intent.putExtra("Prices", mealsPrices)
                    intent.putExtra("TotalPrice", price)
                    startActivity(intent)
                }
            } else {
                Toast.makeText(this@MenuActivity, "Order confirmation requires an" +
                        " Internet connection", Toast.LENGTH_LONG).show()
            }
        }

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true)
    }
}