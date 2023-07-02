package mb.safeEat.components

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.*
import mb.safeEat.R
import mb.safeEat.extensions.Alertable
import mb.safeEat.functions.cleanIntentStack
import mb.safeEat.functions.getSerializableExtra
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api
import mb.safeEat.services.api.dto.UserDto
import java.io.Serializable

data class AllergyParams(
    val password: String,
    val name: String,
    val email: String,
    val cellphone: String,
): Serializable

// Keep this implementation synchronized with AllergyEditFragment.kt
class AllergyActivity : AllergyListener, AppCompatActivity(), Alertable {
    override fun requireView(): View = findViewById(R.id.allergy_activity_container)
    override fun requireContext(): Context = this

    private lateinit var params: AllergyParams
    private lateinit var items: RecyclerView
    private var data = ArrayList<Allergy>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allergy)
        loadParams()
        initAdapter()
        loadInitialData()
        initScreenEvents()
    }

    private fun loadParams() {
        params = getSerializableExtra(this, "params", AllergyParams::class.java)
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
        suspendToLiveData { api.restrictions.findAll() }.observe(this) {result ->
            result.fold(onSuccess = { restrictions ->
                val initialData = mapInitialData(restrictions)
                data = initialData
                (items.adapter as AllergyAdapter).loadInitialData(initialData)
            }, onFailure = {
                alertThrowable(it)
                Log.d("Api Error", "$it")
            })
        }
    }

    private fun mapInitialData(restrictions: List<mb.safeEat.services.api.models.Restriction>): ArrayList<Allergy> {
        return restrictions.map { Allergy(it.id!!, it.name!!, false) }.toCollection(ArrayList())
    }

    private fun initScreenEvents() {
        val button = findViewById<Button>(R.id.allergy_submit)
        button.setOnClickListener {
            hideKeyboard(button)
            val body = UserDto(
                password = params.password,
                name = params.name,
                email = params.email,
                cellphone = params.cellphone,
                restrictionIds = data.filter { it.selected }.map { it.id }
            )
            suspendToLiveData { api.auth.signup(body) }.observe(this) { result ->
                result.fold(onSuccess = {
                    navigateToLogin()
                }, onFailure = {
                    alertThrowable(it)
                    Log.d("Api Error", "$it")
                })
            }
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        cleanIntentStack(intent)
        startActivity(intent)
    }

    override fun onClickAllergy(allergy: Allergy, position: Int) {
        data[position] = allergy.copy(selected = !allergy.selected)
        (items.adapter as AllergyAdapter).updateItem(position, data[position])
    }

    private fun hideKeyboard(button: View) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(button.windowToken, 0)
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
    val id: String,
    val value: String,
    val selected: Boolean
)
