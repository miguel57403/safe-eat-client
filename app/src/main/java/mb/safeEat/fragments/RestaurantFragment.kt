package mb.safeEat.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.card.MaterialCardView
import mb.safeEat.R
import mb.safeEat.activities.NavigationListener
import mb.safeEat.extensions.Alertable
import mb.safeEat.functions.formatPrice
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api
import mb.safeEat.services.api.models.ProductSection

data class RestaurantParams(val restaurantId: String)

class RestaurantFragment(
    private val navigation: NavigationListener,
    private val params: RestaurantParams,
) : Fragment(), Alertable {
    private lateinit var items: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_restaurant, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter(view)
        loadInitialData(view)
    }

    private fun loadInitialData(view: View) {
        loadRestaurantData(view)
        loadProductsSectionsData()
    }

    private fun loadRestaurantData(view: View) {
        val posterImage = view.findViewById<ImageView>(R.id.restaurant_poster_image)
        val deliveryInterval = view.findViewById<TextView>(R.id.restaurant_delivery_interval)
        val deliveryPrice = view.findViewById<TextView>(R.id.restaurant_delivery_price)
        val restaurantName = view.findViewById<TextView>(R.id.restaurant_name)
        val restaurantImage = view.findViewById<ImageView>(R.id.restaurant_image)

        val searchButton = view.findViewById<MaterialCardView>(R.id.restaurant_search_button)
        val backButton = view.findViewById<MaterialCardView>(R.id.restaurant_back_button)

        searchButton.setOnClickListener {
            val params = SearchProductParams(restaurantId = params.restaurantId)
            navigation.navigateTo(SearchProductFragment(navigation, params))
        }
        backButton.setOnClickListener { navigation.onBackPressed() }

        suspendToLiveData {
            api.restaurants.findById(params.restaurantId)
        }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { restaurant ->
                restaurantName.text = restaurant.name
                deliveryInterval.text = restaurant.formattedDeliveryInterval()
                deliveryPrice.text = restaurant.formattedDeliveryPrice("€")
                Glide.with(view) //
                    .load(restaurant.logo) //
                    .apply(RequestOptions.centerInsideTransform()) //
                    .transition(DrawableTransitionOptions.withCrossFade()) //
                    .into(restaurantImage)

                // TODO: Remove development flag
                val development = false
                if (development) {
                    Glide.with(view) //
                        .load(restaurant.cover) //
                        .apply(RequestOptions.centerInsideTransform()) //
                        .transition(DrawableTransitionOptions.withCrossFade()) //
                        .into(posterImage)

                } else {
                    posterImage.setBackgroundResource(R.drawable.restaurant_cover)
                }
            }, onFailure = {
                alertThrowable(it)
            })
        }
    }

    private fun loadProductsSectionsData() {
        suspendToLiveData {
            api.productSections.findAllByRestaurant(params.restaurantId)
        }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { sections ->
                val initialData = mapInitialData(sections)
                (items.adapter as RestaurantCategoryAdapter).loadInitialData(initialData)
            }, onFailure = {
                alertThrowable(it)
            })
        }
    }

    private fun mapInitialData(items: List<ProductSection>): ArrayList<RestaurantCategory> {
        return items.map { item ->
            RestaurantCategory(
                name = item.name!!,
                products = item.products!!.map {
                    RestaurantProduct(
                        id = it.id,
                        imageUrl = it.image ?: "",
                        name = it.name!!,
                        price = formatPrice("€", it.price!!),
                        restricted = it.isRestricted!!,
                    )
                }.toCollection(ArrayList()),
            )
        }.toCollection(ArrayList())
    }

    private fun initAdapter(view: View) {
        items = view.findViewById(R.id.restaurant_items)
        items.layoutManager = LinearLayoutManager(view.context)
        items.adapter = RestaurantCategoryAdapter(navigation)
    }

    @Suppress("unused")
    private fun createMockData(): ArrayList<RestaurantCategory> {
        return arrayListOf(
            RestaurantCategory(
                "Pizza", arrayListOf(
                    RestaurantProduct("", "", "Moda da casa", "€2,99", true),
                    RestaurantProduct("", "", "Presunto e Queijo", "€2,99", false),
                    RestaurantProduct("", "", "Marguerita", "€2,99", false),
                    RestaurantProduct("", "", "Frango com Catupiri", "€2,99", false),
                )
            ), RestaurantCategory(
                "Sandwich", arrayListOf(
                    RestaurantProduct("", "", "Misto Quente", "€2,99", false),
                    RestaurantProduct("", "", "X-Tudo", "€2,99", true),
                    RestaurantProduct("", "", "X-Salada", "€2,99", false),
                    RestaurantProduct("", "", "X-Bacon", "€2,99", false),
                )
            ), RestaurantCategory(
                "Drinks", arrayListOf(
                    RestaurantProduct("", "", "Coca-Cola", "€2,99", false),
                    RestaurantProduct("", "", "Sprite", "€2,99", true),
                    RestaurantProduct("", "", "Cerveja", "€2,99", false),
                )
            )
        )
    }
}

