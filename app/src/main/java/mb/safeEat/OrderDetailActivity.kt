package mb.safeEat

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class OrderDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        initAdapter()
    }

    private fun initAdapter() {
        val adapter = OrderDetailAdapter(createList())
        val items = findViewById<RecyclerView>(R.id.order_detail_items)
        items.layoutManager = LinearLayoutManager(this)
        items.adapter = adapter
    }

    private fun createList(): ArrayList<OrderItem> {
        return arrayListOf(
            OrderItem(10, "Hot-dog combo", "€10,00"),
            OrderItem(10, "Hot-dog combo", "€10,00"),
            OrderItem(10, "Hot-dog combo", "€10,00"),
        )
    }
}

class OrderDetailAdapter(private var data: ArrayList<OrderItem>) :
    RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_order_detail, parent, false)
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
        private val quantity = itemView.findViewById<TextView>(R.id.order_detail_item_quantity)
        private val product = itemView.findViewById<TextView>(R.id.order_detail_item_product)
        private val price = itemView.findViewById<TextView>(R.id.order_detail_item_price)

        @SuppressLint("SetTextI18n")
        fun bindView(item: OrderItem) {
            quantity.text = item.quantity.toString() + "x"
            product.text = item.product
            price.text = item.price
        }
    }
}

data class OrderItem(
    val quantity: Int,
    val product: String,
    val price: String
)

