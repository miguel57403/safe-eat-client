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
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api

class AddressFragment(private val navigation: NavigationListener) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_address, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader(view)
        initScreenEvents(view)
    }

    private fun initHeader(view: View) {
        val title = view.findViewById<TextView>(R.id.header_title)
        val backButton = view.findViewById<MaterialCardView>(R.id.header_back_button)

        title.text = resources.getString(R.string.t_address)
        backButton.setOnClickListener { navigation.onBackPressed() }
    }

    private fun initScreenEvents(view: View) {
        val button = view.findViewById<Button>(R.id.new_address)
        button.setOnClickListener {
            navigation.navigateTo(AddressRegisterFragment(navigation))
        }

        val addressName = view.findViewById<TextView>(R.id.address_main_text)
        suspendToLiveData { api.addresses.findAll() }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { addresses ->
                // TODO: Add a RecyclerView to show all addresses
                addressName.text = addresses[0].name
            }, onFailure = {
                println("Api Error")
            })
        }
    }
}
