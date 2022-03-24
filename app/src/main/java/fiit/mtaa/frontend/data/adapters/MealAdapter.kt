package fiit.mtaa.frontend.data.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64.decode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import fiit.mtaa.frontend.R
import fiit.mtaa.frontend.data.model.Meal
import fiit.mtaa.frontend.ui.user
import java.lang.Byte.decode
import java.util.*


class MealAdapter (
    private val context: Context,
    private val dataset: List<Meal>,
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

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.itemView.apply {
            holder.name.text =  item.name
            holder.descr.text =  item.description
            val priceStr = item.price.toString()
            holder.price.text = "$priceStr €"
            if (item.count == 0) {
                holder.counter.visibility = View.INVISIBLE;
            }

            if (item.photo != null) {
                val decodedBytes = Base64.getDecoder().decode(item.photo)
                val bmp = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                holder.photo.setImageBitmap(bmp)
            }

            if (user.getRole() == "manager") {
                holder.changeBtn.visibility = View.VISIBLE;
                holder.deleteDtn.visibility = View.VISIBLE;
            }

            holder.addBtn.setOnClickListener {
                holder.counter.visibility = View.VISIBLE;
                item.count += 1
                holder.counter.text = item.count.toString()
            }
        }
    }
}