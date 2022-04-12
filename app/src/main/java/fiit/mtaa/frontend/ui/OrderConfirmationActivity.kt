package fiit.mtaa.frontend.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import fiit.mtaa.frontend.R
import fiit.mtaa.frontend.data.adapters.OrderMealsAdapter
import fiit.mtaa.frontend.data.model.Contact
import fiit.mtaa.frontend.data.model.Meal
import fiit.mtaa.frontend.data.model.User
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.http.cio.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.net.ConnectException

class OrderConfirmationActivity() : AppCompatActivity() {

    private fun validData(address: String, phone: String): Boolean {
        if (address.isEmpty() || phone.isEmpty()) {
            return false
        }
        val phone_reg = Regex("^([+][0-9]{3}|0) ?[0-9]{9}\$")
        return phone_reg.matches(phone)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.order_confirmation)

        // Initialize data.
        lateinit var adapter: OrderMealsAdapter
        val mealsIDs = intent.getSerializableExtra("MealsID") as ArrayList<Int>
        val mealsNames = intent.getSerializableExtra("Meals") as ArrayList<String>
        val mealsPrices = intent.getSerializableExtra("Prices") as ArrayList<Int>

        if (mealsNames != null && mealsPrices != null && mealsIDs != null) {
            adapter = OrderMealsAdapter(this, mealsNames, mealsPrices)
        } else {
            Toast.makeText(this@OrderConfirmationActivity, "No meals in order", Toast.LENGTH_LONG).show()
            onBackPressed() // return to the previous screen
        }

        val recyclerView = findViewById<RecyclerView>(R.id.order_rv)
        recyclerView.adapter = adapter

        val btmDiscard = findViewById<Button>(R.id.discard_btn)
        val btnConfirm = findViewById<Button>(R.id.confirm_btn)
        val tvTotalPrice = findViewById<TextView>(R.id.totalPrice_tv)
        val totalPrice: Int = intent.getIntExtra("TotalPrice", 0)
        if (totalPrice == 0) {
            Toast.makeText(this@OrderConfirmationActivity, "Wrong total price", Toast.LENGTH_LONG).show()
            onBackPressed() // return to the previous screen
        } else {
            tvTotalPrice.text = "$totalPrice €"
        }

        val etAddress = findViewById<EditText>(R.id.address)
        val etPhone = findViewById<EditText>(R.id.phone)
        val radioGroup = findViewById<RadioGroup>(R.id.radio_group)

        btmDiscard.setOnClickListener {
            Toast.makeText(this@OrderConfirmationActivity, "Discarded", Toast.LENGTH_LONG).show()
            onBackPressed() // return to the previous screen
        }

        btnConfirm.setOnClickListener {
            if (validData(etAddress.text.toString(), etPhone.text.toString())) {
                runBlocking {
                    launch {
                        try {
                            val selectedOption: Int = radioGroup!!.checkedRadioButtonId
                            val contact: Contact =
                                Contact(etAddress.text.toString(), etPhone.text.toString())
                            val mapObj = mutableMapOf<String, Contact>()
                            mapObj["contact"] = contact
                            var response: Response = client.put("$server_ip/editUser") {
                                contentType(ContentType.Application.Json)
                                body = mapObj
                                header("Authorization", token)
                            }

                            response = client.post("$server_ip/addOrder") {
                                parameter("mealsId", mealsIDs.joinToString(separator = ","))
                                if (selectedOption == R.id.cash_rb) {
                                    parameter("pay_by_cash", true)
                                } else {
                                    parameter("pay_by_cash", false)
                                }
                                header("Authorization", token)
                            }
                            Toast.makeText(
                                this@OrderConfirmationActivity,
                                "Order Registered",
                                Toast.LENGTH_LONG
                            ).show()
                        }  catch (e: Exception) {
                            println(e.localizedMessage)
                            ErrorOutput(this@OrderConfirmationActivity, e)
                        }
                    }
                }
            } else {
                Toast.makeText(this@OrderConfirmationActivity, "Wrong shipping information format", Toast.LENGTH_LONG).show()
            }

        }

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true)
    }
}