package fiit.mtaa.frontend.data.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fiit.mtaa.frontend.R
import java.util.*


class OrderMealsAdapter(
    private val context: Context,
    private val mealsNames: ArrayList<String>,
    private val mealsPrices: ArrayList<Int>,
) : RecyclerView.Adapter<OrderMealsAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name_tv)
        val price: TextView = view.findViewById(R.id.price_tv)
    }

    override fun getItemCount(): Int {
        return mealsNames.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.order_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.itemView.apply {
            holder.name.text =  mealsNames[position]
            val priceStr = mealsPrices[position].toString()
            holder.price.text = "$priceStr €"
        }
    }
}