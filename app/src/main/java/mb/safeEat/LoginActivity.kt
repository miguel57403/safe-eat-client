package mb.safeEat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()

        val textViewWithoutAccount = findViewById<TextView>(R.id.textViewWithoutAccount)
        val textViewSignUp = findViewById<TextView>(R.id.textViewSignUp)

        textViewWithoutAccount.setOnClickListener { navigateToRegister() }
        textViewSignUp.setOnClickListener { navigateToRegister() }
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}