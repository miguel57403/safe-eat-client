package mb.safeEat.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import mb.safeEat.R
import mb.safeEat.extensions.Alertable

class PaymentFragment(private val navigation: NavigationListener) : Fragment(), Alertable {
//    private var orderDraft: OrderDraft? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_payment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader(view)
        initScreenEvents(view)
        loadInitialData()
    }

    private fun initHeader(view: View) {
        val title = view.findViewById<TextView>(R.id.header_title)
        val backButton = view.findViewById<MaterialCardView>(R.id.header_back_button)
        title.text = resources.getString(R.string.t_payment)
        backButton.setOnClickListener { navigation.onBackPressed() }
    }

    private fun loadInitialData() {
        // TODO: load data from API
    }

    private fun initScreenEvents(view: View) {
        val addressButton = view.findViewById<MaterialCardView>(R.id.payment_address_button)
        val deliveryOptionButton =
            view.findViewById<MaterialCardView>(R.id.payment_delivery_option_button)
        val paymentKindButton = view.findViewById<MaterialCardView>(R.id.payment_kind_button)
        val submitButton = view.findViewById<Button>(R.id.payment_button_submit)

        addressButton.setOnClickListener {
            navigation.navigateTo(AddressFragment(navigation))
        }
        deliveryOptionButton.setOnClickListener {
            // TODO: Feed params
            val params = DeliveryOptionsParams(arrayListOf())
            navigation.navigateTo(DeliveryOptionsFragment(navigation, params))
        }
        paymentKindButton.setOnClickListener {
            navigation.navigateTo(PaymentOptionFragment(navigation))
        }
        submitButton.setOnClickListener {
            val dialog = OrderCompletedDialog()
            dialog.setOnDismissListener {
                navigation.navigateTo(
                    NotificationInitialFragment(
                        navigation
                    )
                )
            }
            dialog.show(navigation.getSupportFragmentManager(), dialog.tag)
        }
    }
}