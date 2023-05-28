package mb.safeEat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class CartFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        onInit()
        return view
    }

    private fun onInit() {
        navigateTo(CartInitialFragment())
    }

    private fun navigateTo(fragment: Fragment) {
        childFragmentManager
            .beginTransaction()
            .add(R.id.cart_container, fragment)
            .commit()
    }
}