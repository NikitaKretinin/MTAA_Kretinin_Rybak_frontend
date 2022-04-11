package fiit.mtaa.frontend.ui

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import fiit.mtaa.frontend.R
import fiit.mtaa.frontend.data.model.User
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.*

var server_ip: String = "http://" + (System.getenv("MTAA_SERVER_IP") ?: "192.168.1.5") + ":8080"

val client = HttpClient(CIO) {
    install(JsonFeature) {
        serializer = GsonSerializer()
        acceptContentTypes += ContentType("application", "json")
    }
}

@RequiresApi(Build.VERSION_CODES.M)
fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
//                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
//                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
//                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
    }
    return false
}

fun <T: Any> saveObject(context: Context, record: T, path: String) {
    val fos: FileOutputStream = context.openFileOutput(path, Context.MODE_PRIVATE)
    val os = ObjectOutputStream(fos)
    os.writeObject(record)
    os.close()
    fos.close()
}

fun <T: Any> loadObject(context: Context, path: String): T {
    val fis: FileInputStream = context.openFileInput(path)
    val `is` = ObjectInputStream(fis)
    val result: T = `is`.readObject() as T
    `is`.close()
    fis.close()
    return result
}



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

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val userDataFile = File(this@MainActivity.filesDir, "userData.ser")

        if (!isOnline(this@MainActivity)) {
            val fileExists = userDataFile.exists()

            if (fileExists) {
                val intent = Intent(this@MainActivity, HomepageActivity::class.java)
                val user: User = loadObject(this@MainActivity, "userData.ser") as User

                intent.putExtra("Token", "")
                intent.putExtra("Login", "${user.getLogin()} (offline)")
                intent.putExtra("Role", user.getRole())
                startActivity(intent)
            } else {
                Toast.makeText(this@MainActivity, "No info about previous session found",
                    Toast.LENGTH_LONG).show()
            }
        }

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
                        val mapObj = mutableMapOf<String, String>()
                        mapObj["login"] = usernameText.toString()
                        mapObj["password"] = passwordText.toString()
                        val response: JsonObject = client.get("$server_ip/getToken") {
                            contentType(ContentType.Application.Json)
                            body = mapObj
                        }
                        saveObject(this@MainActivity, User( usernameText.toString(),
                            response.get("role").asString ), "userData.ser")
                        Toast.makeText(this@MainActivity, "Logged " +
                                "as $usernameText", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@MainActivity, HomepageActivity::class.java)
                        intent.putExtra("Token", response.get("token").asString)
                        intent.putExtra("Login", usernameText.toString())
                        intent.putExtra("Role", response.get("role").asString)
                        startActivity(intent)
                    } catch (e: ClientRequestException) {
                        println(e.localizedMessage)
                        Toast.makeText(this@MainActivity, "Wrong login or password!", Toast.LENGTH_LONG).show()
                    }
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