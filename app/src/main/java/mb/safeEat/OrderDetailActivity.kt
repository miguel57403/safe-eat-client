package mb.safeEat

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.ColorRes
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
        val statusArg = OrderStatus.DELIVERED
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
        status.text = orderStatusToString(this, statusArg)
        progressBar.progressDrawable.setTint(
            ContextCompat.getColor(this, orderStatusToColor(statusArg))
        )
        progressBar.progressDrawable.setTintMode(PorterDuff.Mode.DARKEN)
        progressBar.progress = orderStatusToProgress(statusArg)
    }

    private fun initHeader() {
        val title = findViewById<TextView>(R.id.header_title)
        val backButton = findViewById<MaterialCardView>(R.id.header_back_button)
        title.text = resources.getString(R.string.t_order)
        backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun initAdapter() {
        val items = findViewById<RecyclerView>(R.id.order_detail_items)
        items.layoutManager = LinearLayoutManager(this)
        items.adapter = OrderDetailAdapter(createList())
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_order_detail, parent, false)
    )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val quantity = itemView.findViewById<TextView>(R.id.order_detail_item_quantity)
        private val product = itemView.findViewById<TextView>(R.id.order_detail_item_product)
        private val price = itemView.findViewById<TextView>(R.id.order_detail_item_price)

        @SuppressLint("SetTextI18n")
        fun bind(item: OrderItem) {
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
    TRANSPORTING,
    DELIVERED
}

fun orderStatusToString(context: Context, status: OrderStatus): String = when (status) {
    OrderStatus.DELIVERED -> context.resources.getString(R.string.t_delivered)
    OrderStatus.TRANSPORTING -> context.resources.getString(R.string.t_transporting)
    OrderStatus.PREPARING -> context.resources.getString(R.string.t_preparing)
}

@ColorRes
fun orderStatusToColor(status: OrderStatus): Int {
    return when (status) {
        OrderStatus.PREPARING -> R.color.orange_500
        OrderStatus.TRANSPORTING -> R.color.orange_500
        OrderStatus.DELIVERED -> R.color.green_500
    }
}

fun orderStatusToProgress(status: OrderStatus): Int {
    return when (status) {
        OrderStatus.PREPARING -> 33
        OrderStatus.TRANSPORTING -> 66
        OrderStatus.DELIVERED -> 100
    }
}