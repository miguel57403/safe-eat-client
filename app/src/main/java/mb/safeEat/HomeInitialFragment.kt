package mb.safeEat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.Serializable
import java.util.ArrayList

class HomeInitialFragment : Fragment() {
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
        items.adapter = HomeAdapter(createList())
    }

    private fun createList(): ArrayList<HomeItem> {
        return arrayListOf(
            HomeItem.createRestaurantList(
                HomeRestaurantList(
                    "Popular", arrayListOf(
                        Restaurant("Sabor Brasileiro", R.drawable.restaurant, 4.3F),
                        Restaurant("Gelados Maravilhosos", R.drawable.restaurant, 5F),
                        Restaurant("Pingo Doce", R.drawable.restaurant, 4F),
                        Restaurant("Galinha da vizinha", R.drawable.restaurant, 3.9F),
                    )
                )
            ),
            HomeItem.createAdvertisement(HomeAdvertisement("Marmitas caseiras", R.drawable.burger)),
            HomeItem.createRestaurantList(
                HomeRestaurantList(
                    "Best prices", arrayListOf(
                        Restaurant("Galinha da vizinha", R.drawable.restaurant, 3.9F),
                        Restaurant("Mimo's pizza", R.drawable.restaurant, 4.9F),
                        Restaurant("Pingo Doce", R.drawable.restaurant, 4F),
                    )
                )
            ),
            HomeItem.createRestaurantList(
                HomeRestaurantList(
                    "Lunch", arrayListOf(
                        Restaurant("Sabor Brasileiro", R.drawable.restaurant, 4.3F),
                        Restaurant("Gelados Maravilhosos", R.drawable.restaurant, 5F),
                        Restaurant("Galinha da vizinha", R.drawable.restaurant, 3.9F),
                        Restaurant("Mimo's pizza", R.drawable.restaurant, 4.9F),
                    )
                )
            ),
            HomeItem.createRestaurantList(
                HomeRestaurantList(
                    "Breakfast", arrayListOf(
                        Restaurant("Mimo's pizza", R.drawable.restaurant, 4.9F),
                        Restaurant("Pingo Doce", R.drawable.restaurant, 4F),
                    )
                )
            ),
            HomeItem.createAdvertisement(HomeAdvertisement("Padaria Gourmet", R.drawable.burger)),
        )
    }
}

class HomeAdapter(
    private var data: ArrayList<HomeItem>,
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)
    )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    class ViewHolder(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {
        private val container = itemView.findViewById<FrameLayout>(R.id.home_item_container)
        fun bind(item: HomeItem) {
            HomeItemFragment
            when (item.kind) {
                HomeKind.Advertisement -> bindAdvertisement(item.advertisement!!)
                HomeKind.RestaurantList -> bindRestaurantList(item.restaurantList!!)
            }
        }

        private fun bindAdvertisement(item: HomeAdvertisement) {
            val fragmentManager = (itemView.context as FragmentActivity).supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(container.id, HomeAdvertisementFragment.newInstance(item))
                .commit()
        }

        private fun bindRestaurantList(item: HomeRestaurantList) {
            val fragmentManager = (itemView.context as FragmentActivity).supportFragmentManager
            fragmentManager.beginTransaction()
                .replace(container.id, HomeRestaurantListFragment.newInstance(item))
                .commit()
        }
    }
}

data class HomeAdvertisement(
    val restaurant: String,
    val imageId: Int,
) : Serializable

data class Restaurant(
    val name: String,
    val image: Int,
    val score: Float,
) : Serializable

data class HomeRestaurantList(
    val title: String,
    val restaurants: List<Restaurant>,
) : Serializable

enum class HomeKind {
    Advertisement, RestaurantList,
}

data class HomeItem(
    val kind: HomeKind,
    val advertisement: HomeAdvertisement?,
    val restaurantList: HomeRestaurantList?
) : Serializable {
    companion object {
        fun createAdvertisement(advertisement: HomeAdvertisement) =
            HomeItem(HomeKind.Advertisement, advertisement, null)

        fun createRestaurantList(restaurantList: HomeRestaurantList) =
            HomeItem(HomeKind.RestaurantList, null, restaurantList)
    }
}