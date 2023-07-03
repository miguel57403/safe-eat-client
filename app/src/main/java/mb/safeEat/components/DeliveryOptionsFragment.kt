package mb.safeEat.components

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import mb.safeEat.extensions.Alertable

data class DeliveryOptionsParams(
    val deliveryOptions: ArrayList<DeliveryOption>,
)

class DeliveryOptionsFragment(
    private val navigation: NavigationListener,
    private val params: DeliveryOptionsParams,
) : Fragment(), DeliveryOptionsListener, Alertable {
    private lateinit var items: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_delivery_options, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader(view)
        initAdapter(view)
        loadInitialData()
    }

    private fun initHeader(view: View) {
        val title = view.findViewById<TextView>(R.id.header_title)
        val backButton = view.findViewById<MaterialCardView>(R.id.header_back_button)

        title.text = resources.getString(R.string.t_delivery_option)
        backButton.setOnClickListener { navigation.onBackPressed() }
    }

    private fun initAdapter(view: View) {
        items = view.findViewById(R.id.delivery_options_items)
        items.layoutManager = LinearLayoutManager(view.context)
        items.adapter = DeliveryOptionsAdapter(this)
    }

    private fun loadInitialData() {
        (items.adapter as DeliveryOptionsAdapter).loadInitialData(params.deliveryOptions)
    }

    override fun onDeliveryOptionSelected(item: DeliveryOption) {
        // TODO: Implement onDeliveryOptionSelected
        Log.d("Click", "Selected")
    }
}

interface DeliveryOptionsListener {
    fun onDeliveryOptionSelected(item: DeliveryOption)
}

class DeliveryOptionsAdapter(private val listener: DeliveryOptionsListener) :
    RecyclerView.Adapter<DeliveryOptionsAdapter.ViewHolder>() {
    private var data = ArrayList<DeliveryOption>()

    @SuppressLint("NotifyDataSetChanged")
    fun loadInitialData(newData: ArrayList<DeliveryOption>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        listener,
        LayoutInflater.from(parent.context).inflate(R.layout.item_delivery_option, parent, false)
    )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    class ViewHolder(private val listener: DeliveryOptionsListener, itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val container =
            itemView.findViewById<MaterialCardView>(R.id.item_delivery_option_container)
        private val text = itemView.findViewById<TextView>(R.id.item_delivery_option_text)
        private val icon = itemView.findViewById<ImageView>(R.id.item_delivery_option_icon)

        fun bind(item: DeliveryOption) {
            text.text = item.name
            container.setOnClickListener { listener.onDeliveryOptionSelected(item) }
            if (!item.isSelected) {
                icon.imageTintList = ContextCompat.getColorStateList(itemView.context, android.R.color.transparent)
            }
        }
    }
}

data class DeliveryOption(
    val id: String,
    val name: String,
    val isSelected: Boolean
)
