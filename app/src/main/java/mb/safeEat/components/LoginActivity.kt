package mb.safeEat.components

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.textfield.TextInputEditText
import mb.safeEat.R
import mb.safeEat.extensions.Alertable
import mb.safeEat.functions.cleanIntentStack
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api
import mb.safeEat.services.api.authorization
import mb.safeEat.services.api.dto.LoginDto
import mb.safeEat.services.state.state

class LoginActivity : AppCompatActivity(), Alertable {
    override fun requireView(): View = findViewById(R.id.login_material_container)
    override fun requireContext(): Context = this

    // TODO: Add loading to other screens
    private var loading = false

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
        val withoutAccountContainer = findViewById<ConstraintLayout>(R.id.login_material_without_account_container)
        val loginButton = findViewById<Button>(R.id.login_material_submit)

        withoutAccountContainer.setOnClickListener { navigateToRegister() }
        loginButton.setOnClickListener {
            hideKeyboard(it)
            doLogin(loginButton)
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
    private fun doLogin(button: Button) {
        if (loading) return
        val emailInput = findViewById<TextInputEditText>(R.id.login_material_email_input)
        val passwordInput = findViewById<TextInputEditText>(R.id.login_material_password_input)
        // TODO: Remove this when the app is ready
        val development = true
        val body = if (development) {
            LoginDto("afonso@safeeat.com", "123")
        } else {
            val body = LoginDto(emailInput.text.toString(), passwordInput.text.toString())
            if (!validateBody(body)) return
            body
        }

        loading = true
        button.isEnabled = false
        alertInfo("Loading...")
        suspendToLiveData {
            val tokenResponse = api.auth.login(body)
            authorization.setAuthorization("Bearer ${tokenResponse.token}")
            val userResponse = api.users.findMe()
            state.user.postValue(userResponse)
            tokenResponse.token!!
        }.observe(this) { result ->
            result.fold(onSuccess = {
                navigateToHome()
            }, onFailure = {
                alertThrowable(it)
            })
            button.isEnabled = true
            loading = false
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

    private fun hideKeyboard(button: View) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(button.windowToken, 0)
    }

    private fun checkIfLoggedIn() {
        if (state.user.value != null) {
            navigateToHome()
        }
    }
}
