package mb.safeEat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

private const val ARG_PARAM1 = "advertisement"

class HomeAdvertisementFragment : Fragment() {
    var item: HomeAdvertisement? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        item = arguments?.getSerializable(ARG_PARAM1) as HomeAdvertisement
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_restaurant_list, container, false)
        if (view != null) onInit(view)
        return view
    }

    private fun onInit(view: View) {
        if (item == null) {
            throw NullPointerException("restaurantList is null.\nUse RestaurantListFragment.newInstance(restaurantList)")
        }
    }

    companion object {
        fun newInstance(restaurantList: HomeAdvertisement) = HomeAdvertisementFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_PARAM1, restaurantList)
            }
        }
    }
}