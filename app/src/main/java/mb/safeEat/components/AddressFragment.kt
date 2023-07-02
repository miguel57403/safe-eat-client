package mb.safeEat.components

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import mb.safeEat.R
import mb.safeEat.extensions.Alertable
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api

class AddressFragment(private val navigation: NavigationListener) : Fragment(), AddressListener, Alertable {
    private lateinit var items: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_address, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader(view)
        initAdapter(view)
        initScreenEvents(view)
        loadInitialData()
    }

    private fun initHeader(view: View) {
        val title = view.findViewById<TextView>(R.id.header_title)
        val backButton = view.findViewById<MaterialCardView>(R.id.header_back_button)

        title.text = resources.getString(R.string.t_address)
        backButton.setOnClickListener { navigation.onBackPressed() }
    }

    private fun initAdapter(view: View) {
        items = view.findViewById(R.id.address_items)
        items.layoutManager = LinearLayoutManager(view.context)
        items.adapter = AddressAdapter(this)
    }

    private fun initScreenEvents(view: View) {
        val button = view.findViewById<Button>(R.id.address_submit)
        button.setOnClickListener {
            navigation.navigateTo(AddressRegisterFragment(navigation))
        }
    }

    private fun loadInitialData() {
        suspendToLiveData { api.addresses.findAll() }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { addresses ->
                val initialData = mapInitialData(addresses)
                (items.adapter as AddressAdapter).loadInitialData(initialData)
            }, onFailure = {
                alertError("Error: ${it.message}")
                Log.d("Api Error", "$it")
            })
        }
    }

    private fun mapInitialData(address: List<mb.safeEat.services.api.models.Address>): ArrayList<Address> {
        return address.map { Address(it.name!!, false) }.toCollection(ArrayList())
    }

    override fun onAddressSelected(address: Address) {
        // TODO: Implement this
        Log.d("Click", "Address Clicked Selected")
    }

    override fun onAddressEdit(address: Address) {
        // TODO: Implement this
        Log.d("Click", "Address Clicked Edit")
    }
}

interface AddressListener {
    fun onAddressSelected(address: Address)
    fun onAddressEdit(address: Address)
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
        LayoutInflater.from(parent.context).inflate(R.layout.item_address, parent, false)
    )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    class ViewHolder(private val listener: AddressListener, itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val container = itemView.findViewById<MaterialCardView>(R.id.item_address_container)
        private val text = itemView.findViewById<TextView>(R.id.item_address_text)
        private val icon = itemView.findViewById<ImageView>(R.id.item_address_icon1)
        private val editIcon = itemView.findViewById<MaterialCardView>(R.id.item_address_icon2)

        fun bind(item: Address) {
            text.text = item.text
            if (item.default) {
                icon.setImageResource(R.drawable.baseline_home_24)
            } else {
                icon.setImageResource(R.drawable.baseline_business_24)
            }
            container.setOnClickListener { listener.onAddressSelected(item) }
            editIcon.setOnClickListener { listener.onAddressEdit(item) }
        }
    }
}

data class Address(
    val text: String,
    val default: Boolean
)
