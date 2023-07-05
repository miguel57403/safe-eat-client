package mb.safeEat.components

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import mb.safeEat.R
import mb.safeEat.extensions.Alertable
import mb.safeEat.extensions.DataStateIndicator
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api

data class SearchRestaurantParams(val categoryId: String)

class SearchRestaurantFragment(
    private val navigation: NavigationListener,
    private val params: SearchRestaurantParams,
) : Fragment(), Alertable {
    private lateinit var items: RecyclerView
    private lateinit var dataStateIndicator: DataStateIndicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_search_restaurant, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataStateIndicator = DataStateIndicator(view)
        initAdapter(view)
        loadInitialData()
        initScreenEvents(view)
    }

    private fun initAdapter(view: View) {
        items = view.findViewById(R.id.search_restaurant_list)
        items.layoutManager = LinearLayoutManager(view.context)
        items.adapter = SearchRestaurantAdapter(navigation)
    }

    private fun initScreenEvents(view: View) {
        val backButton = view.findViewById<MaterialCardView>(R.id.search_restaurant_back_button)
        val searchLayout = view.findViewById<TextInputLayout>(R.id.search_restaurant_search_layout)
        val searchInput = view.findViewById<TextInputEditText>(R.id.search_restaurant_search_input)

        searchLayout.setEndIconOnClickListener { searchAgain(searchInput.text.toString()) }
        searchInput.setOnEditorActionListener { _, actionId, _ ->
            val enterClicked = actionId == EditorInfo.IME_ACTION_DONE
            if (enterClicked) searchAgain(searchInput.text.toString())
            enterClicked
        }
        backButton.setOnClickListener { navigation.onBackPressed() }
    }

    private fun loadInitialData() {
        dataStateIndicator.showLoading()
        suspendToLiveData {
            api.restaurants.findAllByCategory(params.categoryId)
        }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { restaurants ->
                dataStateIndicator.toggle(restaurants.isNotEmpty())
                updateInitialData(restaurants)
            }, onFailure = {
                dataStateIndicator.showError()
                alertThrowable(it)
            })
        }
    }

    private fun getItemList(restaurants: List<mb.safeEat.services.api.models.Restaurant>): ArrayList<SearchRestaurant> {
        return restaurants.map { restaurant ->
            SearchRestaurant(
                id = restaurant.id,
                imageUrl = restaurant.logo ?: "",
                name = restaurant.name!!,
                price = restaurant.formattedDeliveryPrice("€"),
                time = restaurant.formattedDeliveryInterval()
            )
        }.toCollection(ArrayList())
    }

    private fun searchAgain(input: String) {
        dataStateIndicator.showLoading()
        suspendToLiveData { api.restaurants.findAllByName(input) }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { restaurants ->
                dataStateIndicator.toggle(restaurants.isNotEmpty())
                updateInitialData(restaurants)
            }, onFailure = {
                dataStateIndicator.showError()
                alertThrowable(it)
            })
        }
    }

    private fun updateInitialData(restaurants: List<mb.safeEat.services.api.models.Restaurant>) {
        val initialData = getItemList(restaurants)
        (items.adapter as SearchRestaurantAdapter).loadInitialData(initialData)
    }

    @Suppress("unused")
    private fun createMockData(): ArrayList<SearchRestaurant> {
        return arrayListOf(
            SearchRestaurant("", "", "Sabor Brasileiro", "€2,99", "10 - 20 min"),
            SearchRestaurant("", "", "Marmita Caseira", "€2,99", "10 - 20 min"),
            SearchRestaurant("", "", "Galinha da vizinha", "€2,99", "10 - 20 min"),
        )
    }
}

class SearchRestaurantAdapter(
    private val navigation: NavigationListener,
) : RecyclerView.Adapter<SearchRestaurantAdapter.ViewHolder>() {
    private var data = ArrayList<SearchRestaurant>()

    @SuppressLint("NotifyDataSetChanged")
    fun loadInitialData(newData: ArrayList<SearchRestaurant>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        navigation,
        LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant_alone, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount(): Int = data.size

    class ViewHolder(private val navigation: NavigationListener, itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val container =
            itemView.findViewById<MaterialCardView>(R.id.payment_address_container)
        private val image = itemView.findViewById<ImageView>(R.id.search_restaurant_item_image)
        private val name = itemView.findViewById<TextView>(R.id.search_restaurant_item_product)
        private val price = itemView.findViewById<TextView>(R.id.search_restaurant_item_price)
        private val time = itemView.findViewById<TextView>(R.id.search_restaurant_item_time)

        fun bind(item: SearchRestaurant) {
            name.text = item.name
            price.text = item.price
            time.text = item.time
            Glide.with(itemView) //
                .load(item.imageUrl) //
                .apply(RequestOptions().centerCrop()) //
                .transition(DrawableTransitionOptions.withCrossFade()) //
                .into(image)
            container.setOnClickListener {
                val params = RestaurantParams(item.id)
                navigation.navigateTo(RestaurantFragment(navigation, params))
            }
        }
    }
}

data class SearchRestaurant(
    val id: String,
    val imageUrl: String,
    val name: String,
    val price: String,
    val time: String,
)
