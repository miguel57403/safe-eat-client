package mb.safeEat.components

import android.icu.util.TimeUnit
import android.os.Bundle
import android.text.format.DateFormat
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
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api
import mb.safeEat.services.api.authorization
import mb.safeEat.services.state.state
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class NotificationInitialFragment(private val navigation: NavigationListener) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notification_initial, container, false)
        if (view != null) onInit(view)
        return view
    }

    private fun mapStatus(status: String?): OrderStatus {
        return when (status) {
            "PREPARING" -> OrderStatus.PREPARING
            "REGISTERED" -> OrderStatus.PREPARING // TODO: Deve criar o status registered
            "TRANSPORTING" -> OrderStatus.TRANSPORTING
            "DELIVERED" -> OrderStatus.DELIVERED
            "CANCELLED" -> OrderStatus.DELIVERED // TODO: Deve criar o status cancelled
            else -> {
                throw Exception("Invalid status")
            }
        }
    }



    private class TimeAgo ( moment: String, value: Long) {
        var moment = moment;
        var value = value;
        override fun toString(): String {
            if(moment == "minute" && value < 1) {
                return "Just now"
            }
            return value.toString() + " " + moment.toString() +" ago"
        }
    }

    // default x years ago
    // > ano = x months ago
    // > mes = x days ago
    // > dia = x hours ago
    // > hora = x minutes ago
    // > minuto = just now


    private fun testeDate(date: String?) {
        val dateConvert = timeAgo(date).toString()
        Log.d("data inserida: "+ date + " agora: " + LocalDateTime.now() + " conversão = ", dateConvert)
    }

    private fun timeAgo(date: String?) : TimeAgo {

        if (date == null) {
            throw Exception("Tempo da notificação não especificada")
        }

        val today = LocalDateTime.now()
        val notifyDate = LocalDateTime.parse(date)

        val day = ChronoUnit.DAYS.between(notifyDate,today)
        val month = ChronoUnit.MONTHS.between(notifyDate,today)
        val year = ChronoUnit.YEARS.between(notifyDate,today)
        val minute = ChronoUnit.MINUTES.between(notifyDate,today)
        val hour = ChronoUnit.HOURS.between(notifyDate,today)


        if( year > 0 ) {
            if( year > 1 ) {
                return TimeAgo("years", year)
            }
            return TimeAgo("year", year)
        }

        if( month > 0 ) {
            if( month > 1) {
                return TimeAgo("months", month)
            }
            return TimeAgo("month", month)
        }

        if( day > 0 ) {
            if( day > 1 ) {
                return TimeAgo("days", day)
            }
            return TimeAgo("day", day)
        }

        if( hour > 0 ) {
            if( hour > 1 ) {
                return TimeAgo("hours", hour)
            }
            return TimeAgo("hour", hour)
        }

        if( minute > 1 ) {
            return TimeAgo("minutes", minute)
        }

        return TimeAgo("minute", minute)
    }


    private fun testeTimeAgo() {

        val today = LocalDateTime.now().toString()
        testeDate(today)
        testeDate("2023-07-02T00:00:00.357")
        testeDate("2023-07-01T20:00:42.357")
        testeDate("2023-06-25T23:22:42.357")
        testeDate("2023-01-01T09:22:42.357")
        testeDate("2021-07-01T09:22:42.357")
    }


    private fun onInit(view: View) {
        testeTimeAgo()
        suspendToLiveData { api.notifications.findAll() }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { notifications ->
                val results = notifications
                    .map { notification -> Notification(
                        restaurant = notification.order?.restaurant?.name ?: "#Erro#",
                        arrivalTime = timeAgo(notification.time.toString()).toString(),
                        imageId = R.drawable.restaurant,
                        message = notification.content ?: "#Erro#",
                        orderStatus = mapStatus(notification.order?.status)
                    ) }
                Log.d("notification", "$notifications")
                initAdapter(view, ArrayList(results))
            }, onFailure = {
//                alertError("Internet Connection Error")
                Log.d("Api Error", "$it")
            })
        }
    }




    private fun initAdapter(view: View, list: ArrayList<Notification>) {
        val adapter = NotificationAdapter(navigation, list)
        val items = view.findViewById<RecyclerView>(R.id.notification_items)
        items.layoutManager = LinearLayoutManager(view.context)
        items.adapter = adapter
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
//            Notification(
//                "Sabor Brasileiro",
//                "5 min ago",
//                R.drawable.restaurant,
//                "Your order is out for delivery",
//                OrderStatus.TRANSPORTING
//            ),
//            Notification(
//                "Sabor Brasileiro",
//                "15 min ago",
//                R.drawable.restaurant,
//                "Your order is in preparation",
//                OrderStatus.PREPARING
//            ),
//            Notification(
//                "Mimo's Pizza",
//                "1 day ago",
//                R.drawable.restaurant,
//                "Promotion message",
//                null
//            ),
//            Notification(
//                "Gelados Maravilhosos",
//                "2 days ago",
//                R.drawable.restaurant,
//                "Promotion message",
//                null
//            ),
//            Notification(
//                "Sabor Brasileiro",
//                "25 Apr at 12:45",
//                R.drawable.restaurant,
//                "Promotion message",
//                null
//            ),
//            Notification(
//                "Sabor Brasileiro",
//                "23 Apr at 12:45",
//                R.drawable.restaurant,
//                "Promotion message",
//                null
//            ),
        )
    }
}

class NotificationAdapter(
    private val navigation: NavigationListener,
    private val data: ArrayList<Notification>
) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
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
                                item.arrivalTime
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
