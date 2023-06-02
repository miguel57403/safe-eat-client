package mb.safeEat

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
import java.text.DecimalFormat
import java.util.ArrayList

class CartInitialFragment(private val navigation: NavigationListener) : Fragment() {
    val data = arrayListOf(
        Product(product = "Pizza acebolada", amount = 3, price = 14.99f, warn = true),
        Product(product = "Pizza 4 queijos", amount = 1, price = 2.99f, warn = false),
        Product(product = "Pizza de achova", amount = 2, price = 2.99f, warn = false)
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart_initial, container, false)
        if (view != null) onInit(view)
        return view
    }

    private fun onInit(view: View) {
        initAdapter(view)
        initScreenEvents(view)
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
                val dialog = RestrictionAlertDialogFragment()
                dialog.show(navigation.getSupportFragmentManager(), dialog.tag)
                dialog.setOnConfirmListener { confirm -> if (confirm) navigateToPayment() }
            } else {
                navigateToPayment()
            }
        }
    }

    private fun initAdapter(view: View) {
        val items = view.findViewById<RecyclerView>(R.id.cart_items)
        items.layoutManager = LinearLayoutManager(view.context)
        items.adapter = CartAdapter(data)
    }

    private fun navigateToPayment() {
        navigation.navigateTo(PaymentActivity(navigation))
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
    val product: String,
    val amount: Int,
    val price: Float,
    val warn: Boolean
) {
    fun getTotalPrice(): Float = amount * price
}

fun formatPrice(price: Float): String = DecimalFormat("#.##").format(price)