package mb.safeEat.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mb.safeEat.R
import mb.safeEat.activities.NavigationListener
import mb.safeEat.extensions.Alertable
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api
import mb.safeEat.services.api.dto.PaymentDto

class PaymentRegisterFragment(private val navigation: NavigationListener) : Fragment(), Alertable {
    private lateinit var binding: PaymentRegisterBinding
    private var loading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_payment_register, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = PaymentRegisterBinding.fromView(view)
        initHeader(view)
        initScreenEvents()
    }

    private fun initHeader(view: View) {
        val title = view.findViewById<TextView>(R.id.header_title)
        val backButton = view.findViewById<MaterialCardView>(R.id.header_back_button)

        title.text = resources.getString(R.string.t_new_payment)
        backButton.setOnClickListener { navigation.onBackPressed() }
    }

    private fun initScreenEvents() {
        binding.submitButton.setOnClickListener { doSubmit() }
    }

    private fun doSubmit() {
        if (loading) return

        val body = PaymentDto(
            type = binding.method.selectedItem?.toString(),
            name = binding.name.text?.toString(),
            number = binding.cardNumber.text?.toString()?.toInt(),
            expirationDate = binding.expirationDate.text?.toString(),
            cvv = binding.cvv.text?.toString()?.toInt(),
        )
        if (!validateBody(body)) return

        loading = true
        binding.submitButton.isEnabled = false
        alertInfo("Loading...")
        suspendToLiveData { api.payments.create(body) }.observe(viewLifecycleOwner) {result->
            result.fold(onSuccess = {
                alertSuccess("Payment Method created!")
                lifecycleScope.launch {
                    delay(1500)
                    binding.submitButton.isEnabled = true
                    loading = false
                    navigation.onBackPressed()
                }
            }, onFailure = {
                alertThrowable(it)
                binding.submitButton.isEnabled = true
                loading = false
            })
        }
    }

    private fun validateBody(body: PaymentDto): Boolean {
        if (body.type.isNullOrEmpty()) {
            alertError("Payment type is required")
            return false
        }
        if (body.name.isNullOrEmpty()) {
            alertError("Payment name is required")
            return false
        }
        if (body.number == null) {
            alertError("Payment number is required")
            return false
        }
        if (body.expirationDate.isNullOrEmpty()) {
            alertError("Payment expiration date is required")
            return false
        }
        if (body.cvv == null) {
            alertError("Payment cvv is required")
            return false
        }
        return true
    }
}

data class PaymentRegisterBinding(
    val method: Spinner,
    val name: TextInputEditText,
    val expirationDate: TextInputEditText,
    val cvv: TextInputEditText,
    val cardNumber: TextInputEditText,
    val submitButton: Button
) {
    companion object {
        fun fromView(view: View): PaymentRegisterBinding {
            return PaymentRegisterBinding(
                method = view.findViewById(R.id.payment_type_input),
                name = view.findViewById(R.id.payment_name_input),
                expirationDate = view.findViewById(R.id.payment_expiration_date_input),
                cvv = view.findViewById(R.id.payment_cvv_input),
                cardNumber = view.findViewById(R.id.payment_card_number_input),
                submitButton = view.findViewById(R.id.payment_submit_button)
            )
        }
    }
}