package mb.safeEat.components

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mb.safeEat.R
import mb.safeEat.extensions.Alertable
import mb.safeEat.functions.formatPrice
import mb.safeEat.functions.hideNoData
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api
import mb.safeEat.services.api.dto.ItemDto
import kotlin.collections.ArrayList

class CartFragment(private val navigation: NavigationListener) : Fragment(), Alertable,
    CartListener {
    private lateinit var items: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_cart, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter(view)
        initScreenEvents(view)
        loadInitialData(view)
    }

    private fun initAdapter(view: View) {
        items = view.findViewById(R.id.cart_items)
        items.layoutManager = LinearLayoutManager(view.context)
        items.adapter = CartAdapter(this)
    }

    @SuppressLint("SetTextI18n")
    private fun initScreenEvents(view: View) {
        val button = view.findViewById<Button>(R.id.cart_button_submit)
        button.setOnClickListener { navigateToPayment() }
    }

    private fun loadInitialData(view: View) {
        val products = view.findViewById<TextView>(R.id.cart_products_value)
        val subtotal = view.findViewById<TextView>(R.id.cart_products_subtotal_value)

        suspendToLiveData { api.carts.findMe() }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { cart ->
                products.text = cart.quantity.toString()
                subtotal.text = formatPrice("€", cart.subtotal)

                if (cart.quantity!! > 0) {
                    hideNoData(view)
                    val initialData = mapInitialData(cart.items ?: listOf())
                    (items.adapter as CartAdapter).loadInitialData(initialData)
                }
            }, onFailure = {
                products.text = "0"
                subtotal.text = formatPrice("€", 0.0)
                alertThrowable(it)
            })
        }
    }

    private fun mapInitialData(items: List<mb.safeEat.services.api.models.Item>): ArrayList<Product> {
        return items.map {
            Product(
                id = it.id!!,
                amount = it.quantity!!,
                productId = it.product!!.id!!,
                product = it.product.name!!,
                price = it.product.price!!,
                warn = it.product.isRestricted!!
            )
        }.toCollection(ArrayList())
    }

    private fun navigateToPayment() {
        navigation.navigateTo(CartPaymentFragment(navigation))
    }

    @Suppress("unused")
    private fun createMockData(): ArrayList<Product> {
        return arrayListOf(
            Product("", "", "Pizza acebolada", 3, 14.99, true),
            Product("", "", "Pizza 4 queijos", 1, 2.99, false),
            Product("", "", "Pizza de achova", 2, 2.99, false),
        )
    }

    override fun onItemDelete(item: Product) {
        suspendToLiveData { api.items.delete(item.id) }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {
                loadInitialData(requireView())
            }, onFailure = {
                alertThrowable(it)
            })
        }
    }

    override fun onItemQuantityChange(item: Product) {
        val body = ItemDto(id = item.id, productId = item.productId, quantity = item.amount)
        suspendToLiveData { api.items.update(body) }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {
                loadInitialData(requireView())
            }, onFailure = {
                alertThrowable(it)
            })
        }
    }
}

interface CartListener {
    fun onItemDelete(item: Product)
    fun onItemQuantityChange(item: Product)
}

class CartAdapter(private val listener: CartListener) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    private var data: ArrayList<Product> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun loadInitialData(newData: ArrayList<Product>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        listener, LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
    )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    class ViewHolder(private val listener: CartListener, itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val container = itemView.findViewById<ConstraintLayout>(R.id.cart_item_container)
        private val product = itemView.findViewById<TextView>(R.id.cart_item_name)
        private val quantity = itemView.findViewById<TextView>(R.id.cart_item_quantity)
        private val price = itemView.findViewById<TextView>(R.id.cart_item_price)
        private val icon = itemView.findViewById<ImageView>(R.id.cart_item_alert)
        private val delete = itemView.findViewById<ImageView>(R.id.cart_item_button_delete)

        fun bind(item: Product) {
            if (item.warn) {
                container.setBackgroundResource(R.drawable.border_item_warning)
                icon.visibility = View.VISIBLE
                icon.imageTintList = itemView.context.getColorStateList(R.color.red_500)
            }
            product.text = item.product
            quantity.text = item.amount.toString()
            price.text = formatPrice("€", item.price)
            delete.setOnClickListener { listener.onItemDelete(item) }
            // TODO: Call listener.onItemQuantityChange()
        }
    }
}

data class Product(
    val id: String,
    val productId: String,
    val product: String,
    val amount: Int,
    val price: Double,
    val warn: Boolean,
)
