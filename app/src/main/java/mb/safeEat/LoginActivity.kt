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

        val withoutAccount = findViewById<TextView>(R.id.login_without_account)
        val signUp = findViewById<TextView>(R.id.login_sign_up)

        withoutAccount.setOnClickListener { navigateToRegister() }
        signUp.setOnClickListener { navigateToRegister() }
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}