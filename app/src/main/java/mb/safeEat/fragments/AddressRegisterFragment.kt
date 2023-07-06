package mb.safeEat.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mb.safeEat.R
import mb.safeEat.activities.NavigationListener
import mb.safeEat.extensions.Alertable
import mb.safeEat.functions.initHeader
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api
import mb.safeEat.services.api.dto.AddressDto

data class AddressRegisterParams(val initialData: Address?) {
    fun isEdit() = initialData != null
}

class AddressRegisterFragment(
    private val navigation: NavigationListener,
    private val params: AddressRegisterParams,
) : Fragment(), Alertable {
    private lateinit var binding: AddressBinding
    private var loading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.activity_address_register, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AddressBinding.fromView(view)
        val title = if (params.isEdit()) R.string.m_address_edit else R.string.m_address_new
        initHeader(view, navigation, title)
        initScreenEvents(view)
    }

    private fun initScreenEvents(view: View) {
        val submitButton = view.findViewById<Button>(R.id.address_register_button)
        submitButton.setOnClickListener {
            hideKeyboard(it)
            submit(submitButton)
        }

        if (params.isEdit()) {
            binding.update(params.initialData!!)
        }
    }

    private fun submit(button: Button) {
        if (loading) return

        val body = binding.toDto(params.initialData?.id)
        if (!validateBody(body)) return

        loading = true
        button.isEnabled = false
        alertInfo("Loading...")
        suspendToLiveData {
            params.isEdit().let {
                if (it) {
                    api.addresses.update(body)
                    "Address updated!"
                } else {
                    api.addresses.create(body)
                    "Address created!"
                }
            }
        }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { message ->
                alertSuccess(message)
                lifecycleScope.launch {
                    delay(1500)
                    button.isEnabled = true
                    loading = false
                    navigation.onBackPressed()
                }
            }, onFailure = {
                alertThrowable(it)
                button.isEnabled = true
                loading = false
            })
        }
    }

    private fun validateBody(body: AddressDto): Boolean {
        if (body.name.isNullOrBlank()) {
            alertError("Name is required")
            return false
        }
        if (body.city.isNullOrBlank()) {
            alertError("City is required")
            return false
        }
        if (body.street.isNullOrBlank()) {
            alertError("Street is required")
            return false
        }
        if (body.number.isNullOrBlank()) {
            alertError("Number is required")
            return false
        }
        if (body.postalCode.isNullOrBlank()) {
            alertError("Postal code is required")
            return false
        }
        if (body.complement.isNullOrBlank()) {
            alertError("Complement is required")
            return false
        }
        return true
    }

    private fun hideKeyboard(button: View) {
        val inputMethodManager =
            ContextCompat.getSystemService(button.context, InputMethodManager::class.java)!!
        inputMethodManager.hideSoftInputFromWindow(button.windowToken, 0)
    }
}

data class AddressBinding(
    val name: EditText,
    val city: EditText,
    val street: EditText,
    val number: EditText,
    val postalCode: EditText,
    val complement: EditText,
) {
    fun update(address: Address) {
        name.setText(address.name)
        city.setText(address.city)
        street.setText(address.street)
        number.setText(address.number)
        postalCode.setText(address.postalCode)
        complement.setText(address.complement)
    }

    fun toDto(id: String?) = AddressDto(
        id,
        name.text.toString(),
        street.text.toString(),
        number.text.toString(),
        complement.text.toString(),
        city.text.toString(),
        postalCode.text.toString()
    )

    companion object {
        fun fromView(view: View): AddressBinding {
            return AddressBinding(
                name = view.findViewById(R.id.address_register_name_input),
                city = view.findViewById(R.id.address_register_city_input),
                street = view.findViewById(R.id.address_register_street_input),
                number = view.findViewById(R.id.address_register_number_input),
                postalCode = view.findViewById(R.id.address_register_postal_code_input),
                complement = view.findViewById(R.id.address_register_complement_input),
            )
        }
    }
}
