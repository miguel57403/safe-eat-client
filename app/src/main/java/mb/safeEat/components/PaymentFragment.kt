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
import mb.safeEat.functions.initHeader
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api
import mb.safeEat.services.api.dto.OrderDraftDto

// TODO: Rename to CartPaymentFragment
class PaymentFragment(private val navigation: NavigationListener) : Fragment(), Alertable {
    private var orderDraft: OrderDraftDto? = null
    private var loading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_payment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader(view, navigation, R.string.t_payment)
        initScreenEvents(view)
        loadInitialData(view)
    }

    private fun loadInitialData(view: View) {
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
                if (orderDraft == null) {
                    alertInfo(resources.getString(R.string.t_cart_is_empty))
                } else {
                    val address = view.findViewById<TextView>(R.id.payment_address_name)
                    val addressValue = view.findViewById<TextView>(R.id.payment_address_value)
                    val delivery = view.findViewById<TextView>(R.id.payment_delivery_option_name)
                    val deliveryValue = view.findViewById<TextView>(R.id.payment_delivery_option_value)
                    val payment = view.findViewById<TextView>(R.id.payment_kind_name)

                    val selectedPayment = orderDraft.payments?.first { it.isSelected!! }!!
                    val selectedDelivery = orderDraft.deliveries?.first { it.isSelected!! }!!
                    val selectedAddress = orderDraft.addresses?.first { it.isSelected!! }!!

                    address.text = selectedAddress.name
                    addressValue.text = selectedAddress.fullAddress()
                    delivery.text = selectedDelivery.name
                    deliveryValue.text = selectedDelivery.formattedDeliveryInterval()
                    payment.text = selectedPayment.name
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
            navigation.navigateTo(AddressesFragment(navigation, AddressAction.DO_SELECT))
        }
        deliveryOptionButton.setOnClickListener {
            if (orderDraft != null) {
                val params = DeliveryOptionsParams(getDeliveryOptions())
                navigation.navigateTo(DeliveryOptionsFragment(navigation, params))
            }
        }
        paymentKindButton.setOnClickListener {
            navigation.navigateTo(PaymentOptionsFragment(navigation))
        }
        submitButton.setOnClickListener { submit(submitButton) }
    }

    private fun submit(button: Button) {
        if (orderDraft == null) return
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
                isSelected = it.isSelected!!,
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
