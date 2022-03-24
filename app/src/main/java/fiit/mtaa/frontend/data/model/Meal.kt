package fiit.mtaa.frontend.data.model

import java.io.Serializable

data class Meal(val id: Int, val name: String, val description: String? = "", val price: Int, val photo: String? = null): Serializable
