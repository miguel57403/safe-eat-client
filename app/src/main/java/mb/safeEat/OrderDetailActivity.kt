package mb.safeEat

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class OrderDetailActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_detail)
        initHeader()
        initAdapter()

        // TODO: Transform this in args
        val statusArg = OrderStatus.COMPLETE
        val restaurantArg = "Sabor Brasileiro"
        val dateArg = "27/03/2023"

        val image = findViewById<ImageView>(R.id.order_detail_restaurant_image)
        val restaurant = findViewById<TextView>(R.id.order_detail_restaurant_name)
        val date = findViewById<TextView>(R.id.order_detail_date)
        val status = findViewById<TextView>(R.id.order_detail_status)
        val progressBar = findViewById<ProgressBar>(R.id.order_detail_progress_bar)

        image.setImageResource(R.drawable.restaurant)
        restaurant.text = restaurantArg
        date.text = dateArg
        when (statusArg) {
            OrderStatus.PREPARING -> {
                status.text = resources.getString(R.string.v_preparing)
                progressBar.progress = 33
            }

            OrderStatus.DELIVERING -> {
                status.text = resources.getString(R.string.v_delivering)
                progressBar.progress = 66
            }

            OrderStatus.COMPLETE -> {
                status.text = resources.getString(R.string.v_complete)
                progressBar.progress = 0
                progressBar.progressDrawable.setTint(ContextCompat.getColor(this, R.color.green_500))
                progressBar.progressDrawable.setTintMode(PorterDuff.Mode.DARKEN)
            }
        }
    }

    private fun initHeader() {
        val title = findViewById<TextView>(R.id.header_title)
        val backButton = findViewById<MaterialCardView>(R.id.header_back_button)
        title.text = resources.getString(R.string.t_order)
        backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
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

enum class OrderStatus {
    PREPARING,
    DELIVERING,
    COMPLETE
}
