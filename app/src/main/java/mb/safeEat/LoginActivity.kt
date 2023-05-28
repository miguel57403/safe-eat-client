package mb.safeEat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val withoutAccountContainer = findViewById<ConstraintLayout>(R.id.login_material_without_account_container)
        val loginButton = findViewById<Button>(R.id.login_material_submit)

        withoutAccountContainer.setOnClickListener { navigateToRegister() }
        loginButton.setOnClickListener { doLogin() }
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun doLogin() {
        navigateToHome()
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
}