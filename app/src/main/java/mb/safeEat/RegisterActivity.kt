package mb.safeEat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        val withAccount = findViewById<TextView>(R.id.register_with_account)
        val loginLink = findViewById<TextView>(R.id.register_login_link)

        withAccount.setOnClickListener { navigateToLogin() }
        loginLink.setOnClickListener { navigateToLogin() }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}