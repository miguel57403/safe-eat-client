package mb.safeEat.components

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.textfield.TextInputEditText
import mb.safeEat.R
import mb.safeEat.functions.cleanIntentStack
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api
import mb.safeEat.services.api.authorization
import mb.safeEat.services.state.state

class SearchCategoryFragment(private val navigation: NavigationListener) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search_category, container, false)
        onInit()
        return view
    }

    private fun onInit() {
        navigation.navigateTo(SearchCategoryInitialFragment(navigation))
    }
}
