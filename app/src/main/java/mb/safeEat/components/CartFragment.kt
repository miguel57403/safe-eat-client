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
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mb.safeEat.R
import mb.safeEat.extensions.Alertable
import mb.safeEat.functions.formatPrice
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api
import mb.safeEat.services.state.state
import java.util.ArrayList

class CartFragment(private val navigation: NavigationListener) : Fragment(), Alertable {
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
        items.adapter = CartAdapter()
    }

    @SuppressLint("SetTextI18n")
    private fun initScreenEvents(view: View) {
        val button = view.findViewById<Button>(R.id.cart_button_submit)
        button.setOnClickListener { navigateToPayment() }
    }

    private fun loadInitialData(view: View) {
        suspendToLiveData { api.orders.findDraft() }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { draft ->
                val products = view.findViewById<TextView>(R.id.cart_products_value)
                val subtotal = view.findViewById<TextView>(R.id.cart_products_subtotal_value)

                products.text = draft.quantity.toString()
                subtotal.text = formatPrice("€", draft.subtotal)

                // TODO: load data from api
                (items.adapter as CartAdapter).loadInitialData(createList())
            }, onFailure = {
                alertThrowable(it)
            })
        }
        val cartId = state.user.value!!.cartId!!
        suspendToLiveData { api.carts.findById(cartId) }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {

            }, onFailure = {
                alertThrowable(it)
            })
        }
    }

    private fun navigateToPayment() {
        navigation.navigateTo(PaymentFragment(navigation))
    }

    private fun createList() = arrayListOf(
        Product(product = "Pizza acebolada", amount = 3, price = 14.99, warn = true),
        Product(product = "Pizza 4 queijos", amount = 1, price = 2.99, warn = false),
        Product(product = "Pizza de achova", amount = 2, price = 2.99, warn = false)
    )
}

class CartAdapter : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    private var data: ArrayList<Product> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun loadInitialData(newData: ArrayList<Product>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
    )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val container = itemView.findViewById<ConstraintLayout>(R.id.cart_item_container)
        private val product = itemView.findViewById<TextView>(R.id.cart_item_name)
        private val quantity = itemView.findViewById<TextView>(R.id.cart_item_quantity)
        private val price = itemView.findViewById<TextView>(R.id.cart_item_price)
        private val icon = itemView.findViewById<ImageView>(R.id.cart_item_alert)

        fun bind(item: Product) {
            if (item.warn) {
                container.setBackgroundResource(R.drawable.border_item_warning)
                icon.visibility = View.VISIBLE
            }
            product.text = item.product
            quantity.text = item.amount.toString()
            price.text = formatPrice("€", item.price)
        }
    }
}

data class Product(
    val product: String,
    val amount: Int,
    val price: Double,
    val warn: Boolean,
) {
    fun getTotalPrice(): Double = amount * price
}
