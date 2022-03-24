package fiit.mtaa.frontend.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import fiit.mtaa.frontend.R
import fiit.mtaa.frontend.data.adapters.MealAdapter
import fiit.mtaa.frontend.data.datasources.MealDatasource
import fiit.mtaa.frontend.data.model.User

class MenuActivity() : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_page)

        // Initialize data.
        val mealsDataset = MealDatasource().loadMeals()

        val recyclerView = findViewById<RecyclerView>(R.id.menu_rv)
        recyclerView.adapter = MealAdapter(this, mealsDataset)

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true)
    }
}