package fiit.mtaa.frontend.data.model

import android.os.Parcel
import android.os.Parcelable
import io.ktor.utils.io.charsets.Charsets
import java.io.Serializable

data class Meal(val id: Int, val name: String, val description: String? = "", val price: Int, val photo: String? = null): Serializable {
    var count: Int = 0
}


