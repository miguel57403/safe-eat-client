package mb.safeEat.components

import android.graphics.Bitmap
import android.os.Bundle
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import mb.safeEat.R

class OrdersFragment(private val navigation: NavigationListener) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_orders, container, false)
        if (view != null) onInit(view)
        return view
    }

    private fun onInit(view: View) {
        initAdapter(view)
        initHeader(view)
    }

    private fun initHeader(view: View) {
        val title = view.findViewById<TextView>(R.id.header_title)
        val backButton = view.findViewById<MaterialCardView>(R.id.header_back_button)
        title.text = view.resources.getString(R.string.t_orders)
        backButton.setOnClickListener { navigation.onBackPressed() }
    }

    private fun initAdapter(view: View) {
        val adapter = OrdersAdapter(createList())
        val items = view.findViewById<RecyclerView>(R.id.orders_items)
        items.layoutManager = LinearLayoutManager(view.context)
        items.adapter = adapter
    }

    private fun createList(): java.util.ArrayList<Order> {
        return arrayListOf(
            Order(
                imageBase64,
                "Sabor Brasileiro",
                "20-12-2023",
                OrderStatus.PREPARING,
                10,
            ), Order(
                imageBase64,
                "Sabor Brasileiro",
                "20-12-2023",
                OrderStatus.TRANSPORTING,
                10,
            ), Order(
                imageBase64,
                "Sabor Brasileiro",
                "20-12-2023",
                OrderStatus.DELIVERED,
                10,
            )
        )
    }
}

class OrdersAdapter(private var data: ArrayList<Order>) :
    RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
    )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val image = itemView.findViewById<ImageView>(R.id.order_item_image)
        private val restaurant = itemView.findViewById<TextView>(R.id.order_item_restaurant)
        private val date = itemView.findViewById<TextView>(R.id.order_item_date)
        private val statusIcon = itemView.findViewById<ImageView>(R.id.order_item_status_icon)
        private val statusText = itemView.findViewById<TextView>(R.id.order_item_status_text)
        private val productsNumber =
            itemView.findViewById<TextView>(R.id.order_item_products_number)

        fun bind(order: Order) {
            restaurant.text = order.restaurant
            date.text = order.date
            statusText.text = order.status.toResourceString(itemView.context)
            image.setImageBitmap(base64ToBitmap(order.image))
            productsNumber.text = order.products_number.toString()
            val color = order.status.toResourceColor()
            statusIcon.setColorFilter(ContextCompat.getColor(itemView.context, color))
        }
    }
}

data class Order(
    val image: String,
    val restaurant: String,
    val date: String,
    val status: OrderStatus,
    val products_number: Int,
)

// Remove base64 header: [data:image/jpeg;base64,]
const val imageBase64 =
    "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"


fun base64ToBitmap(base64: String): Bitmap {
    val bytes = Base64.decode(base64, Base64.DEFAULT)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}