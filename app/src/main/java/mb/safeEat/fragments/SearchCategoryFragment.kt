package mb.safeEat.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import mb.safeEat.R
import mb.safeEat.activities.NavigationListener
import mb.safeEat.extensions.Alertable
import mb.safeEat.extensions.DataStateIndicator
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api

class SearchCategoryFragment(private val navigation: NavigationListener) : Fragment(), Alertable {
    private lateinit var items: RecyclerView
    private lateinit var dataStateIndicator: DataStateIndicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_search_category, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataStateIndicator = DataStateIndicator(view)
        initAdapter(view)
        initScreenEvents(view)
        loadInitialData()
        validateCart()
    }

    private fun initAdapter(view: View) {
        items = view.findViewById(R.id.search_categories_items)
        items.layoutManager = GridLayoutManager(view.context, 2)
        items.adapter = SearchCategoryAdapter(navigation)
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
        backButton.visibility = View.GONE
    }

    private fun doSearch(input: String) {
        if (input.isBlank()) return
        val params = SearchRestaurantParams(categoryId = null, search = input)
        navigation.navigateTo(SearchRestaurantFragment(navigation, params))
    }

    private fun loadInitialData() {
        dataStateIndicator.showLoading()
        suspendToLiveData { api.categories.findAll() }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { categories ->
                dataStateIndicator.toggle(categories.isNotEmpty())
                val initialData = mapInitialData(categories)
                (items.adapter as SearchCategoryAdapter).loadInitialData(initialData)
            }, onFailure = {
                dataStateIndicator.showError()
                alertThrowable(it)
            })
        }
    }

    private fun mapInitialData(categories: List<mb.safeEat.services.api.models.Category>): ArrayList<Category> {
        return categories.map { category ->
            Category(
                id = category.id,
                name = category.name!!,
                imageUrl = category.image ?: "",
            )
        }.toCollection(ArrayList())
    }

    private fun validateCart() {
        suspendToLiveData { api.carts.isEmpty() }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { data ->
                if (!data.isEmpty) {
                    val params = RestaurantParams(data.restaurantId!!)
                    navigation.navigateTo(RestaurantFragment(navigation, params))
                }
            }, onFailure = {
                alertThrowable(it)
            })
        }
    }
}

class SearchCategoryAdapter(
    private val navigation: NavigationListener,
) : RecyclerView.Adapter<SearchCategoryAdapter.ViewHolder>() {
    private var data = ArrayList<Category>()

    @SuppressLint("NotifyDataSetChanged")
    fun loadInitialData(newData: ArrayList<Category>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        navigation,
        LayoutInflater.from(parent.context).inflate(R.layout.item_search_category, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    override fun getItemCount(): Int = data.size

    class ViewHolder(private val navigation: NavigationListener, itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val container =
            itemView.findViewById<MaterialCardView>(R.id.item_search_category_container)
        private val category: TextView = itemView.findViewById(R.id.search_category_name)
        private val image: ImageView = itemView.findViewById(R.id.search_category_image)

        fun bind(item: Category) {
            this.category.text = item.name
            Glide.with(itemView) //
                .load(item.imageUrl) //
                .apply(RequestOptions.centerCropTransform()) //
                .transition(DrawableTransitionOptions.withCrossFade()) //
                .into(image)

            container.setOnClickListener {
                val params = SearchRestaurantParams(categoryId = item.id, search = null)
                navigation.navigateTo(SearchRestaurantFragment(navigation, params))
            }
        }
    }
}

data class Category(
    val id: String,
    val name: String,
    val imageUrl: String,
)
