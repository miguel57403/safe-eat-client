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
    private lateinit var items: RecyclerView
    private var data = ArrayList<Allergy>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allergy)
        initAdapter()
        loadInitialData()
    }

    private fun initAdapter() {
        items = findViewById(R.id.allergies_buttons)
        items.layoutManager = FlexboxLayoutManager(this).apply {
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

interface AllergyListener {
    fun onClickAllergy(allergy: Allergy, position: Int)
}

class AllergyAdapter(
    private val listener: AllergyListener
) : RecyclerView.Adapter<AllergyAdapter.ViewHolder>() {
    private var data: ArrayList<Allergy> = ArrayList()

    @SuppressLint("NotifyDataSetChanged")
    fun loadInitialData(newData: ArrayList<Allergy>) {
        data = newData
        notifyDataSetChanged()
    }

    // TODO: Remove this function
    fun __feedData() {
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
        val localData = list.map { Allergy(it, false) }.toCollection(ArrayList())
        loadInitialData(localData)
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
