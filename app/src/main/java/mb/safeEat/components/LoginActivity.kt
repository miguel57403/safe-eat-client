package mb.safeEat.components

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import mb.safeEat.R
import mb.safeEat.extensions.AlertColors
import mb.safeEat.extensions.CustomSnackbar
import mb.safeEat.functions.cleanIntentStack
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api
import mb.safeEat.services.api.authorization
import mb.safeEat.services.api.dto.LoginDto
import mb.safeEat.services.state.state

//import mb.safeEat.services.state.state

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        onInit()
    }

    private fun onInit() {
        initScreenEvents()
        checkIfLoggedIn()
    }

    private fun initScreenEvents() {
        val withoutAccountContainer =
            findViewById<ConstraintLayout>(R.id.login_material_without_account_container)
        val loginButton = findViewById<Button>(R.id.login_material_submit)

        withoutAccountContainer.setOnClickListener { navigateToRegister() }
        loginButton.setOnClickListener {
            hideKeyboard(it)
            doLogin()
        }
    }

    // Navigation
    private fun navigateToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        cleanIntentStack(intent)
        startActivity(intent)
    }

    // Actions
    private fun doLogin() {
//        val emailInput = findViewById<TextInputEditText>(R.id.login_material_email_input)
//        val passwordInput = findViewById<TextInputEditText>(R.id.login_material_password_input)
//        val body = LoginDto(emailInput.text.toString(), passwordInput.text.toString())
        val emailInput = "afonso@safeeat.com"
        val passwordInput = "123"
        val body = LoginDto(emailInput, passwordInput)


        if (!validateBody(body)) return

        suspendToLiveData {
            val tokenResponse = api.auth.login(body)
            authorization.setAuthorization("Bearer ${tokenResponse.token}")
            val userResponse = api.users.findMe()
            state.user.postValue(userResponse)
            tokenResponse.token!!
        }.observe(this) { result ->
            result.fold(onSuccess = { token ->
                navigateToHome()
            }, onFailure = {
                alertError("Internet Connection Error")
                Log.d("Api Error", "$it")
            })
        }
    }

    // Extras
    private fun validateBody(body: LoginDto): Boolean {
        if (body.email.isNullOrBlank()) {
            alertError("Email is required")
            return false
        }
        if (body.password.isNullOrBlank()) {
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

    private fun hideKeyboard(button: View) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(button.windowToken, 0)
    }

    private fun checkIfLoggedIn() {
//        if (state.user.value != null) {
//            navigateToHome()
//        }
    }
}
