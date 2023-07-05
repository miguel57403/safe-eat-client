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
import mb.safeEat.functions.formatPrice
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api
import mb.safeEat.services.api.models.Product

data class SearchProductParams(val restaurantId: String)

class SearchProductFragment(
    private val navigation: NavigationListener,
    private val params: SearchProductParams,
) : Fragment(), Alertable {
    private lateinit var items: RecyclerView
    private lateinit var dataStateIndicator: DataStateIndicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_search_product, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataStateIndicator = DataStateIndicator(view)
        initAdapter(view)
        initScreenEvents(view)
        loadInitialData()
    }

    private fun initAdapter(view: View) {
        items = view.findViewById(R.id.search_product_list)
        items.layoutManager = LinearLayoutManager(view.context)
        items.adapter = SearchProductAdapter(navigation)
    }

    private fun initScreenEvents(view: View) {
        val backButton = view.findViewById<MaterialCardView>(R.id.header_search_back_button)
        val searchLayout = view.findViewById<TextInputLayout>(R.id.header_search_search_layout)
        val searchInput = view.findViewById<TextInputEditText>(R.id.header_search_search_input)

        searchLayout.setEndIconOnClickListener { doSearch(searchInput.text.toString()) }
        searchInput.setOnEditorActionListener { _, actionId, _ ->
            val enterClicked = actionId == EditorInfo.IME_ACTION_DONE
            if (enterClicked) doSearch(searchInput.text.toString())
            enterClicked
        }
        backButton.setOnClickListener { navigation.onBackPressed() }
    }

    private fun loadInitialData() {
        doSearch("")
    }

    private fun doSearch(input: String) {
        dataStateIndicator.showLoading()
        suspendToLiveData {
            if (input.isEmpty()) api.products.findAllByRestaurant(params.restaurantId)
            else api.products.findAllByRestaurantAndName(params.restaurantId, input)
        }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { products ->
                dataStateIndicator.toggle(products.isNotEmpty())
                val initialData = mapInitialData(products)
                (items.adapter as SearchProductAdapter).loadInitialData(initialData)
            }, onFailure = {
                dataStateIndicator.showError()
                alertThrowable(it)
            })
        }
    }

    private fun mapInitialData(products: List<Product>): ArrayList<SearchProduct> {
        return products.map {
            SearchProduct(
                id = it.id,
                imageUrl = it.image ?: "",
                name = it.name!!,
                price = formatPrice("€", it.price),
            )
        }.toCollection(ArrayList())
    }

    @Suppress("unused")
    private fun createMockData(): ArrayList<SearchProduct> {
        return arrayListOf(
            SearchProduct("", "", "Product Name 2", "€2,99"),
            SearchProduct("", "", "Product Name 1", "€2,99"),
            SearchProduct("", "", "Product Name 3", "€2,99")
        )
    }
}

class SearchProductAdapter(private val navigation: NavigationListener) :
    RecyclerView.Adapter<SearchProductAdapter.ViewHolder>() {
    private var data = ArrayList<SearchProduct>()

    @SuppressLint("NotifyDataSetChanged")
    fun loadInitialData(newData: ArrayList<SearchProduct>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        navigation,
        LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount(): Int = data.size

    class ViewHolder(private val navigation: NavigationListener, itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val container = itemView.findViewById<MaterialCardView>(R.id.item_product_container)
        private val image = itemView.findViewById<ImageView>(R.id.search_product_item_image)
        private val name = itemView.findViewById<TextView>(R.id.search_product_item_product)
        private val price = itemView.findViewById<TextView>(R.id.search_product_item_price)

        fun bind(item: SearchProduct) {
            name.text = item.name
            price.text = item.price
            Glide.with(itemView) //
                .load(item.imageUrl) //
                .apply(RequestOptions().centerCrop()) //
                .transition(DrawableTransitionOptions.withCrossFade()) //
                .into(image)
            container.setOnClickListener {
                val params = ProductDetailsParams(item.id)
                navigation.navigateTo(ProductDetailsFragment(navigation, params))
            }
        }
    }
}

data class SearchProduct(
    val id: String,
    val imageUrl: String,
    val name: String,
    val price: String,
)
