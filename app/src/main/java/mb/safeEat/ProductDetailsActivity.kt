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
import com.google.android.material.card.MaterialCardView

class ProductDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)
        initAdapter()
        initHeader()
    }

    private fun initHeader() {
        val title = findViewById<TextView>(R.id.header_title)
        val backButton = findViewById<MaterialCardView>(R.id.header_back_button)
        title.text = resources.getString(R.string.t_product_details)
        backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun initAdapter() {
        val adapter = ProductDetailAdapter(createList())
        val items = findViewById<RecyclerView>(R.id.product_detail_items)
        items.layoutManager = LinearLayoutManager(this)
        items.adapter = adapter
    }

    private fun createList(): java.util.ArrayList<ProductDetail> {
        return arrayListOf(
            ProductDetail("Carne moida bovina", false),
            ProductDetail("Pimenta", true),
        )
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
        private val content = itemView.findViewById<TextView>(R.id.product_details_item_content)
        private val image = itemView.findViewById<ImageView>(R.id.product_details_item_image)

        fun bind(item: ProductDetail) {
            content.text = item.name
            if (item.isRestrict) {
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
