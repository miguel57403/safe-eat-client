package mb.safeEat.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import mb.safeEat.R
import mb.safeEat.activities.NavigationListener
import mb.safeEat.extensions.Alertable
import mb.safeEat.extensions.TimeAgo
import mb.safeEat.functions.formatPrice
import mb.safeEat.functions.initHeader
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api
import mb.safeEat.services.api.models.*
import mb.safeEat.services.api.models.Order

data class OrderDetailParams(val orderId: String)

class OrderDetailFragment(
    private val navigation: NavigationListener,
    private val params: OrderDetailParams,
) : Fragment(), Alertable {
    private lateinit var items: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_order_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader(view, navigation, R.string.t_order)
        initAdapter(view)
        loadInitialData(view)
    }

    private fun initAdapter(view: View) {
        items = view.findViewById(R.id.order_detail_items)
        items.layoutManager = LinearLayoutManager(view.context)
        items.adapter = OrderDetailAdapter()
    }

    private fun loadInitialData(view: View) {
        suspendToLiveData { api.orders.findById(params.orderId) }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { order ->
                updateUi(view, order)
                val initialData = mapInitialData(order.items!!)
                (items.adapter as OrderDetailAdapter).loadInitialData(initialData)
            }, onFailure = {
                alertThrowable(it)
            })
        }
    }

    private fun updateUi(view: View, order: Order) {
        val image = view.findViewById<ImageView>(R.id.order_detail_restaurant_image)
        val restaurant = view.findViewById<TextView>(R.id.order_detail_restaurant_name)
        val date = view.findViewById<TextView>(R.id.order_detail_date)
        val status = view.findViewById<TextView>(R.id.order_detail_status)
        val progressBar = view.findViewById<ProgressBar>(R.id.order_detail_progress_bar)
        val buttonFeedback = view.findViewById<Button>(R.id.order_detail_button_feedback)
        val subTotal = view.findViewById<TextView>(R.id.order_detail_products_count)
        val total = view.findViewById<TextView>(R.id.order_detail_products_price)

        val orderStatus = OrderStatus.fromApi(order.status)

        subTotal.text = formatPrice("€", order.subtotal)
        total.text = formatPrice("€", order.total)
        restaurant.text = order.restaurant?.name ?: ""
        date.text = TimeAgo.parse(order.time!!).toString()
        status.text = orderStatus.toResourceString(view.context)
        progressBar.progressDrawable.setTint(
            ContextCompat.getColor(view.context, orderStatus.toResourceColor())
        )
        progressBar.progressDrawable.setTintMode(PorterDuff.Mode.SRC_ATOP)
        progressBar.progress = orderStatus.toProgress()
        if (orderStatus == OrderStatus.DELIVERED) {
            buttonFeedback.setOnClickListener {
                val params = FeedbackParams(orderId = order.id)
                navigation.navigateTo(FeedbackFragment(navigation, params))
            }
            buttonFeedback.visibility = View.VISIBLE
        } else {
            buttonFeedback.visibility = View.GONE
        }

        Glide.with(view) //
            .load(order.restaurant?.logo) //
            .apply(RequestOptions().centerCrop()) //
            .transition(DrawableTransitionOptions.withCrossFade()) //
            .into(image)
    }

    private fun mapInitialData(listOrderItem: List<Item>): ArrayList<OrderItem> {
        return listOrderItem.map { orderItem ->
            OrderItem(
                quantity = orderItem.quantity ?: 0,
                product = orderItem.product?.name ?: "",
                price = formatPrice("€", orderItem.product?.price)
            )
        }.toCollection(ArrayList())
    }

    @Suppress("unused")
    private fun createMockData(): ArrayList<OrderItem> {
        return arrayListOf(
            OrderItem(10, "Hot-dog combo", "€10,00"),
            OrderItem(10, "Hot-dog combo", "€10,00"),
            OrderItem(10, "Hot-dog combo", "€10,00"),
        )
    }
}

class OrderDetailAdapter : RecyclerView.Adapter<OrderDetailAdapter.ViewHolder>() {
    private var data = ArrayList<OrderItem>()

    @SuppressLint("NotifyDataSetChanged")
    fun loadInitialData(newData: ArrayList<OrderItem>) {
        data = newData
        notifyDataSetChanged()
    }

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
            quantity.text =
                item.quantity.toString() + itemView.resources.getString(R.string.r_x_times)
            product.text = item.product
            price.text = item.price
        }
    }
}

data class OrderItem(
    val quantity: Int,
    val product: String,
    val price: String,
)

enum class OrderStatus {
    REGISTERED, PREPARING, TRANSPORTING, DELIVERED, CANCELED;

    fun toResourceString(context: Context): String = when (this) {
        REGISTERED -> context.resources.getString(R.string.t_registered)
        PREPARING -> context.resources.getString(R.string.t_preparing)
        TRANSPORTING -> context.resources.getString(R.string.t_transporting)
        DELIVERED -> context.resources.getString(R.string.t_delivered)
        CANCELED -> context.resources.getString(R.string.t_canceled)
    }

    @ColorRes
    fun toResourceColor(): Int = when (this) {
        REGISTERED -> R.color.orange_500
        PREPARING -> R.color.orange_500
        TRANSPORTING -> R.color.orange_500
        DELIVERED -> R.color.green_500
        CANCELED -> R.color.red_500
    }

    fun toProgress(): Int = when (this) {
        REGISTERED -> 25
        PREPARING -> 50
        TRANSPORTING -> 75
        DELIVERED -> 100
        CANCELED -> 100
    }

    companion object {
        fun fromApi(status: String?): OrderStatus {
            return when (status) {
                "REGISTERED" -> REGISTERED
                "PREPARING" -> PREPARING
                "TRANSPORTING" -> TRANSPORTING
                "DELIVERED" -> DELIVERED
                "CANCELLED" -> CANCELED
                else -> throw Exception("Invalid status")
            }
        }
    }
}
