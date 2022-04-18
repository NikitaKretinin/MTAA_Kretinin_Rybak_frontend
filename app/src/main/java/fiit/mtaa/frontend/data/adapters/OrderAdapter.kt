package fiit.mtaa.frontend.data.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fiit.mtaa.frontend.R
import fiit.mtaa.frontend.data.model.Order
import fiit.mtaa.frontend.ui.client
import fiit.mtaa.frontend.ui.server_ip
import fiit.mtaa.frontend.ui.token
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*


class OrderAdapter(
    private val context: Context,
    private val orders: MutableList<Order>,
) : RecyclerView.Adapter<OrderAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val login: TextView = view.findViewById(R.id.name_tv)
        val price: TextView = view.findViewById(R.id.price_tv)

        val btnDone: Button = view.findViewById(R.id.set_done_btn)
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.undone_order_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.itemView.apply {
            holder.login.text =  orders[position].user
            val priceStr = orders[position].price
            holder.price.text = "$priceStr €"
        }

        holder.btnDone.setOnClickListener {
            val id = orders[position].id
            runBlocking {
                launch {
                    val response: HttpResponse = client.put("$server_ip/setOrderDone/$id") {
                        header("Authorization", token)
                    }
                }
            }
            orders.removeAt(position)
            this.notifyItemRemoved(position)
            notifyItemRangeChanged(position, getItemCount());
        }
    }
}