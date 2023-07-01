package mb.safeEat.components

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import mb.safeEat.R
import java.io.Serializable
import java.text.DecimalFormat
import java.util.ArrayList

class HomeInitialFragment(private val navigation: NavigationListener) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_initial, container, false)
        if (view != null) onInit(view)
        return view
    }

    private fun onInit(view: View) {
        initAdapter(view)
    }

    private fun initAdapter(view: View) {
        val items = view.findViewById<RecyclerView>(R.id.home_items)
        items.layoutManager = LinearLayoutManager(view.context)
        items.adapter = HomeAdapter(navigation, createList())
    }

    private fun createList(): ArrayList<HomeItem> {
        val image = R.drawable.restaurant
        return arrayListOf(
            HomeItem.createRestaurantList(
                HomeRestaurantList(
                    "Popular", arrayListOf(
                        Restaurant(null, "Sabor Brasileiro", image, 4.3F, "€2,99", "20 - 30 min"),
                        Restaurant(null, "Gelados Maravilhosos", image, 5F, "€2,99", "20 - 30 min"),
                        Restaurant(null, "Pingo Doce", image, 4F, "€2,99", "20 - 30 min"),
                        Restaurant(null, "Galinha da vizinha", image, 3.9F, "€2,99", "20 - 30 min"),
                    )
                )
            ),
            HomeItem.createAdvertisement(HomeAdvertisement("Marmitas caseiras", R.drawable.burger)),
            HomeItem.createRestaurantList(
                HomeRestaurantList(
                    "Best prices", arrayListOf(
                        Restaurant(null, "Galinha da vizinha", image, 3.9F, "€2,99", "20 - 30 min"),
                        Restaurant(null, "Mimo's pizza", image, 4.9F, "€2,99", "20 - 30 min"),
                        Restaurant(null, "Pingo Doce", image, 4F, "€2,99", "20 - 30 min"),
                    )
                )
            ),
            HomeItem.createRestaurantList(
                HomeRestaurantList(
                    "Lunch", arrayListOf(
                        Restaurant(null, "Sabor Brasileiro", image, 4.3F, "€2,99", "20 - 30 min"),
                        Restaurant(null, "Gelados Maravilhosos", image, 5F, "€2,99", "20 - 30 min"),
                        Restaurant(null, "Galinha da vizinha", image, 3.9F, "€2,99", "20 - 30 min"),
                        Restaurant(null, "Mimo's pizza", image, 4.9F, "€2,99", "20 - 30 min"),
                    )
                )
            ),
            HomeItem.createRestaurantList(
                HomeRestaurantList(
                    "Breakfast", arrayListOf(
                        Restaurant(null, "Mimo's pizza", image, 4.9F, "€2,99", "20 - 30 min"),
                        Restaurant(null, "Pingo Doce", image, 4F, "€2,99", "20 - 30 min"),
                    )
                )
            ),
            HomeItem.createAdvertisement(HomeAdvertisement("Padaria Gourmet", R.drawable.burger)),
        )
    }
}

class HomeAdapter(
    private val navigation: NavigationListener,
    private var data: ArrayList<HomeItem>,
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        navigation, LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)
    )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    class ViewHolder(
        private val navigation: NavigationListener,
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {
        private val advertisement = itemView.findViewById<ConstraintLayout>(R.id.home_item_advertisement)
        private val advertisementTitle = itemView.findViewById<TextView>(R.id.home_item_advertisement_title)
        private val advertisementImage = itemView.findViewById<ImageView>(R.id.home_item_advertisement_image)

        private val restaurantList = itemView.findViewById<LinearLayoutCompat>(R.id.home_item_restaurant_list)
        private val restaurantListTitle = itemView.findViewById<TextView>(R.id.home_item_restaurant_list_title)
        private val restaurantListItems = itemView.findViewById<RecyclerView>(R.id.home_item_restaurant_list_items)

        fun bind(item: HomeItem) = when (item.kind) {
            HomeKind.Advertisement -> bindAdvertisement(item.advertisement!!)
            HomeKind.RestaurantList -> bindRestaurantList(item.restaurantList!!)
        }

        private fun bindAdvertisement(item: HomeAdvertisement) {
            advertisement.visibility = LinearLayoutCompat.VISIBLE
            advertisementTitle.text = item.title
            advertisementImage.setImageResource(item.imageId)
        }

        private fun bindRestaurantList(item: HomeRestaurantList) {
            restaurantList.visibility = LinearLayoutCompat.VISIBLE
            restaurantListTitle.text = item.title
            initAdapter(item)
        }

        private fun initAdapter(item: HomeRestaurantList) {
            val items = restaurantListItems
            items.layoutManager = LinearLayoutManager(itemView.context).apply {
                orientation = LinearLayoutManager.HORIZONTAL
            }
            items.adapter = HomeRestaurantAdapter(navigation, item.restaurants)
        }
    }
}

class HomeRestaurantAdapter(
    private val navigation: NavigationListener, private val data: List<Restaurant>
) : RecyclerView.Adapter<HomeRestaurantAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        navigation, LayoutInflater.from(parent.context).inflate(R.layout.item_home_restaurant_list, parent, false)
    )

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    class ViewHolder(private val navigation: NavigationListener, itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val container = itemView.findViewById<MaterialCardView>(R.id.restaurant_list_item_container)
        private val image = itemView.findViewById<ImageView>(R.id.restaurant_list_item_image)
        private val restaurant = itemView.findViewById<TextView>(R.id.restaurant_list_item_restaurant)
        private val score = itemView.findViewById<TextView>(R.id.restaurant_list_item_score)

        fun bind(item: Restaurant) {
            image.setImageResource(item.image)
            restaurant.text = item.name
            score.text = DecimalFormat("0.0").format(item.score)
            container.setOnClickListener {
                navigation.navigateTo(
                    RestaurantFragment(
                        navigation, RestaurantParams("649f3335b743876fd72143b1")
                    )
                )
            }
        }
    }
}

data class HomeAdvertisement(
    val title: String,
    val imageId: Int,
) : Serializable

data class Restaurant(
    val id: String?,
    val name: String,
    val image: Int,
    val score: Float,
    val deliveryPrice: String,
    val deliveryTime: String,
) : Serializable

data class HomeRestaurantList(
    val title: String,
    val restaurants: List<Restaurant>,
) : Serializable

enum class HomeKind {
    Advertisement, RestaurantList,
}

data class HomeItem(
    val kind: HomeKind, val advertisement: HomeAdvertisement?, val restaurantList: HomeRestaurantList?
) : Serializable {
    companion object {
        fun createAdvertisement(advertisement: HomeAdvertisement) =
            HomeItem(HomeKind.Advertisement, advertisement, null)

        fun createRestaurantList(restaurantList: HomeRestaurantList) =
            HomeItem(HomeKind.RestaurantList, null, restaurantList)
    }
}