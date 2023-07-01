package mb.safeEat.components

import android.os.Bundle
import android.util.Log
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
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api

class SearchCategoryInitialFragment(private val navigation: NavigationListener) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_category_initial, container, false)
        if (view != null) onInit(view)
        return view
    }

    private fun onInit(view: View) {
        onGetAllCategory(view)
    }

    private fun onGetAllCategory(view: View) {
        suspendToLiveData { api.categories.findAll() }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { categories ->
                initAdapter(view, getItemList(categories))
            }, onFailure = { failure ->
                Log.d("Debug", "Failure $failure")
            })
        }
    }

    private fun initAdapter(view: View, categories: List<Category>) {
        val items = view.findViewById<RecyclerView>(R.id.search_categories_items)
        items.layoutManager = GridLayoutManager(view.context, 2)
        items.adapter = SearchCategoryAdapter(navigation, categories)
    }

    // TODO: Change the mapper name to a standard name
    private fun getItemList(categories: List<mb.safeEat.services.api.models.Category>): List<Category> {
        return categories.map { category ->
            Category(
                category.name!!,
                category.image ?: "https://placehold.co/600x400?text=${category.name}",
            )
        }
    }

    private fun createList(): List<Category> {
        // TODO: Add this to the database
        return listOf(
            Category("Sandwich", ""), // R.drawable.sandwich),
            Category("Pizza", ""), // R.drawable.pizza),
            Category("Burger", ""), // R.drawable.burger),
            Category("Portions", ""), // R.drawable.portions),
            Category("Meals", ""), // R.drawable.meals),
            Category("Japanese", ""), // R.drawable.japanese),
            Category("Drinks", ""), // R.drawable.drinks),
            Category("Ice Cream", ""), // R.drawable.ice_cream),
            Category("Donner", ""), // R.drawable.donner),
            Category("Desserts", ""), // R.drawable.desserts),
            Category("Vegan", ""), // R.drawable.vegan),
            Category("Thai Foods", ""), // R.drawable.thai_foods)
        )
    }
}

class SearchCategoryAdapter(
    private val navigation: NavigationListener,
    private val data: List<Category>,
) : RecyclerView.Adapter<SearchCategoryAdapter.ViewHolder>() {
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
            container.setOnClickListener { navigation.navigateTo(SearchRestaurantFragment(navigation)) }
        }
    }
}

data class Category(
    val name: String,
    val imageUrl: String,
)
