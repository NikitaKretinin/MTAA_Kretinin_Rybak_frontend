package fiit.mtaa.frontend.data.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import fiit.mtaa.frontend.R
import fiit.mtaa.frontend.data.model.Meal
import fiit.mtaa.frontend.ui.user

class MealAdapter (
    private val context: Context,
    private val dataset: List<Meal>
) : RecyclerView.Adapter<MealAdapter.ItemViewHolder>() {

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.name_tv)
        val descr: TextView = view.findViewById(R.id.description_tv)
        val price: TextView = view.findViewById(R.id.price_tv)
        val counter: TextView = view.findViewById(R.id.count_tv)
        val addBtn: Button = view.findViewById(R.id.add_btn)
        val changeBtn: Button = view.findViewById(R.id.change_btn)
        val deleteDtn: Button = view.findViewById(R.id.delete_btn)
        val photo: ImageView = view.findViewById(R.id.imageView)

        fun initialize() {
            name.text = "name"
            descr.text = "descr"
            price.text = "1$"
            counter.text = "0"
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.menu_item, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.itemView.apply {
            holder.name.text =  item.name
            holder.descr.text =  item.description
            holder.price.text =  item.price.toString()

            if (user.getRole() == "manager") {
                holder.changeBtn.visibility = View.VISIBLE;
                holder.deleteDtn.visibility = View.VISIBLE;
            }

            holder.addBtn.setOnClickListener {
                holder.counter.visibility = View.VISIBLE;
                val count: Int = holder.counter.text.toString().toInt()
                holder.counter.text = (count + 1).toString()
            }
        }
    }
}