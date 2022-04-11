package fiit.mtaa.frontend.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fiit.mtaa.frontend.R
import fiit.mtaa.frontend.data.model.User
import java.io.File

lateinit var token: String
lateinit var role: String

class HomepageActivity() : AppCompatActivity() {

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
        loginName.text = "signed in as $login"

        val btnMenu = findViewById<Button>(R.id.menu_btn)
        val btnLiveCooking = findViewById<Button>(R.id.live_cooking_btn)

        btnMenu.setOnClickListener {
            val intent = Intent(this@HomepageActivity, MenuActivity::class.java)
            startActivity(intent)
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