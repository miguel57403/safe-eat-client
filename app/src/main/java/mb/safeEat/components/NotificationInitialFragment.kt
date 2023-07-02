package mb.safeEat.components

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import mb.safeEat.R
import mb.safeEat.extensions.Alertable
import mb.safeEat.extensions.TimeAgo
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api

class NotificationInitialFragment(private val navigation: NavigationListener) : Fragment(),
    Alertable {
    private lateinit var items: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_notification_initial, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter(view)
        loadInitialData()
    }

    private fun initAdapter(view: View) {
        items = view.findViewById(R.id.notification_items)
        items.layoutManager = LinearLayoutManager(view.context)
        items.adapter = NotificationAdapter(navigation)
    }

    private fun loadInitialData() {
        suspendToLiveData { api.notifications.findAll() }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { notifications ->
                val initialData = mapInitialData(notifications)
                (items.adapter as NotificationAdapter).loadInitialData(initialData)
            }, onFailure = {
                alertError("Error: ${it.message}")
                Log.d("Api Error", "$it")
            })
        }
    }

    private fun mapInitialData(notifications: List<mb.safeEat.services.api.models.Notification>): ArrayList<Notification> {
        return notifications.map { notification ->
            Notification(
                restaurant = notification.order?.restaurant?.name ?: "#Erro#",
                arrivalTime = TimeAgo.parse(notification.time!!).toString(),
                imageId = R.drawable.restaurant,
                message = notification.content ?: "#Erro#",
                orderStatus = mapStatus(notification.order?.status)
            )
        }.toCollection(ArrayList())
    }

    private fun createList(): ArrayList<Notification> {
        return arrayListOf(
            Notification(
                "Sabor Brasileiro",
                "30 seconds ago",
                R.drawable.restaurant,
                "Your order has arrived",
                OrderStatus.DELIVERED
            ),
            Notification(
                "Sabor Brasileiro",
                "5 min ago",
                R.drawable.restaurant,
                "Your order is out for delivery",
                OrderStatus.TRANSPORTING
            ),
            Notification(
                "Sabor Brasileiro",
                "15 min ago",
                R.drawable.restaurant,
                "Your order is in preparation",
                OrderStatus.PREPARING
            ),
            Notification(
                "Mimo's Pizza",
                "1 day ago",
                R.drawable.restaurant,
                "Promotion message",
                null,
            ),
            Notification(
                "Gelados Maravilhosos",
                "2 days ago",
                R.drawable.restaurant,
                "Promotion message",
                null,
            ),
            Notification(
                "Sabor Brasileiro",
                "25 Apr at 12:45",
                R.drawable.restaurant,
                "Promotion message",
                null,
            ),
            Notification(
                "Sabor Brasileiro",
                "23 Apr at 12:45",
                R.drawable.restaurant,
                "Promotion message",
                null,
            ),
        )
    }
}

class NotificationAdapter(
    private val navigation: NavigationListener,
) : RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
    private var data = ArrayList<Notification>()

    @SuppressLint("NotifyDataSetChanged")
    fun loadInitialData(newData: ArrayList<Notification>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        navigation,
        LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
    )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    class ViewHolder(private val navigation: NavigationListener, itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val container =
            itemView.findViewById<MaterialCardView>(R.id.restaurant_product_container)
        private val image = itemView.findViewById<ImageView>(R.id.notification_restaurant_image)
        private val restaurant = itemView.findViewById<TextView>(R.id.notification_restaurant_name)
        private val timeArrival = itemView.findViewById<TextView>(R.id.notification_time_arrival)
        private val status = itemView.findViewById<TextView>(R.id.notification_order_status)

        fun bind(item: Notification) {
            image.setImageResource(item.imageId)
            restaurant.text = item.restaurant
            timeArrival.text = item.arrivalTime
            status.text = item.message
            if (item.orderStatus != null) {
                container.setOnClickListener {
                    navigation.navigateTo(
                        OrderDetailFragment(
                            navigation, OrderDetailParams(
                                item.orderStatus,
                                item.restaurant,
                                item.arrivalTime,
                            )
                        )
                    )
                }
            } else {
                container.isClickable = false
            }
        }
    }
}

data class Notification(
    val restaurant: String,
    val arrivalTime: String,
    val imageId: Int,
    val message: String,
    val orderStatus: OrderStatus?,
)

private fun mapStatus(status: String?): OrderStatus {
    return when (status) {
        "PREPARING" -> OrderStatus.PREPARING
        "REGISTERED" -> OrderStatus.PREPARING // TODO: Update OrderStatus
        "TRANSPORTING" -> OrderStatus.TRANSPORTING
        "DELIVERED" -> OrderStatus.DELIVERED
        "CANCELLED" -> OrderStatus.DELIVERED // TODO: Update OrderStatus
        else -> throw Exception("Invalid status")
    }
}
