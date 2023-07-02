package mb.safeEat.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import mb.safeEat.R
import mb.safeEat.services.state.state

class ProfileInitialFragment(private val navigation: NavigationListener) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_profile_initial, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initScreenEvents(view)
    }

    private fun initScreenEvents(view: View) {
        // TODO: Load view items to properties to split this function
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
        addressButton.setOnClickListener { navigation.navigateTo(AddressFragment(navigation)) }
        restrictionsButton.setOnClickListener { navigation.navigateTo(AllergyEditFragment(navigation)) }
        ordersButton.setOnClickListener { navigation.navigateTo(OrdersFragment(navigation)) }
        paymentButton.setOnClickListener { navigation.navigateTo(PaymentOptionFragment(navigation)) }
        // TODO: Hidden this buttons
        settingsButton.setOnClickListener { }
        aboutUsButton.setOnClickListener { }
        exitButton.setOnClickListener { state.logout() }

        state.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                if (user.image != null) {
                    Glide.with(this).load(user.image).apply(RequestOptions.centerCropTransform())
                        .transition(DrawableTransitionOptions.withCrossFade()).into(profileImage)
                }
                profileName.text = user.name
            }
        }
    }
}
