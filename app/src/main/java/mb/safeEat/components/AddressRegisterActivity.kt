package mb.safeEat.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import mb.safeEat.R

// TODO: Rename file
class AddressRegisterFragment(private val navigation: NavigationListener) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.activity_address_register, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader(view)
    }

    private fun initHeader(view: View) {
        val title = view.findViewById<TextView>(R.id.header_title)
        val backButton = view.findViewById<MaterialCardView>(R.id.header_back_button)
        title.text = resources.getString(R.string.t_new_address)
        backButton.setOnClickListener { navigation.onBackPressed() }
    }
}
