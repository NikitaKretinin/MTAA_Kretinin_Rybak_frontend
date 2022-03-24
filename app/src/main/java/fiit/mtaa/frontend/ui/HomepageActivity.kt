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

class HomepageActivity() : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.homepage)

        // get reference to all views
        var loginName = findViewById<TextView>(R.id.login_tv)
        loginName.text = "signed in as ${user.getLogin()}"

        var btnMenu = findViewById<Button>(R.id.menu_btn)
        var btnLiveCooking = findViewById<Button>(R.id.live_cooking_btn)

        btnMenu.setOnClickListener {
            val intent = Intent(this@HomepageActivity, MenuActivity::class.java)
            startActivity(intent)
        }

        btnLiveCooking.setOnClickListener {
            Toast.makeText(this@HomepageActivity, "Live Cooking", Toast.LENGTH_LONG).show()
        }
    }
}