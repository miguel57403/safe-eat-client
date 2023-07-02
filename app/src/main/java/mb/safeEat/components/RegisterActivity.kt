package mb.safeEat.components

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import mb.safeEat.R
import mb.safeEat.extensions.Alertable
import mb.safeEat.functions.cleanIntentStack

class RegisterActivity : AppCompatActivity(), Alertable {
    override fun requireView(): View = findViewById(R.id.register_material_container)
    override fun requireContext(): Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initScreenEvents()
    }

    private fun initScreenEvents() {
        val withAccountContainer =
            findViewById<ConstraintLayout>(R.id.register_material_with_account_container)
        val submitButton = findViewById<Button>(R.id.register_material_submit)

        withAccountContainer.setOnClickListener { navigateToLogin() }
        submitButton.setOnClickListener {
            hideKeyboard(submitButton)
            submit()
        }
    }

    private fun submit() {
        val name = findViewById<TextInputEditText>(R.id.register_material_name_input)
        val email = findViewById<TextInputEditText>(R.id.register_material_email_input)
        val cellphone = findViewById<TextInputEditText>(R.id.register_material_cellphone_input)
        val password = findViewById<TextInputEditText>(R.id.register_material_password_input)
        val confirmPassword =
            findViewById<TextInputEditText>(R.id.register_material_confirm_password_input)
        val params = AllergyParams(
            password = password.text.toString(),
            name = name.text.toString(),
            email = email.text.toString(),
            cellphone = cellphone.text.toString(),
        )
        if (!validateParams(params, confirmPassword.text.toString())) return
        navigateToAllergies(params)
    }

    private fun validateParams(body: AllergyParams, confirmPassword: String): Boolean {
        if (body.name.isBlank()) {
            alertError("Name is required")
            return false
        }
        if (body.email.isBlank()) {
            alertError("Email is required")
            return false
        }
        if (!body.email.contains('@')) {
            alertError("Email is invalid")
            return false
        }
        if (body.cellphone.isBlank()) {
            alertError("Cellphone is required")
            return false
        }
        if (body.password.isBlank()) {
            alertError("Password is required")
            return false
        }
        if (body.password != confirmPassword) {
            alertError("Passwords don't match")
            return false
        }
        return true
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        cleanIntentStack(intent)
        startActivity(intent)
    }

    private fun navigateToAllergies(params: AllergyParams) {
        val intent = Intent(this, AllergyActivity::class.java)
        intent.putExtra("params", params)
        startActivity(intent)
    }

    private fun hideKeyboard(button: View) {
        val inputMethodManager =
            ContextCompat.getSystemService(button.context, InputMethodManager::class.java)!!
        inputMethodManager.hideSoftInputFromWindow(button.windowToken, 0)
    }
}
