package mb.safeEat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout

class LoginMaterialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_material)
        supportActionBar?.hide()

        val withoutAccountContainer = findViewById<ConstraintLayout>(R.id.login_material_without_account_container)

        withoutAccountContainer.setOnClickListener { navigateToRegister() }
    }

    private fun navigateToRegister() {
        val intent = Intent(this, RegisterMaterialActivity::class.java)
        startActivity(intent)
    }
}