package mb.safeEat.components

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import mb.safeEat.R
import mb.safeEat.extensions.AlertColors
import mb.safeEat.extensions.CustomSnackbar
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.LoginBody
import mb.safeEat.services.api.api
import mb.safeEat.services.api.authorization
import java.util.Locale

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val withoutAccountContainer =
            findViewById<ConstraintLayout>(R.id.login_material_without_account_container)
        val loginButton = findViewById<Button>(R.id.login_material_submit)

        withoutAccountContainer.setOnClickListener { navigateToRegister() }
        loginButton.setOnClickListener { doLogin() }
    }

    // Navigation
    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    // Actions
    private fun doLogin() {
        val emailInput = findViewById<TextInputEditText>(R.id.login_material_email_input)
        val passwordInput = findViewById<TextInputEditText>(R.id.login_material_password_input)
        val body = LoginBody(emailInput.text.toString(), passwordInput.text.toString())
        if (!validateBody(body)) return

        suspendToLiveData { api.auth.login(body) }.observe(this) { result ->
            result.fold(onSuccess = {
                when (it.tokenType.lowercase(Locale.getDefault())) {
                    "bearer" -> {
                        authorization.setAuthorization("Bearer ${it.accessToken}")
                        navigateToHome()
                    }

                    else -> {
                        alertError("Internal Error: Unknown tokenType")
                    }
                }
            }, onFailure = {
                alertError("Internet Connection Error")
                Log.d("Api Error", "$it")
            })
        }
    }

    // Extras
    private fun validateBody(body: LoginBody): Boolean {
        if (body.username.isBlank()) {
            alertError("Email is required")
            return false
        }
        if (body.password.isBlank()) {
            alertError("Password is required")
            return false
        }
        return true
    }

    private fun alertError(message: String) {
        CustomSnackbar.make(
            findViewById<FrameLayout>(R.id.login_material_container),
            message,
            Snackbar.LENGTH_SHORT,
            AlertColors.error(applicationContext)
        ).unwrap().show()
    }
}
