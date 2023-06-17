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

class AllergyEditActivity(private val navigation: NavigationListener) : AllergyListener, Fragment()  {
    // TODO: Create a fragment for allergies buttons
    private lateinit var allergiesButtons: RecyclerView
    private var allergyList = ArrayList<Allergy>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_allergy_edit, container, false)
        if (view != null) onInit(view)
        return view
    }

    private fun onInit(view: View) {
        initHeader(view)
        initAllergiesButtons(view)
        updateInitialAllergies()
    }

    private fun initHeader(view: View) {
        val title = view.findViewById<TextView>(R.id.header_title)
        val backButton = view.findViewById<MaterialCardView>(R.id.header_back_button)
        title.text = resources.getString(R.string.t_edit_allergies)
        backButton.setOnClickListener { navigation.onBackPressed() }
    }

    private fun initAllergiesButtons(view: View) {
        allergiesButtons = view.findViewById(R.id.allergies_buttons)
        allergiesButtons.layoutManager = FlexboxLayoutManager(view.context).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.CENTER
        }
        allergiesButtons.adapter = AllergyAdapter(this)
        allergiesButtons.visibility = View.VISIBLE
    }

    private fun updateInitialAllergies() {
        val list = listOf(
            "Halal",
            "Kosher",
            "Hypertension",
            "Diabetes",
            "Gluten intolerance",
            "Seafood allergy",
            "Lactose intolerance",
            "Veganism",
            "Vegetarianism"
        )
        list.forEach { allergyList.add(Allergy(it, false)) }
        (allergiesButtons.adapter as AllergyAdapter).updateData(allergyList)
    }

    override fun onClickAllergy(allergy: Allergy, position: Int) {
        allergyList[position] = allergy.copy(selected = !allergy.selected)
        (allergiesButtons.adapter as AllergyAdapter).updateItem(position, allergyList[position])
    }
}