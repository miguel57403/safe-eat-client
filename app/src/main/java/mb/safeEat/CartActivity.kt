package mb.safeEat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class CartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        initAdapter()
    }
    private fun initAdapter() {
        val adapter = CartAdapter(createList())
        val rv_home = findViewById<RecyclerView>(R.id.rv_home)
        rv_home.layoutManager = LinearLayoutManager(this)
        rv_home.adapter = adapter
    }
    private fun createList(): ArrayList<Product> {
        val list = ArrayList<Product>()
        list.add(Product(product = "Pizza acebolada", quantifyItem = 3, priceItem = 14.99F))
        list.add(Product(product = "Pizza 4 queijos", quantifyItem = 1, priceItem = 2.99F))
        list.add(Product(product = "Pizza de achova", quantifyItem = 2, priceItem = 2.99F))
        return list
    }
}

class CartAdapter(private var data: ArrayList<Product>) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false))
    }
    override fun getItemCount(): Int {
        return data.size
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bindView(item, position)
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val containermeu = itemView.findViewById<LinearLayout>(R.id.containerMeu)
        val product = itemView.findViewById<TextView>(R.id.cart_item_product)
        val quantifyItem = itemView.findViewById<TextView>(R.id.quantifyItem)
        val priceItem = itemView.findViewById<TextView>(R.id.priceItem)
        val icon = itemView.findViewById<ImageView>(R.id.iconWarning)
        fun bindView(item: Product,position: Int) {
            if(position == 1 ){
                val bg = ContextCompat.getDrawable(itemView.context, R.drawable.item_without_border_)
                containermeu.background = bg
                icon.visibility = View.GONE
            }
            if(position == 2 ){
                val bg = ContextCompat.getDrawable(itemView.context, R.drawable.item_without_border_)
                containermeu.background = bg
                icon.visibility = View.GONE
            }
            product.text = item.product
            quantifyItem.text = item.quantifyItem.toString()
            priceItem.text = ("€" + item.priceItem.toString())

        }
    }

}
data class Product(
    val product: String,
    val quantifyItem: Int,
    val priceItem: Float,
)