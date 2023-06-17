package mb.safeEat.components

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.*
import mb.safeEat.R

class AllergyActivity : AllergyListener, AppCompatActivity() {
    private lateinit var allergiesButtons: RecyclerView
    private var allergyList = ArrayList<Allergy>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allergy)
        initAllergiesButtons()
        updateInitialAllergies()
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

interface AllergyListener {
    fun onClickAllergy(allergy: Allergy, position: Int)
}

class AllergyAdapter(
    private val listener: AllergyListener
) : RecyclerView.Adapter<AllergyAdapter.ViewHolder>() {
    private var data: ArrayList<Allergy> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: ArrayList<Allergy>) {
        data = newData
        notifyDataSetChanged()
    }

    fun updateItem(position: Int, allergy: Allergy) {
        data[position] = allergy
        notifyItemChanged(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.button_allergy, parent, false)
    )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        data.getOrNull(position)?.also { item ->
            holder.bind(item)
            holder.button.setOnClickListener { listener.onClickAllergy(item, position) }
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val button: Button = itemView.findViewById(R.id.button_allergy)
        fun bind(allergy: Allergy) {
            button.text = allergy.value
            button.isSelected = allergy.selected
        }
    }
}

data class Allergy(
    val value: String,
    val selected: Boolean
)
