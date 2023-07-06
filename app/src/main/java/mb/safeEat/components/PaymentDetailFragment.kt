package mb.safeEat.components

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import mb.safeEat.R
import mb.safeEat.extensions.Alertable
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api
import mb.safeEat.services.api.models.Ingredient

class PaymentDetailFragment(private val navigation: NavigationListener) : Fragment(),
    PaymentListener, Alertable {
    override fun onPaymentSelected(payment: Payment) {
        TODO("Not yet implemented")
    }

    override fun onPaymentEdit(payment: Payment) {
        TODO("Not yet implemented")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_payment_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader(view)
        iniSpinner(view)
        //initAdapter(view)
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
            this.context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, type) }
    }

}