package mb.safeEat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

private const val ARG_PARAM1 = "restaurantList"

class HomeItemFragment  : Fragment()  {
    var item: HomeItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        item = arguments?.getSerializable(ARG_PARAM1) as HomeItem
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_restaurant_list, container, false)
        if (view != null) onInit(view)
        return view
    }

    private fun onInit(view: View) {
        childFragmentManager
//        if (item == null) {
//            throw NullPointerException("restaurantList is null.\nUse RestaurantListFragment.newInstance(restaurantList)")
//        }
    }


    private fun bindAdvertisement(item: HomeAdvertisement) {
//        val fragmentManager = (itemView.context as FragmentActivity).supportFragmentManager
//        fragmentManager.beginTransaction()
//            .replace(container.id, HomeAdvertisementFragment.newInstance(item))
//            .commit()
    }

    private fun bindRestaurantList(item: HomeRestaurantList) {
//        val fragmentManager = (itemView.context as FragmentActivity).supportFragmentManager
//        fragmentManager.beginTransaction()
//            .replace(container.id, HomeRestaurantListFragment.newInstance(item))
//            .commit()
    }

    companion object {
        fun newInstance(homeItem: HomeItem) = HomeItemFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_PARAM1, homeItem)
            }
        }
    }
}