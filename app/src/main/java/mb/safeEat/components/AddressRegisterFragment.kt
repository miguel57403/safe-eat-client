package mb.safeEat.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mb.safeEat.R
import mb.safeEat.extensions.Alertable
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api
import mb.safeEat.services.api.dto.AddressDto

class AddressRegisterFragment(private val navigation: NavigationListener) : Fragment(), Alertable {
    private var loading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.activity_address_register, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader(view)
        initScreenEvents(view)
    }

    private fun initHeader(view: View) {
        val title = view.findViewById<TextView>(R.id.header_title)
        val backButton = view.findViewById<MaterialCardView>(R.id.header_back_button)
        title.text = resources.getString(R.string.t_new_address)
        backButton.setOnClickListener { navigation.onBackPressed() }
    }

    private fun initScreenEvents(view: View) {
        val createButton = view.findViewById<Button>(R.id.address_register_button)
        createButton.setOnClickListener {
            hideKeyboard(it)
            doCreate(createButton, view)
        }
    }

    private fun doCreate(button: Button, view: View) {
        loading = true
        button.isEnabled = false
        alertInfo("Creating...")

        val name = view.findViewById<EditText>(R.id.new_address_name_input)
        val city = view.findViewById<EditText>(R.id.new_address_city_input)
        val street = view.findViewById<EditText>(R.id.new_address_street_input)
        val number = view.findViewById<EditText>(R.id.new_address_number_input)
        val postalCode = view.findViewById<EditText>(R.id.new_address_postal_code_input)
        val complement = view.findViewById<EditText>(R.id.new_address_complement_input)

        val address = AddressDto(
            null,
            name.text.toString(),
            street.text.toString(),
            number.text.toString(),
            complement.text.toString(),
            city.text.toString(),
            postalCode.text.toString()
        )

        suspendToLiveData {
            api.addresses.create(address)
        }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {
                alertSuccess("Address created!")
                lifecycleScope.launch {
                    delay(1500)
                    navigation.onBackPressed()
                }
            }, onFailure = {
                alertThrowable(it)
            })
            button.isEnabled = true
            loading = false
        }
    }

    private fun hideKeyboard(button: View) {
        val inputMethodManager =
            ContextCompat.getSystemService(button.context, InputMethodManager::class.java)!!
        inputMethodManager.hideSoftInputFromWindow(button.windowToken, 0)
    }
}
