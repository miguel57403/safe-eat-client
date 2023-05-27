package mb.safeEat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val withAccountContainer = findViewById<ConstraintLayout>(R.id.register_material_with_account_container)
        val submit = findViewById<Button>(R.id.register_material_submit)

        withAccountContainer.setOnClickListener { navigateToLogin() }
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
