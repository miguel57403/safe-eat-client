package mb.safeEat.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import mb.safeEat.R
import mb.safeEat.activities.NavigationListener
import mb.safeEat.extensions.Alertable
import mb.safeEat.extensions.DataStateIndicator
import mb.safeEat.functions.initHeader
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api

class PaymentOptionsFragment(private val navigation: NavigationListener) : Fragment(),
    PaymentListener, Alertable {
    private lateinit var items: RecyclerView
    private lateinit var dataStateIndicator: DataStateIndicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_payment_options, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataStateIndicator = DataStateIndicator(view)
        initHeader(view, navigation, R.string.t_payment)
        initAdapter(view)
        initScreenEvents(view)
        loadInitialData()
    }

    private fun initAdapter(view: View) {
        items = view.findViewById(R.id.payment_select_items)
        items.layoutManager = LinearLayoutManager(view.context)
        items.adapter = PaymentAdapter(this)
    }

    private fun initScreenEvents(view: View) {
        val button = view.findViewById<Button>(R.id.payment_select_submit)
        button.setOnClickListener {
            navigation.navigateTo(PaymentDetailFragment(navigation))
        }
    }

    private fun loadInitialData() {
        dataStateIndicator.showLoading()
        suspendToLiveData { api.payments.findMe() }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { payments ->
                dataStateIndicator.toggle(payments.isNotEmpty())
                val initialData = mapInitialData(payments)
                (items.adapter as PaymentAdapter).loadInitialData(initialData)
            }, onFailure = {
                dataStateIndicator.showError()
                alertThrowable(it)
            })
        }
    }

    private fun mapInitialData(payment: List<mb.safeEat.services.api.models.Payment>): ArrayList<Payment> {
        return payment.map {
            Payment(
                id = it.id,
                name = it.name!!,
                isSelected = false,
            )
        }.toCollection(ArrayList())
    }

    override fun onPaymentSelected(payment: Payment) {
        // TODO: Implement onPaymentSelected
        Log.d("Click", "Payment Clicked Selected")
    }
}

interface PaymentListener {
    fun onPaymentSelected(payment: Payment)
}

class PaymentAdapter(private val listener: PaymentListener) :
    RecyclerView.Adapter<PaymentAdapter.ViewHolder>() {
    private var data = ArrayList<Payment>()

    @SuppressLint("NotifyDataSetChanged")
    fun loadInitialData(newData: ArrayList<Payment>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        listener, LayoutInflater.from(parent.context).inflate(R.layout.item_payment, parent, false)
    )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    class ViewHolder(private val listener: PaymentListener, itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val container = itemView.findViewById<MaterialCardView>(R.id.item_payment_container)
        private val text = itemView.findViewById<TextView>(R.id.item_payment_text)
        private val icon = itemView.findViewById<ImageView>(R.id.item_payment_icon1)

        fun bind(item: Payment) {
            text.text = item.name
            if (!item.isSelected) {
                icon.imageTintList = ContextCompat.getColorStateList(itemView.context, android.R.color.transparent)
            }
            container.setOnClickListener { listener.onPaymentSelected(item) }
        }
    }
}

data class Payment(
    val id: String,
    val name: String,
    val isSelected: Boolean,
)