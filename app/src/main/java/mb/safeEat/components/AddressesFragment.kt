package mb.safeEat.components

import android.annotation.SuppressLint
import android.os.Bundle
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
import mb.safeEat.extensions.Alertable
import mb.safeEat.extensions.DataStateIndicator
import mb.safeEat.functions.initHeader
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api


enum class AddressAction {
    GO_REGISTER, DO_SELECT
}

class AddressesFragment(
    private val navigation: NavigationListener,
    private val action: AddressAction,
) : Fragment(), Alertable, AddressListener {
    private lateinit var items: RecyclerView
    private lateinit var dataStateIndicator: DataStateIndicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_addresses, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataStateIndicator = DataStateIndicator(view)
        initHeader(view, navigation, R.string.t_address)
        initAdapter(view)
        initScreenEvents(view)
        loadInitialData()
    }

    private fun initAdapter(view: View) {
        items = view.findViewById(R.id.address_items)
        items.layoutManager = LinearLayoutManager(view.context)
        items.adapter = AddressAdapter(this)
    }

    private fun initScreenEvents(view: View) {
        val button = view.findViewById<Button>(R.id.address_submit)
        button.setOnClickListener {
            val params = AddressRegisterParams(null)
            navigation.navigateTo(AddressRegisterFragment(navigation, params))
        }
    }

    private fun loadInitialData() {
        dataStateIndicator.showLoading()
        suspendToLiveData { api.addresses.findMe() }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { addresses ->
                dataStateIndicator.toggle(addresses.isNotEmpty())
                val initialData = mapInitialData(addresses)
                (items.adapter as AddressAdapter).loadInitialData(initialData)
            }, onFailure = {
                dataStateIndicator.showError()
                alertThrowable(it)
            })
        }
    }

    private fun mapInitialData(address: List<mb.safeEat.services.api.models.Address>): ArrayList<Address> {
        return address.map {
            Address(
                id = it.id,
                name = it.name!!,
                street = it.street!!,
                number = it.number!!,
                complement = it.complement!!,
                city = it.city!!,
                postalCode = it.postalCode!!,
                isSelected = it.isSelected!!,
            )
        }.toCollection(ArrayList())
    }

    override fun onAddressClicked(address: Address) {
        when (action) {
            AddressAction.DO_SELECT -> {
                suspendToLiveData { api.addresses.select(address.id) }.observe(viewLifecycleOwner) { result ->
                    result.fold(onSuccess = {
                        navigation.onBackPressed()
                    }, onFailure = {
                        alertThrowable(it)
                    })
                }
            }

            AddressAction.GO_REGISTER -> {
                val params = AddressRegisterParams(address)
                navigation.navigateTo(AddressRegisterFragment(navigation, params))
            }
        }
    }
}

interface AddressListener {
    fun onAddressClicked(address: Address)
}

class AddressAdapter(private val listener: AddressListener) :
    RecyclerView.Adapter<AddressAdapter.ViewHolder>() {
    private var data = ArrayList<Address>()

    @SuppressLint("NotifyDataSetChanged")
    fun loadInitialData(newData: ArrayList<Address>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        listener,
        LayoutInflater.from(parent.context).inflate(R.layout.item_address, parent, false),
    )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    class ViewHolder(private val listener: AddressListener, itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val container = itemView.findViewById<MaterialCardView>(R.id.item_address_container)
        private val text = itemView.findViewById<TextView>(R.id.item_address_text)
        private val icon = itemView.findViewById<ImageView>(R.id.item_address_icon1)

        fun bind(item: Address) {
            text.text = item.name
            if (!item.isSelected) {
                icon.imageTintList =
                    ContextCompat.getColorStateList(itemView.context, android.R.color.transparent)
            }
            container.setOnClickListener { listener.onAddressClicked(item) }
        }
    }
}

data class Address(
    val id: String,
    val name: String,
    val street: String,
    val number: String,
    val complement: String,
    val city: String,
    val postalCode: String,
    val isSelected: Boolean,
)
