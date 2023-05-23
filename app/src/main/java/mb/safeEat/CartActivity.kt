package mb.safeEat

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class CartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        initAdapter()
    }

    private fun initAdapter() {
        val adapter = CartAdapter(createList())
        val items = findViewById<RecyclerView>(R.id.cart_items)
        items.layoutManager = LinearLayoutManager(this)
        items.adapter = adapter
    }

    private fun createList(): ArrayList<Product> {
        return arrayListOf(
            Product(product = "Pizza acebolada", quantifyItem = 3, priceItem = 14.99F, warn = true),
            Product(product = "Pizza 4 queijos", quantifyItem = 1, priceItem = 2.99F, warn = false),
            Product(product = "Pizza de achova", quantifyItem = 2, priceItem = 2.99F, warn = false)
        )
    }
}

class CartAdapter(private var data: ArrayList<Product>) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bindView(item)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val container = itemView.findViewById<ConstraintLayout>(R.id.cart_item_container)
        private val product = itemView.findViewById<TextView>(R.id.cart_item_name)
        private val quantity = itemView.findViewById<TextView>(R.id.cart_item_quantity)
        private val price = itemView.findViewById<TextView>(R.id.cart_item_price)
        private val icon = itemView.findViewById<ImageView>(R.id.cart_item_alert)

        @SuppressLint("SetTextI18n")
        fun bindView(item: Product) {
            if (item.warn) {
                val bg =
                    ContextCompat.getDrawable(itemView.context, R.drawable.border_item_warning)
                container.background = bg
                icon.visibility = View.VISIBLE
            }
            product.text = item.product
            quantity.text = item.quantifyItem.toString()
            price.text = ("€" + item.priceItem.toString())
        }
    }
}

data class Product(
    val product: String,
    val quantifyItem: Int,
    val priceItem: Float,
    val warn: Boolean
)