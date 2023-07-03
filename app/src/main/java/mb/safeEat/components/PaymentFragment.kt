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
import mb.safeEat.dialogs.OrderCompletedDialog
import mb.safeEat.dialogs.RestrictionAlertDialog
import mb.safeEat.extensions.Alertable
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api
import mb.safeEat.services.api.dto.OrderDraftDto

class PaymentFragment(private val navigation: NavigationListener) : Fragment(), Alertable {
    private var orderDraft: OrderDraftDto? = null
    private var loading = false

    private fun isNotEmpty() = orderDraft != null
    private fun isEmpty() = orderDraft == null

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
        suspendToLiveData {
            val isEmpty = api.carts.isEmpty()
            if (isEmpty) {
                null
            } else {
                api.orders.findDraft()
            }
        }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { orderDraft ->
                this.orderDraft = orderDraft
                if (isEmpty()) {
                    alertInfo(resources.getString(R.string.t_cart_is_empty))
                } else {
                    // TODO: Feed selected data to screen
                }
            }, onFailure = {
                alertThrowable(it)
            })
        }
    }

    private fun initScreenEvents(view: View) {
        val addressButton = view.findViewById<MaterialCardView>(R.id.payment_address_button)
        val deliveryOptionButton =
            view.findViewById<MaterialCardView>(R.id.payment_delivery_option_button)
        val paymentKindButton = view.findViewById<MaterialCardView>(R.id.payment_kind_button)
        val submitButton = view.findViewById<Button>(R.id.payment_button_submit)

        addressButton.setOnClickListener {
            // TODO: Create params
            navigation.navigateTo(AddressesFragment(navigation))
        }
        deliveryOptionButton.setOnClickListener {
            if (isNotEmpty()) {
                val params = DeliveryOptionsParams(getDeliveryOptions())
                navigation.navigateTo(DeliveryOptionsFragment(navigation, params))
            }
        }
        paymentKindButton.setOnClickListener {
            // TODO: Create params
            navigation.navigateTo(PaymentOptionsFragment(navigation))
        }
        submitButton.setOnClickListener { submit(submitButton) }
    }

    private fun submit(button: Button) {
        if (isEmpty()) return
        if (loading) return

        val hasWarnings = true // TODO: Check if there are warnings
        if (hasWarnings) {
            val dialog = RestrictionAlertDialog()
            dialog.show(navigation.getSupportFragmentManager(), dialog.tag)
            dialog.setOnConfirmListener { confirm ->
                if (confirm) confirmCart(button)
            }
        } else {
            confirmCart(button)
        }
    }

    private fun confirmCart(button: Button) {
        loading = true
        button.isEnabled = false
        alertInfo("Loading...")
        suspendToLiveData { api.orders.create(null!!) }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {
                val dialog = OrderCompletedDialog()
                dialog.setOnDismissListener {
                    loading = false
                    button.isEnabled = true
                    navigation.navigateTo(NotificationsFragment(navigation))
                }
                dialog.show(navigation.getSupportFragmentManager(), dialog.tag)
            }, onFailure = {
                alertThrowable(it)
                loading = false
                button.isEnabled = true
            })
        }
    }

    private fun getDeliveryOptions(): ArrayList<DeliveryOption> {
        return orderDraft!!.deliveries!!.map {
            DeliveryOption(
                id = it.id!!,
                name = it.name!!,
                isSelected = it.id == orderDraft!!.deliveryId
            )
        }.toCollection(ArrayList())
    }

    private fun createDeliveryOptions(): ArrayList<DeliveryOption> {
        return arrayListOf(
            DeliveryOption("t", "Takeout", true),
            DeliveryOption("e", "Economy", false),
            DeliveryOption("x", "Express", false),
        )
    }
}
