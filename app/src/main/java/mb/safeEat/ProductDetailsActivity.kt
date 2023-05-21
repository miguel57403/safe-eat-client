package mb.safeEat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProductDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        initAdapter()
    }

    private fun initAdapter() {
        val adapter = ProductDetailAdapter(
            arrayListOf(
                ProductDetail("Carne moida bovina", false),
                ProductDetail("Pimenta", true),
            )
        )
        val items = findViewById<RecyclerView>(R.id.product_detail_items)
        items.layoutManager = LinearLayoutManager(this)
        items.adapter = adapter
    }
}


class ProductDetailAdapter(private var data: ArrayList<ProductDetail>) :
    RecyclerView.Adapter<ProductDetailAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_product_detail, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val content = itemView.findViewById<TextView>(R.id.product_details_item_content)
        val image = itemView.findViewById<ImageView>(R.id.product_details_item_image)

        fun bind(item: ProductDetail) {
            content.text = item.name
            if(item.isRestrict) {
                val color = ContextCompat.getColor(itemView.context, R.color.red)
                content.setTextColor(color)
                image.setColorFilter(color)
            }

        }
    }
}

data class ProductDetail(
    val name: String,
    val isRestrict: Boolean,
)
