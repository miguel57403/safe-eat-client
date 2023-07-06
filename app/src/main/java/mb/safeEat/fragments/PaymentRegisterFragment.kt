package mb.safeEat.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import mb.safeEat.R
import mb.safeEat.activities.NavigationListener
import mb.safeEat.extensions.Alertable

class PaymentRegisterFragment(private val navigation: NavigationListener) : Fragment(), Alertable {
    // TODO: Finish implementation
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_payment_register, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader(view)
        iniSpinner(view)
        //initScreenEvents(view)
        //loadInitialData()
    }

    private fun initHeader(view: View) {
        val title = view.findViewById<TextView>(R.id.header_title)
        val backButton = view.findViewById<MaterialCardView>(R.id.header_back_button)

        title.text = resources.getString(R.string.t_new_payment)
        backButton.setOnClickListener { navigation.onBackPressed() }
    }

    private fun iniSpinner(view: View) {
        val spinnerId = view.findViewById<Spinner>(R.id.payment_type_input)
        val type = arrayOf("Credit", "Debit")

        spinnerId.adapter =
            ArrayAdapter(view.context, android.R.layout.simple_spinner_dropdown_item, type)
    }

}