package mb.safeEat.components

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.card.MaterialCardView
import mb.safeEat.R
import mb.safeEat.extensions.Alertable
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api

class SearchCategoryFragment(private val navigation: NavigationListener) : Fragment(),
    Alertable {
    private lateinit var items: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_search_category, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter(view)
        loadInitialData()
    }

    private fun initAdapter(view: View) {
        items = view.findViewById(R.id.search_categories_items)
        items.layoutManager = GridLayoutManager(view.context, 2)
        items.adapter = SearchCategoryAdapter(navigation)
    }

    private fun loadInitialData() {
        suspendToLiveData { api.categories.findAll() }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { categories ->
                val initialData = mapInitialData(categories)
                (items.adapter as SearchCategoryAdapter).loadInitialData(initialData)
            }, onFailure = {
                alertThrowable(it)
            })
        }
    }

    private fun mapInitialData(categories: List<mb.safeEat.services.api.models.Category>): ArrayList<Category> {
        return categories.map { category ->
            Category(
                id = category.id!!,
                name = category.name!!,
                imageUrl = category.image ?: "",
            )
        }.toCollection(ArrayList())
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

        fun bind(category: Category) {
            this.category.text = category.name
            Glide.with(itemView) //
                .load(category.imageUrl) //
                .apply(RequestOptions().centerCrop()) //
                .transition(DrawableTransitionOptions.withCrossFade()) //
                .into(image)

            container.setOnClickListener {
                val params = RestaurantParams(category.id)
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
