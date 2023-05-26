package mb.safeEat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.card.MaterialCardView

class PaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        val addressButton = findViewById<MaterialCardView>(R.id.payment_address_button)
        val deliveryOptionButton = findViewById<MaterialCardView>(R.id.payment_delivery_option_button)
        val paymentKindButton = findViewById<MaterialCardView>(R.id.payment_kind_button)

        addressButton.setOnClickListener {
            Log.d("Click", "Address Button Clicked")
        }
        deliveryOptionButton.setOnClickListener {
            Log.d("Click", "Delivery Option Button Clicked")
        }
        paymentKindButton.setOnClickListener {
            Log.d("Click", "Payment Kind Button Clicked")
        }
    }
}