package mb.safeEat.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import mb.safeEat.R
import mb.safeEat.activities.NavigationListener
import mb.safeEat.dialogs.OrderCompletedDialog
import mb.safeEat.extensions.Alertable
import mb.safeEat.functions.formatPrice
import mb.safeEat.functions.initHeader
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api
import mb.safeEat.services.api.dto.OrderDraftDto
import mb.safeEat.services.api.dto.OrderDto

class CartPaymentFragment(private val navigation: NavigationListener) : Fragment(), Alertable {
    private lateinit var binding: CartPaymentBinding
    private var orderDraft: OrderDraftDto? = null
    private var loading = false
    private var payment: mb.safeEat.services.api.models.Payment? = null
    private var delivery: mb.safeEat.services.api.models.Delivery? = null
    private var address: mb.safeEat.services.api.models.Address? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_cart_payment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CartPaymentBinding.fromView(view)
        initHeader(view, navigation, R.string.t_payment)
        initScreenEvents()
        loadInitialData()
    }

    private fun loadInitialData() {
        suspendToLiveData { api.orders.findDraft() }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { orderDraft ->
                this.orderDraft = orderDraft
                if (orderDraft == null) {
                    alertInfo(resources.getString(R.string.t_cart_is_empty))
                } else {
                    payment = orderDraft.payments.firstOrNull { it.isSelected!! }
                    delivery = orderDraft.deliveries.firstOrNull { it.isSelected!! }
                    address = orderDraft.addresses.firstOrNull { it.isSelected!! }

                    var alert = true
                    var invalid = false
                    if (address != null) {
                        binding.address.text = address!!.name
                        binding.addressValue.text = address!!.fullAddress()
                    } else {
                        binding.address.text = resources.getString(R.string.t_no_data)
                        binding.addressValue.text = "-"
                        invalid = true
                    }
                    if (delivery != null) {
                        binding.delivery.text = delivery!!.name
                        binding.deliveryValue.text = delivery!!.formattedInterval()
                        binding.deliveryPrice.text = delivery!!.formattedPrice("€")
                    } else {
                        binding.delivery.text = resources.getString(R.string.t_no_data)
                        binding.deliveryValue.text = "-"
                        invalid = true
                    }
                    if (payment != null) {
                        binding.payment.text = payment!!.name
                    } else {
                        binding.payment.text = resources.getString(R.string.t_no_data)
                        alertInfo("You don't have payment methods")
                        invalid = true
                        alert = false
                    }

                    if (alert && invalid) {
                        alertInfo("You have missing values")
                    }
                    binding.submitButton.isEnabled = !invalid

                    binding.subtotal.text = formatPrice("€", orderDraft.subtotal)
                    binding.total.text =
                        formatPrice("€", orderDraft.subtotal + (delivery?.price ?: 0.0))
                }
            }, onFailure = {
                alertThrowable(it)
            })
        }
    }

    private fun initScreenEvents() {
        binding.addressButton.setOnClickListener {
            navigation.navigateTo(AddressesFragment(navigation, AddressAction.DO_SELECT))
        }
        binding.deliveryOptionButton.setOnClickListener {
            if (orderDraft != null) {
                // TODO: Pass restaurantId to params only
                val params = DeliveryOptionsParams(getDeliveryOptions())
                navigation.navigateTo(DeliveryOptionsFragment(navigation, params))
            }
        }
        binding.paymentKindButton.setOnClickListener {
            navigation.navigateTo(PaymentOptionsFragment(navigation))
        }
        binding.submitButton.setOnClickListener { submit(binding.submitButton) }
    }

    private fun submit(button: Button) {
        if (orderDraft == null) return
        if (loading) return

        val body = OrderDto(
            addressId = address!!.id,
            paymentId = payment!!.id,
            deliveryId = payment!!.id
        )

        loading = true
        button.isEnabled = false
        alertInfo("Loading...")
        suspendToLiveData { api.orders.create(body) }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {
                val dialog = OrderCompletedDialog()
                dialog.setOnDismissListener {
                    loading = false
                    button.isEnabled = true
                    navigation.navigateTo(OrdersFragment(navigation))
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
        return orderDraft!!.deliveries.map {
            DeliveryOption(
                id = it.id,
                name = it.name!!,
                isSelected = it.isSelected!!,
            )
        }.toCollection(ArrayList())
    }

    @Suppress("unused")
    private fun createMockData(): ArrayList<DeliveryOption> {
        return arrayListOf(
            DeliveryOption("t", "Takeout", true),
            DeliveryOption("e", "Economy", false),
            DeliveryOption("x", "Express", false),
        )
    }
}

data class CartPaymentBinding(
    val address: TextView,
    val addressValue: TextView,
    val delivery: TextView,
    val deliveryValue: TextView,
    val deliveryPrice: TextView,
    val payment: TextView,
    val subtotal: TextView,
    val total: TextView,
    val submitButton: Button,
    val addressButton: MaterialCardView,
    val deliveryOptionButton: MaterialCardView,
    val paymentKindButton: MaterialCardView,
) {
    companion object {
        fun fromView(view: View): CartPaymentBinding {
            return CartPaymentBinding(
                address = view.findViewById(R.id.payment_address_name),
                addressValue = view.findViewById(R.id.payment_address_value),
                delivery = view.findViewById(R.id.payment_delivery_option_name),
                deliveryValue = view.findViewById(R.id.payment_delivery_option_value),
                deliveryPrice = view.findViewById(R.id.payment_delivery_option_price),
                payment = view.findViewById(R.id.payment_kind_name),
                subtotal = view.findViewById(R.id.payment_products_subtotal_value),
                total = view.findViewById(R.id.payment_products_price),
                submitButton = view.findViewById(R.id.payment_button_submit),
                addressButton = view.findViewById(R.id.payment_address_button),
                deliveryOptionButton = view.findViewById(R.id.payment_delivery_option_button),
                paymentKindButton = view.findViewById(R.id.payment_kind_button),
            )
        }
    }
}