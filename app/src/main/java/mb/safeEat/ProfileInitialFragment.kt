package mb.safeEat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class ProfileInitialFragment(private val navigation: NavigationListener) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_initial, container, false)
        if (view != null) onInit(view)
        return view
    }

    private fun onInit(view: View) {
        val addressButton = view.findViewById<Button>(R.id.profile_button_address)
        val restrictionsButton = view.findViewById<Button>(R.id.profile_button_restrictions)
        val ordersButton = view.findViewById<Button>(R.id.profile_button_orders)
        val paymentButton = view.findViewById<Button>(R.id.profile_button_payment)
        val settingsButton = view.findViewById<Button>(R.id.profile_button_settings)
        val aboutUsButton = view.findViewById<Button>(R.id.profile_button_about_us)

        addressButton.setOnClickListener { navigation.navigateTo(AddressActivity(navigation))  }
        restrictionsButton.setOnClickListener { navigation.navigateTo(AllergyEditActivity(navigation))  }
        ordersButton.setOnClickListener { navigation.navigateTo(OrdersActivity(navigation)) }
        paymentButton.setOnClickListener { navigation.navigateTo(PaymentOptionActivity(navigation))  }
        settingsButton.setOnClickListener {  }
        aboutUsButton.setOnClickListener {  }
    }
}
