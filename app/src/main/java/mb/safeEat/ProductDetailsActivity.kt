package mb.safeEat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class ProductDetailsActivity(private val navigation: NavigationListener) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_product_details, container, false)
        if (view != null) onInit(view)
        return view
    }

    private fun onInit(view: View) {
        initHeader(view)
        initAdapter(view)
    }

    private fun initHeader(view: View) {
        val title = view.findViewById<TextView>(R.id.header_title)
        val backButton = view.findViewById<MaterialCardView>(R.id.header_back_button)
        title.text = resources.getString(R.string.t_product_details)
        backButton.setOnClickListener { navigation.onBackPressed() }
    }

    private fun initAdapter(view: View) {
        val items = view.findViewById<RecyclerView>(R.id.product_detail_items)
        items.layoutManager = LinearLayoutManager(view.context)
        items.adapter = ProductDetailAdapter(createList())
    }

    private fun createList(): java.util.ArrayList<ProductDetail> {
        return arrayListOf(
            ProductDetail("Carne moida bovina", false),
            ProductDetail("Pimenta", true),
        )
    }
}

class ProductDetailAdapter(private var data: ArrayList<ProductDetail>) :
    RecyclerView.Adapter<ProductDetailAdapter.ViewHolder>() {

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
                val color = ContextCompat.getColor(itemView.context, R.color.red)
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
