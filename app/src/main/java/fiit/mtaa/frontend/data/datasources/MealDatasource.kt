package fiit.mtaa.frontend.data.datasources

import fiit.mtaa.frontend.data.model.Meal
import fiit.mtaa.frontend.ui.server_ip
import fiit.mtaa.frontend.ui.client
import io.ktor.client.request.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MealDatasource() {

    fun loadMeals(): MutableList<Meal> {
        lateinit var meals: MutableList<Meal>
        runBlocking {
            launch {
                meals = client.get("$server_ip/getMeals")
            }
        }

        return meals
    }
}