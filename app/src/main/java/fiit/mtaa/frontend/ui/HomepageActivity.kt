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
import fiit.mtaa.frontend.R
import fiit.mtaa.frontend.data.model.User
import java.io.File

lateinit var token: String
lateinit var role: String

class HomepageActivity() : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homepage)

        lateinit var login: String
        // get reference to all views
        if (intent.hasExtra("Token") && intent.hasExtra("Role")) {
            token = intent.getStringExtra("Token") as String
            role = intent.getStringExtra("Role") as String
            login = intent.getStringExtra("Login") as String
        }
        val loginName = findViewById<TextView>(R.id.login_tv)
        if (isOnline(this@HomepageActivity)) {
            loginName.text = "signed in as $login"
        } else {
            loginName.text = "signed in as $login (offline)"
        }

//        val photo:
        val btnMenu = findViewById<Button>(R.id.menu_btn)
        val btnLiveCooking = findViewById<Button>(R.id.live_cooking_btn)
        val btnManageOrders = findViewById<Button>(R.id.orders_btn)

        val tvManageOrders = findViewById<TextView>(R.id.orders_tv)

        if (role == "manager") {
            btnManageOrders.visibility = View.VISIBLE
            tvManageOrders.visibility = View.VISIBLE
        }

        btnMenu.setOnClickListener {
            val intent = Intent(this@HomepageActivity, MenuActivity::class.java)
            startActivity(intent)
        }

        btnManageOrders.setOnClickListener {
            if (isOnline(this@HomepageActivity)) {
                val intent = Intent(this@HomepageActivity, OrdersActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this@HomepageActivity, "Orders management needs an internet connection", Toast.LENGTH_LONG).show()
            }
        }

        btnLiveCooking.setOnClickListener {
            if (isOnline(this@HomepageActivity)) {
                val intent = Intent(this@HomepageActivity, StartCallActivity::class.java)
                startActivity(intent)
                Toast.makeText(this@HomepageActivity, "Live Cooking", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@HomepageActivity, "Live Cooking needs an internet connection", Toast.LENGTH_LONG).show()
            }

        }
    }
}