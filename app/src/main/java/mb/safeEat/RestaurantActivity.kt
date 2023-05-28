package mb.safeEat

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class RestaurantActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)
        initAdapter()

        // TODO: Receive values by arguments
        val restaurantNameArg = "Sabor Brasileiro"
        val deliveryPriceArg = "Delivery €2,99"
        val deliveryIntervalArg = "20 - 30 min"

        val posterImage = findViewById<ImageView>(R.id.restaurant_poster_image)
        val deliveryInterval = findViewById<TextView>(R.id.restaurant_delivery_interval)
        val deliveryPrice = findViewById<TextView>(R.id.restaurant_delivery_price)
        val restaurantName = findViewById<TextView>(R.id.restaurant_name)
        val restaurantImage = findViewById<ImageView>(R.id.restaurant_image)
        val searchButton = findViewById<MaterialCardView>(R.id.restaurant_search_button)
        val backButton = findViewById<MaterialCardView>(R.id.restaurant_back_button)

        posterImage.setImageResource(R.drawable.sandwich)
        deliveryInterval.text = deliveryIntervalArg
        deliveryPrice.text = deliveryPriceArg
        restaurantName.text = restaurantNameArg
        restaurantImage.setImageResource(R.drawable.restaurant)
        searchButton.setOnClickListener { Log.d("Click", "Search button") }
        backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun initAdapter() {
        val items = findViewById<RecyclerView>(R.id.restaurant_items)
        items.layoutManager = LinearLayoutManager(this)
        items.adapter = RestaurantCategoryAdapter(createList())
    }

    private fun createList(): ArrayList<RestaurantCategory> {
        return arrayListOf(
            RestaurantCategory(
                "Pizza", arrayListOf(
                    RestaurantProduct(R.drawable.pizza, "Moda da casa", "€2,99", true),
                    RestaurantProduct(R.drawable.pizza, "Presunto e Queijo", "€2,99", false),
                    RestaurantProduct(R.drawable.pizza, "Marguerita", "€2,99", false),
                    RestaurantProduct(R.drawable.pizza, "Frango com Catupiri", "€2,99", false),
                )
            ), RestaurantCategory(
                "Sandwich", arrayListOf(
                    RestaurantProduct(R.drawable.sandwich, "Misto Quente", "€2,99", false),
                    RestaurantProduct(R.drawable.sandwich, "X-Tudo", "€2,99", true),
                    RestaurantProduct(R.drawable.sandwich, "X-Salada", "€2,99", false),
                    RestaurantProduct(R.drawable.sandwich, "X-Bacon", "€2,99", false),
                )
            ), RestaurantCategory(
                "Drinks", arrayListOf(
                    RestaurantProduct(R.drawable.drinks, "Coca-Cola", "€2,99", false),
                    RestaurantProduct(R.drawable.drinks, "Sprite", "€2,99", true),
                    RestaurantProduct(R.drawable.drinks, "Cerveja", "€2,99", false),
                )
            )
        )
    }
}

class RestaurantCategoryAdapter(private val data: ArrayList<RestaurantCategory>) :
    RecyclerView.Adapter<RestaurantCategoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        parent.context,
        LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant, parent, false)
    )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    class ViewHolder(val context: Context, itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val category = itemView.findViewById<TextView>(R.id.restaurant_category_name)
        private val items = itemView.findViewById<RecyclerView>(R.id.restaurant_category_items)
        fun bind(item: RestaurantCategory) {
            category.text = item.name
            initAdapter(item)
        }

        private fun initAdapter(item: RestaurantCategory) {
            val adapter = RestaurantProductAdapter(item.products)
            items.layoutManager = LinearLayoutManager(context).apply {
                orientation = RecyclerView.HORIZONTAL
            }
            items.adapter = adapter
        }
    }
}

class RestaurantProductAdapter(private val data: ArrayList<RestaurantProduct>) :
    RecyclerView.Adapter<RestaurantProductAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_restaurant_product, parent, false)
    )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val container =
            itemView.findViewById<MaterialCardView>(R.id.restaurant_product_container)
        private val product = itemView.findViewById<TextView>(R.id.restaurant_product_name)
        private val price = itemView.findViewById<TextView>(R.id.restaurant_product_price)
        private val image = itemView.findViewById<ImageView>(R.id.restaurant_product_image)
        private val warning = itemView.findViewById<ImageView>(R.id.restaurant_product_warning)

        fun bind(item: RestaurantProduct) {
            product.text = item.name
            price.text = item.price
            image.setImageResource(item.imageId)

            if (item.restricted) {
                warning.visibility = ImageView.VISIBLE
                container.strokeColor = ContextCompat.getColor(itemView.context, R.color.red)
                container.strokeWidth = dp2px(itemView.context, 2.0f).toInt()
            } else {
                warning.visibility = ImageView.GONE
                container.strokeColor =
                    ContextCompat.getColor(itemView.context, android.R.color.transparent)
                container.strokeWidth = dp2px(itemView.context, 0f).toInt()
            }
        }
    }
}

fun dp2px(context: Context, dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        context.resources.displayMetrics
    )
}

data class RestaurantProduct(
    val imageId: Int,
    val name: String,
    val price: String,
    val restricted: Boolean
)

data class RestaurantCategory(
    val name: String,
    val products: ArrayList<RestaurantProduct>
)