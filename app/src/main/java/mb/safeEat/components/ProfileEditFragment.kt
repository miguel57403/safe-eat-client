package mb.safeEat.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import mb.safeEat.R
import mb.safeEat.extensions.Alertable
import mb.safeEat.functions.initHeader
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api
import mb.safeEat.services.api.dto.UserUpdateDto
import mb.safeEat.services.state.state

class ProfileEditFragment(private val navigation: NavigationListener) : Fragment(), Alertable {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_profile_edit, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader(view, navigation, R.string.t_edit_profile)
        loadInitialData(view)
    }

    private fun loadInitialData(view: View) {
        val name = view.findViewById<TextView>(R.id.profile_edit_name_input)
        val email = view.findViewById<TextView>(R.id.profile_edit_email_input)
        val cellphone = view.findViewById<TextView>(R.id.profile_edit_cellphone_input)
        val password = view.findViewById<TextView>(R.id.profile_edit_password_input)
        val confirmPassword = view.findViewById<TextView>(R.id.profile_edit_confirm_password_input)

        val cancelButton = view.findViewById<Button>(R.id.profile_edit_cancel_button)
        val saveButton = view.findViewById<Button>(R.id.profile_edit_save_button)

        state.user.observe(viewLifecycleOwner) { user ->
            // TODO: Remove overused nullables from api models
            name.text = user!!.name
            email.text = user.email
            cellphone.text = user.cellphone
        }

        cancelButton.setOnClickListener { navigation.onBackPressed() }
        saveButton.setOnClickListener {
            val body = UserUpdateDto(
                password = password.text.toString(),
                name = name.text.toString(),
                email = email.text.toString(),
                cellphone = cellphone.text.toString(),
            )
            doEditProfile(body, confirmPassword.text.toString())
        }
    }

    private fun doEditProfile(body: UserUpdateDto, confirmPassword: String) {
        if (!validateBody(body, confirmPassword)) return
        suspendToLiveData { api.users.update(body) }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {
                state.user.postValue(it)
                navigation.onBackPressed()
            }, onFailure = {
                alertThrowable(it)
            })
        }
    }

    private fun validateBody(body: UserUpdateDto, confirmPassword: String): Boolean {
        if (!body.password.isNullOrBlank() && body.password != confirmPassword) {
            alertError("Passwords do not match")
            return false
        }
        if (body.name.isNullOrBlank()) {
            alertError("Name cannot be empty")
            return false
        }
        if (body.email.isNullOrBlank()) {
            alertError("Email cannot be empty")
            return false
        }
        if (body.cellphone.isNullOrBlank()) {
            alertError("Cellphone cannot be empty")
            return false
        }
        return true
    }
}