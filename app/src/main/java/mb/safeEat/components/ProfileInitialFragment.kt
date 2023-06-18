package mb.safeEat.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import mb.safeEat.R
import android.widget.TextView
import mb.safeEat.functions.base64ToBitmap
import mb.safeEat.services.state.state

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
        val profileImage = view.findViewById<ImageView>(R.id.profile_image)
        val profileName = view.findViewById<TextView>(R.id.profile_name)
        val addressButton = view.findViewById<Button>(R.id.profile_button_address)
        val restrictionsButton = view.findViewById<Button>(R.id.profile_button_restrictions)
        val ordersButton = view.findViewById<Button>(R.id.profile_button_orders)
        val paymentButton = view.findViewById<Button>(R.id.profile_button_payment)
        val settingsButton = view.findViewById<Button>(R.id.profile_button_settings)
        val aboutUsButton = view.findViewById<Button>(R.id.profile_button_about_us)
        val exitButton = view.findViewById<Button>(R.id.profile_button_exit)

        profileImage.setOnClickListener { navigation.navigateTo(ProfileEditFragment(navigation)) }
        addressButton.setOnClickListener { navigation.navigateTo(AddressFragment(navigation))  }
        restrictionsButton.setOnClickListener { navigation.navigateTo(AllergyEditFragment(navigation))  }
        ordersButton.setOnClickListener { navigation.navigateTo(OrdersFragment(navigation)) }
        paymentButton.setOnClickListener { navigation.navigateTo(PaymentOptionFragment(navigation))  }
        settingsButton.setOnClickListener {  }
        aboutUsButton.setOnClickListener {  }
        exitButton.setOnClickListener { state.logout() }

        state.user.observe(viewLifecycleOwner) {
            if (it != null) {
                profileImage.setImageBitmap(base64ToBitmap(it.profileImage))
                profileName.text = it.name
            }
        }
    }
}
