package mb.safeEat.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.card.MaterialCardView
import mb.safeEat.R
import mb.safeEat.extensions.Alertable

class AllergyEditFragment(private val navigation: NavigationListener) : AllergyListener, Fragment(), Alertable  {
    // TODO: Create a fragment for allergies buttons
    private lateinit var items: RecyclerView
    private var data = ArrayList<Allergy>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_allergy_edit, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader(view)
        initialAdapter(view)
        loadInitialData()
    }

    private fun initHeader(view: View) {
        val title = view.findViewById<TextView>(R.id.header_title)
        val backButton = view.findViewById<MaterialCardView>(R.id.header_back_button)
        title.text = resources.getString(R.string.t_edit_allergies)
        backButton.setOnClickListener { navigation.onBackPressed() }
    }

    private fun initialAdapter(view: View) {
        items = view.findViewById(R.id.allergies_buttons)
        items.layoutManager = FlexboxLayoutManager(view.context).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.CENTER
        }
        items.adapter = AllergyAdapter(this)
        items.visibility = View.VISIBLE
    }

    private fun loadInitialData() {
        // TODO: load data from API
        (items.adapter as AllergyAdapter).__feedData()
    }

    override fun onClickAllergy(allergy: Allergy, position: Int) {
        data[position] = allergy.copy(selected = !allergy.selected)
        (items.adapter as AllergyAdapter).updateItem(position, data[position])
    }
}
