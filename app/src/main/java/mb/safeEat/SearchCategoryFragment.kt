package mb.safeEat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class SearchCategoryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_category, container, false)
        onInit()
        return view
    }

    private fun onInit() {
        navigateTo(SearchCategoryActivity())
    }

    private fun navigateTo(fragment: Fragment) {
        childFragmentManager
            .beginTransaction()
            .replace(R.id.search_container, fragment)
            .commit()
    }
}
