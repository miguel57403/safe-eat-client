package mb.safeEat

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchCategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_category)

        val recycleView = findViewById<RecyclerView>(R.id.search_categories_items)
        val adapter = SearchCategoryAdapter(this, getItemList())

        recycleView.layoutManager = GridLayoutManager(this, 2)
        recycleView.adapter = adapter
    }

    private fun getItemList(): List<Category> {
        return listOf(
            Category("Sandwich", R.drawable.sandwich),
            Category("Pizza", R.drawable.pizza),
            Category("Burger", R.drawable.burger),
            Category("Portions", R.drawable.portions),
            Category("Meals", R.drawable.meals),
            Category("Japanese", R.drawable.japanese),
            Category("Drinks", R.drawable.drinks),
            Category("Ice Cream", R.drawable.ice_cream),
            Category("Donner", R.drawable.donner),
            Category("Desserts", R.drawable.desserts),
            Category("Vegan", R.drawable.vegan),
            Category("Thai Foods", R.drawable.thai_foods)
        )
    }
}

class SearchCategoryAdapter(private val context: Context, private val itemList: List<Category>) :
    RecyclerView.Adapter<SearchCategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_search_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(context, itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemName: TextView = itemView.findViewById(R.id.search_category_name)
        private val itemImage: ImageView = itemView.findViewById(R.id.search_category_image)

        fun bind(context: Context, category: Category) {
            val imageDrawable = ContextCompat.getDrawable(context, category.imageId)
            itemName.text = category.name
            itemImage.background = imageDrawable
        }
    }
}

data class Category(
    val name: String,
    val imageId: Int
)
