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
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api
import mb.safeEat.services.api.dto.OrderDto
import mb.safeEat.services.api.models.Cart
import mb.safeEat.services.state.state
import java.text.DecimalFormat
import java.util.ArrayList

class CartInitialFragment(private val navigation: NavigationListener) : Fragment() {
    private var orderDto = OrderDto()

    val data = arrayListOf(
        Product(product = "Pizza acebolada", amount = 3, price = 14.99f, warn = true),
        Product(product = "Pizza 4 queijos", amount = 1, price = 2.99f, warn = false),
        Product(product = "Pizza de achova", amount = 2, price = 2.99f, warn = false)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_cart_initial, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData(view)
        initAdapter(view)
        initScreenEvents(view)
    }

    private fun loadData(view: View) {
        val userId = state.user.value?.id
        if (userId != null) {
            suspendToLiveData { api.carts.findByUser(userId) }.observe(viewLifecycleOwner) { result ->
                result.fold(onSuccess = {
                    loadDataByCart(it)
                }, onFailure = {
                    Log.d("Api Error", "$it")
                })
            }

            suspendToLiveData { api.addresses.findAllByUser(userId) }.observe(viewLifecycleOwner) { result ->
                result.fold(onSuccess = {
                    // TODO: Usar os endereços
                }, onFailure = {
                    Log.d("Api Error", "$it")
                })
            }

            suspendToLiveData { api.payments.findAllByUser(userId) }.observe(viewLifecycleOwner) { result ->
                result.fold(onSuccess = {
                    // TODO: Usar os pagamentos
                }, onFailure = {
                    Log.d("Api Error", "$it")
                })
            }
        }
    }

    private fun loadDataByCart(cart: Cart) {
        suspendToLiveData { api.items.findAllByCart(cart.id!!) }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {
                // TODO: Usar os items
            }, onFailure = {
                Log.d("Api Error", "$it")
            })
        }

        suspendToLiveData { api.restaurants.findByCart() }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {
                loadDataByRestaurant(it)
            }, onFailure = {
                Log.d("Api Error", "$it")
            })
        }
    }

    private fun loadDataByRestaurant(restaurant: mb.safeEat.services.api.models.Restaurant) {
        suspendToLiveData { api.deliveries.findByAllRestaurant(restaurant.id!!) }.observe(
            viewLifecycleOwner
        ) { result ->
            result.fold(onSuccess = {
                // TODO: Usar as entregas
            }, onFailure = {
                Log.d("Api Error", "$it")
            })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initScreenEvents(view: View) {
        val productsCount = view.findViewById<TextView>(R.id.cart_products_count)
        val productsPrice = view.findViewById<TextView>(R.id.cart_products_price)
        val button = view.findViewById<Button>(R.id.cart_button_submit)

        val priceUnit = "€"
        val hasWarnings = data.any { it.warn }
        val totalPrice = data.sumOf { it.getTotalPrice().toDouble() }.toFloat()

        productsCount.text = data.size.toString()
        productsPrice.text = priceUnit + formatPrice(totalPrice)

        button.setOnClickListener {
            if (hasWarnings) {
                val dialog = RestrictionAlertDialog()
                dialog.show(navigation.getSupportFragmentManager(), dialog.tag)
                dialog.setOnConfirmListener { confirm ->
                    if (confirm) confirmCart()
                }
            } else {
                confirmCart()
            }
        }
    }

    private fun confirmCart() {
        //suspendToLiveData { api.orders.create(orderDto) }
        navigateToPayment()
    }

    private fun initAdapter(view: View) {
        val items = view.findViewById<RecyclerView>(R.id.cart_items)
        items.layoutManager = LinearLayoutManager(view.context)
        items.adapter = CartAdapter(data)
    }

    private fun navigateToPayment() {
        navigation.navigateTo(PaymentFragment(navigation))
    }
}

class CartAdapter(private var data: ArrayList<Product>) :
    RecyclerView.Adapter<CartAdapter.ViewHolder>() {
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
            price.text = formatPrice(item.price)
        }
    }
}

data class Product(
    val product: String, val amount: Int, val price: Float, val warn: Boolean
) {
    fun getTotalPrice(): Float = amount * price
}

fun formatPrice(price: Float): String = DecimalFormat("#.##").format(price)