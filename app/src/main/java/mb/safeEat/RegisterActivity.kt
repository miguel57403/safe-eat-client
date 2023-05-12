package mb.safeEat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        val withAccount = findViewById<TextView>(R.id.register_with_account)
        val loginLink = findViewById<TextView>(R.id.register_login_link)
        val submit = findViewById<Button>(R.id.register_submit)

        withAccount.setOnClickListener { navigateToLogin() }
        loginLink.setOnClickListener { navigateToLogin() }
        submit.setOnClickListener { navigateToAllergies() }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToAllergies() {
        val intent = Intent(this, AllergyActivity::class.java)
        startActivity(intent)
    }
}