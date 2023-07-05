package mb.safeEat.components

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import mb.safeEat.R
import mb.safeEat.extensions.Alertable
import mb.safeEat.extensions.DataStateIndicator
import mb.safeEat.extensions.TimeAgo
import mb.safeEat.functions.initHeader
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api
import mb.safeEat.services.state.state

class OrdersFragment(private val navigation: NavigationListener) : Fragment(), Alertable {
    private lateinit var items: RecyclerView
    private lateinit var dataStateIndicator: DataStateIndicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_orders, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataStateIndicator = DataStateIndicator(view)
        initHeader(view, navigation, R.string.t_orders)
        initAdapter(view)
        loadInitialData()
    }

    private fun initAdapter(view: View) {
        items = view.findViewById(R.id.orders_items)
        items.layoutManager = LinearLayoutManager(view.context)
        items.adapter = OrdersAdapter()
    }

    private fun loadInitialData() {
        dataStateIndicator.showLoading()
        val userId = state.user.value!!.id
        suspendToLiveData { api.orders.findAllByUser(userId) }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { orders ->
                dataStateIndicator.toggle(orders.isNotEmpty())
                val initialData = mapInitialData(orders)
                (items.adapter as OrdersAdapter).loadInitialData(initialData)
            }, onFailure = {
                dataStateIndicator.showError()
                alertThrowable(it)
            })
        }
    }

    private fun mapInitialData(order: List<mb.safeEat.services.api.models.Order>): ArrayList<Order> {
        return order.map {
            Order(
                imageUrl = it.restaurant?.logo ?: "",
                restaurant = it.restaurant?.name ?: "",
                date = TimeAgo.parse(it.time!!).toString(),
                status = OrderStatus.DELIVERED, // TODO: Set correct status
                products_number = it.quantity!!
            )
        }.toCollection(ArrayList())
    }

    @Suppress("unused")
    private fun createMockData(): ArrayList<Order> {
        return arrayListOf(
            Order("", "Sabor Brasileiro", "20-12-2023", OrderStatus.REGISTERED, 10),
            Order("", "Sabor Brasileiro", "20-12-2023", OrderStatus.PREPARING, 10),
            Order("", "Sabor Brasileiro", "20-12-2023", OrderStatus.TRANSPORTING, 10),
            Order("", "Sabor Brasileiro", "20-12-2023", OrderStatus.DELIVERED, 10),
            Order("", "Sabor Brasileiro", "20-12-2023", OrderStatus.CANCELED, 10)
        )
    }
}

class OrdersAdapter : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {
    private var data = ArrayList<Order>()

    @SuppressLint("NotifyDataSetChanged")
    fun loadInitialData(newData: ArrayList<Order>) {
        data = newData
        notifyDataSetChanged()
    }

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

        fun bind(item: Order) {
            restaurant.text = item.restaurant
            date.text = item.date
            statusText.text = item.status.toResourceString(itemView.context)
            productsNumber.text = item.products_number.toString()
            statusIcon.setColorFilter(
                ContextCompat.getColor(itemView.context, item.status.toResourceColor())
            )
            Glide.with(itemView) //
                .load(item.imageUrl) //
                .apply(RequestOptions.centerCropTransform()) //
                .into(image)
        }
    }
}

data class Order(
    val imageUrl: String,
    val restaurant: String,
    val date: String,
    val status: OrderStatus,
    val products_number: Int,
)
