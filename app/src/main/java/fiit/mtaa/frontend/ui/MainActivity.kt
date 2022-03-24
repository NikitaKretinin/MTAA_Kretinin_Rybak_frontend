package fiit.mtaa.frontend.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import fiit.mtaa.frontend.R
import fiit.mtaa.frontend.data.model.User
import org.json.JSONObject
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

var server_ip: String = "http://" + (System.getenv("MTAA_SERVER_IP") ?: "192.168.1.5") + ":8080"

val client = HttpClient(CIO) {
    install(JsonFeature) {
        serializer = GsonSerializer()
        acceptContentTypes += ContentType("application", "json")
    }
}

lateinit var user: User

class MainActivity : AppCompatActivity() {

    // Function to control data and register a new user
    private fun registerUser(login: String, pass: String) {
        if (login.length < 8 || login.length > 16) {
            Toast.makeText(this@MainActivity, "Username length " +
                    "should be in [8, 16] range", Toast.LENGTH_LONG).show()
        } else if (pass.length < 5) {
            Toast.makeText(this@MainActivity, "Password has to be at least 5" +
                    " characters long", Toast.LENGTH_LONG).show()
        } else {
            runBlocking {
                launch {
                    val newUser: User = client.post("$server_ip/addUser") {
                        contentType(ContentType.Application.Json)
                        body = User(login, pass)
                    }
                    Toast.makeText(this@MainActivity, "Registered", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // get reference to all views
        var username = findViewById<EditText>(R.id.username)
        var password = findViewById<EditText>(R.id.password)

        var btnLogin = findViewById<Button>(R.id.login)
        var btnRegister = findViewById<Button>(R.id.register)

        btnLogin.setOnClickListener {
            val usernameText = username.text
            val passwordText = password.text

            runBlocking {
                launch { // launch a new coroutine and continue
//                    val response: HttpResponse = client.get("$server_ip/getUsers")
                    try {
                        user = client.get("$server_ip/getUser") {
                            formData {
                                parameter("login", usernameText)
                            }
                        }
                        if (user.verify(usernameText.toString(), passwordText.toString())) {
                            Toast.makeText(this@MainActivity, "Logged " +
                                    "as $usernameText", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@MainActivity, HomepageActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@MainActivity, "Wrong login or password!", Toast.LENGTH_LONG).show()
                        }
                    } catch (e: ClientRequestException) {
                        println(e.localizedMessage)
                        Toast.makeText(this@MainActivity, "Wrong login or password!", Toast.LENGTH_LONG).show()
                    }
                    /* users: List<User> = get...
                    users.forEach {
                        it.printUserInfo()
                    }*/
                }
            }
        }

        // set on-click listener
        btnRegister.setOnClickListener {
            val usernameText = username.text;
            val passwordText = password.text;
            registerUser(usernameText.toString(), passwordText.toString())

        }
    }
}