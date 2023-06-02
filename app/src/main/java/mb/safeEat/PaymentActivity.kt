package mb.safeEat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView

class PaymentActivity(private val navigation: NavigationListener) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_payment, container, false)
        if (view != null) onInit(view)
        return view
    }

    private fun onInit(view: View) {
        initHeader(view)
        initScreenEvents(view)
    }

    private fun initHeader(view: View) {
        val title = view.findViewById<TextView>(R.id.header_title)
        val backButton = view.findViewById<MaterialCardView>(R.id.header_back_button)
        title.text = resources.getString(R.string.t_payment)
        backButton.setOnClickListener { navigation.onBackPressed() }
    }

    private fun initScreenEvents(view: View) {
        val addressButton = view.findViewById<MaterialCardView>(R.id.payment_address_button)
        val deliveryOptionButton =
            view.findViewById<MaterialCardView>(R.id.payment_delivery_option_button)
        val paymentKindButton = view.findViewById<MaterialCardView>(R.id.payment_kind_button)
        val submitButton = view.findViewById<Button>(R.id.payment_button_submit)

        addressButton.setOnClickListener {
            navigation.navigateTo(AddressActivity(navigation))
        }
        deliveryOptionButton.setOnClickListener {
            Log.d("Click", "Delivery Option Button Clicked")
            // navigation.navigateTo(DeliveryOptionActivity(navigation))
        }
        paymentKindButton.setOnClickListener {
            navigation.navigateTo(PaymentOptionActivity(navigation))
        }
        submitButton.setOnClickListener {
            val dialog = OrderCompletedDialogFragment()
            dialog.setOnDismissListener { navigation.navigateTo(NotificationFragment(navigation)) }
            dialog.show(navigation.getSupportFragmentManager(), dialog.tag)
        }
    }
}