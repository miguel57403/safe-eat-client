package mb.safeEat.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mb.safeEat.R
import mb.safeEat.activities.NavigationListener
import mb.safeEat.dialogs.ProductAddedDialog
import mb.safeEat.extensions.Alertable
import mb.safeEat.extensions.DataStateIndicator
import mb.safeEat.functions.formatPrice
import mb.safeEat.functions.initHeader
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api
import mb.safeEat.services.api.dto.ItemDto
import mb.safeEat.services.api.models.Ingredient

data class ProductDetailsParams(val productId: String)

class ProductDetailsFragment(
    private val navigation: NavigationListener,
    private val params: ProductDetailsParams,
) : Fragment(), Alertable {
    private lateinit var items: RecyclerView
    private lateinit var dataStateIndicator: DataStateIndicator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_product_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataStateIndicator = DataStateIndicator(view)
        initHeader(view, navigation, R.string.t_product_details)
        initAdapter(view)
        initScreenEvents(view)
        loadInitialData(view)
    }

    private fun initAdapter(view: View) {
        items = view.findViewById(R.id.product_detail_items)
        items.layoutManager = LinearLayoutManager(view.context)
        items.adapter = ProductDetailAdapter()
    }

    private fun initScreenEvents(view: View) {
        val addToCartButton = view.findViewById<Button>(R.id.product_detail_button)
        addToCartButton.setOnClickListener { doAddToCart() }
    }

    private fun doAddToCart() {
        val body = ItemDto(productId = params.productId, quantity = 1)
        suspendToLiveData { api.items.create(body) }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {
                showSuccessDialog()
            }, onFailure = {
                alertThrowable(it)
            })
        }
    }

    private fun showSuccessDialog() {
        val dialog = ProductAddedDialog()
        dialog.show(navigation.getSupportFragmentManager(), dialog.tag)
        dialog.lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                // TODO: Try remove this observer
                if (event == Lifecycle.Event.ON_DESTROY) {
                    navigation.navigateTo(CartFragment(navigation))
                }
            }
        })
    }

    private fun loadInitialData(view: View) {
        loadProductData(view)
        loadIngredientsData(view)
    }

    private fun loadProductData(view: View) {
        suspendToLiveData {
            api.products.findById(params.productId)
        }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { product ->
                val image = view.findViewById<ImageView>(R.id.product_details_content_card_image)
                val name = view.findViewById<TextView>(R.id.product_details_content_card_title)
                val price = view.findViewById<TextView>(R.id.product_details_content_card_price)

                name.text = product.name!!
                price.text = formatPrice("€", product.price)
                // TODO: Use product.image
                image.setBackgroundResource(R.drawable.restaurant_cover)
            }, onFailure = {
                alertThrowable(it)
            })
        }
    }

    private fun loadIngredientsData(view: View) {
        dataStateIndicator.showLoading()
        suspendToLiveData {
            api.ingredients.findByAllProduct(params.productId)
        }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { ingredients ->
                val initData = mapInitialData(ingredients)
                (items.adapter as ProductDetailAdapter).loadInitialData(initData)

                val alert = view.findViewById<ConstraintLayout>(R.id.product_details_content_alert)
                alert.isVisible = ingredients.any { it.isRestricted!! }
                dataStateIndicator.toggle(ingredients.isNotEmpty())
            }, onFailure = {
                dataStateIndicator.showError()
                alertThrowable(it)
            })
        }
    }

    private fun mapInitialData(ingredients: List<Ingredient>): ArrayList<ProductDetail> {
        return ingredients.map { ingredient ->
            ProductDetail(ingredient.name!!, ingredient.isRestricted!!)
        }.toCollection(ArrayList())
    }

    @Suppress("unused")
    private fun createMockData(): java.util.ArrayList<ProductDetail> {
        return arrayListOf(
            ProductDetail("Carne moida bovina", false),
            ProductDetail("Pimenta", true),
        )
    }
}

class ProductDetailAdapter : RecyclerView.Adapter<ProductDetailAdapter.ViewHolder>() {
    private var data = ArrayList<ProductDetail>()

    @SuppressLint("NotifyDataSetChanged")
    fun loadInitialData(newData: ArrayList<ProductDetail>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_product_detail, parent, false)
    )

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(data[position])

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val content = itemView.findViewById<TextView>(R.id.product_details_item_content)
        private val image = itemView.findViewById<ImageView>(R.id.product_details_item_image)

        fun bind(item: ProductDetail) {
            content.text = item.name
            if (item.isRestrict) {
                val color = ContextCompat.getColor(itemView.context, R.color.red_500)
                content.setTextColor(color)
                image.setColorFilter(color)
            }
        }
    }
}

data class ProductDetail(
    val name: String,
    val isRestrict: Boolean,
)
