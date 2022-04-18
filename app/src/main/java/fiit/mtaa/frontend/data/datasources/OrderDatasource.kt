package fiit.mtaa.frontend.data.datasources

import fiit.mtaa.frontend.data.model.Meal
import fiit.mtaa.frontend.data.model.Order
import fiit.mtaa.frontend.ui.server_ip
import fiit.mtaa.frontend.ui.client
import fiit.mtaa.frontend.ui.token
import io.ktor.client.request.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class OrderDatasource() {

    fun loadUndoneOrders(): List<Order> {
        lateinit var orders: List<Order>
        runBlocking {
            launch {
                orders = client.get("$server_ip/getUndoneOrders") {
                    header("Authorization", token)
                }
            }
        }

        return orders
    }
}