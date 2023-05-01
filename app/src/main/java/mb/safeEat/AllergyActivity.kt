package mb.safeEat

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.*

class AllergyActivity : AllergyListener, AppCompatActivity() {
    private lateinit var allergiesButtons: RecyclerView
    private var allergyList = ArrayList<Allergy>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allergy)
        supportActionBar?.hide()
        onInit()
    }

    private fun onInit() {
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
        updateAllergies()
    }

    private fun updateAllergies() {
        (allergiesButtons.adapter as AllergyAdapter).updateData(allergyList)
    }

    override fun onClickAllergy(allergy: Allergy, position: Int) {
        allergyList[position] = Allergy(allergy.value, !allergy.selected)
        updateAllergies()
        Log.d("Click", "$allergy")
    }
}

interface AllergyListener {
    fun onClickAllergy(allergy: Allergy, position: Int)
}

class AllergyAdapter(
    private val listener: AllergyListener
) : RecyclerView.Adapter<AllergyViewHolder>() {
    private var data: ArrayList<Allergy> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newData: ArrayList<Allergy>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllergyViewHolder {
        return AllergyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.button_allergy, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: AllergyViewHolder, position: Int) {
        val item = data.getOrNull(position)
        if (item != null) {
            holder.bind(item)
            holder.button.setOnClickListener { listener.onClickAllergy(item, position) }
        }
    }
}

class AllergyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val button: Button = itemView.findViewById(R.id.button_allergy)
    fun bind(allergy: Allergy) {
        button.text = allergy.value
        button.isSelected = allergy.selected
    }
}

data class Allergy(
    val value: String,
    val selected: Boolean
)
