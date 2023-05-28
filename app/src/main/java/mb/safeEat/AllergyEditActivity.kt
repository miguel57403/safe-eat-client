package mb.safeEat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.card.MaterialCardView

class AllergyEditActivity : AllergyListener, AppCompatActivity()  {
    // TODO: Create a fragment for allergies buttons
    private lateinit var allergiesButtons: RecyclerView
    private var allergyList = ArrayList<Allergy>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allergy_edit)
        initAllergiesButtons()
        updateInitialAllergies()
        initHeader()
    }

    private fun initHeader() {
        val title = findViewById<TextView>(R.id.header_title)
        val backButton = findViewById<MaterialCardView>(R.id.header_back_button)
        title.text = resources.getString(R.string.t_edit_allergies)
        backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun initAllergiesButtons() {
        allergiesButtons = findViewById(R.id.allergies_buttons)
        allergiesButtons.layoutManager = FlexboxLayoutManager(this).apply {
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