package mb.safeEat.components

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api
import mb.safeEat.services.api.models.Product

class SearchProductFragment(private val navigation: NavigationListener) : Fragment(), Alertable {
    private lateinit var items: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_search_product, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        val backButton = view.findViewById<ImageView>(R.id.search_product_back_button)
        val searchLayout = view.findViewById<TextInputLayout>(R.id.search_product_search_layout)
        val searchInput = view.findViewById<TextInputEditText>(R.id.search_product_search_input)

        searchLayout.setEndIconOnClickListener { searchAgain(searchInput.text.toString()) }
        searchInput.setOnEditorActionListener { _, actionId, _ ->
            val enterClicked = actionId == EditorInfo.IME_ACTION_DONE
            if (enterClicked) searchAgain(searchInput.text.toString())
            enterClicked
        }
        backButton.setOnClickListener { navigation.onBackPressed() }
    }

    private fun loadInitialData() {
        // TODO: Replace with real restaurant id
        val restaurantId = "649f3335b743876fd72143b1"

        suspendToLiveData {
            api.products.findAllByRestaurant(restaurantId)
        }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { searchProduct ->
                val initialData = mapInitialData(searchProduct)
                (items.adapter as SearchProductAdapter).loadInitialData(initialData)
            }, onFailure = {
                alertThrowable(it)
            })
        }
    }

    private fun mapInitialData(searchProduct: List<Product>): ArrayList<SearchProduct> {
        return searchProduct.map {
            SearchProduct(
                it.image!!,
                it.name!!,
                mb.safeEat.functions.formatPrice("€", it.price), // TODO: Rename to formatPrice
            )
        }.toCollection(ArrayList())
    }

    private fun createList(): ArrayList<SearchProduct> {
        return arrayListOf(
            SearchProduct("", "Product Name 1", "€2,99"),
            SearchProduct("", "Product Name 2", "€2,99"),
            SearchProduct("", "Product Name 3", "€2,99")
        )
    }

    private fun searchAgain(input: String) {
        // TODO: search with input
        Log.d("Search", input)
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

        fun bind(product: SearchProduct) {
            name.text = product.name
            price.text = product.price
            Glide.with(itemView) //
                .load(product.imageUrl) //
                .apply(RequestOptions().centerCrop()) //
                .transition(DrawableTransitionOptions.withCrossFade()) //
                .into(image)
            container.setOnClickListener { navigation.navigateTo(ProductDetailsFragment(navigation)) }
        }
    }
}

data class SearchProduct(
    val imageUrl: String,
    val name: String,
    val price: String,
)