class RestaurantCategoryAdapter(
    private val navigation: NavigationListener
) : RecyclerView.Adapter<RestaurantCategoryAdapter.ViewHolder>() {
    private var data = ArrayList<RestaurantCategory>()

    @SuppressLint("NotifyDataSetChanged")
    fun loadInitialData(newData: ArrayList<RestaurantCategory>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        navigation,
        LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant, parent, false)
    )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    class ViewHolder(
        private val navigation: NavigationListener,
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {
        private val category = itemView.findViewById<TextView>(R.id.restaurant_category_name)
        private val items = itemView.findViewById<RecyclerView>(R.id.restaurant_category_items)
        fun bind(item: RestaurantCategory) {
            category.text = item.name
            initAdapter(item)
        }

        private fun initAdapter(item: RestaurantCategory) {
            val adapter = RestaurantProductAdapter(navigation, item.products)
            items.layoutManager = LinearLayoutManager(itemView.context).apply {
                orientation = RecyclerView.HORIZONTAL
            }
            items.adapter = adapter
        }
    }
}

class RestaurantProductAdapter(
    private val navigation: NavigationListener,
    private val data: ArrayList<RestaurantProduct>,
) : RecyclerView.Adapter<RestaurantProductAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        navigation,
        LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant_product, parent, false)
    )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    class ViewHolder(private val navigation: NavigationListener, itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val container =
            itemView.findViewById<MaterialCardView>(R.id.restaurant_product_container)
        private val product = itemView.findViewById<TextView>(R.id.restaurant_product_name)
        private val price = itemView.findViewById<TextView>(R.id.restaurant_product_price)
        private val image = itemView.findViewById<ImageView>(R.id.restaurant_product_image)
        private val warning = itemView.findViewById<ImageView>(R.id.restaurant_product_warning)

        fun bind(item: RestaurantProduct) {
            product.text = item.name
            price.text = item.price
            if (item.restricted) {
                warning.visibility = ImageView.VISIBLE
                container.strokeColor = ContextCompat.getColor(itemView.context, R.color.red_500)
                container.strokeWidth = dp2px(itemView.context, 2.0f).toInt()
            } else {
                warning.visibility = ImageView.GONE
                container.strokeColor =
                    ContextCompat.getColor(itemView.context, android.R.color.transparent)
                container.strokeWidth = dp2px(itemView.context, 0f).toInt()
            }
            container.setOnClickListener {
                val params = ProductDetailsParams(item.id)
                navigation.navigateTo(ProductDetailsFragment(navigation, params))
            }

            // TODO: Remove development flag
            val development = false
            if (development) {
                Glide.with(itemView) //
                    .load(item.imageUrl) //
                    .apply(RequestOptions.centerInsideTransform()) //
                    .transition(DrawableTransitionOptions.withCrossFade()) //
                    .into(image)
            } else {
                image.setBackgroundResource(R.drawable.detail_product_pizza)
            }
        }
    }
}

fun dp2px(context: Context, dp: Float): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics
    )
}

data class RestaurantProduct(
    val id: String,
    val imageUrl: String,
    val name: String,
    val price: String,
    val restricted: Boolean,
)

data class RestaurantCategory(
    val name: String,
    val products: ArrayList<RestaurantProduct>,
)