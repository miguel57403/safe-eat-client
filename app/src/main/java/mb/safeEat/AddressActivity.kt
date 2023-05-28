package mb.safeEat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.android.material.card.MaterialCardView

class AddressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)
        initHeader()
    }

    private fun initHeader() {
        val title = findViewById<TextView>(R.id.header_title)
        val backButton = findViewById<MaterialCardView>(R.id.header_back_button)
        title.text = resources.getString(R.string.t_address)
        backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }
}