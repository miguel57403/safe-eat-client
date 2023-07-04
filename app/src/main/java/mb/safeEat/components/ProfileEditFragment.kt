package mb.safeEat.components

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import mb.safeEat.R
import mb.safeEat.extensions.Alertable
import mb.safeEat.functions.initHeader
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api
import mb.safeEat.services.api.dto.UserUpdateDto
import mb.safeEat.services.state.state
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ProfileEditFragment(private val navigation: NavigationListener) : Fragment(), Alertable {
    private lateinit var binding: ProfileEditBinding
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_profile_edit, container, false)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri = result.data?.data
                uri?.path?.also { path ->
                    val file = File(path)
                    val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
                    val photoPart = MultipartBody.Part.createFormData("photo", file.name, requestFile)
                    // TODO: Test upload of the user image
                    suspendToLiveData { api.users.updateImage(photoPart) }.observe(viewLifecycleOwner) { result ->
                        result.fold(onSuccess = {
                            state.user.postValue(it)
                            binding.image.setImageURI(uri)
                        }, onFailure = {
                            alertThrowable(it)
                        })
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ProfileEditBinding.fromView(view)
        initHeader(view, navigation, R.string.t_edit_profile)
        loadInitialData(view)
    }

    private fun loadInitialData(view: View) {
        state.user.observe(viewLifecycleOwner) { user ->
            // TODO: Remove overused nullables from api models
            binding.name.text = user!!.name
            binding.email.text = user.email
            binding.cellphone.text = user.cellphone
            Glide.with(view) //
                .load(user.image) //
                .apply(RequestOptions().centerCrop()) //
                .transition(DrawableTransitionOptions.withCrossFade()) //
                .into(binding.image)
        }

        binding.changePhoto.setOnClickListener { openPhotoPicker() }
        binding.cancelButton.setOnClickListener { navigation.onBackPressed() }
        binding.saveButton.setOnClickListener {
            val body = UserUpdateDto(
                password = binding.password.text.toString(),
                name = binding.name.text.toString(),
                email = binding.email.text.toString(),
                cellphone = binding.cellphone.text.toString(),
            )
            doEditProfile(body, binding.confirmPassword.text.toString())
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

    private fun openPhotoPicker() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activityResultLauncher.launch(photoPickerIntent)
    }
}

data class ProfileEditBinding(
    val name: TextView,
    val email: TextView,
    val cellphone: TextView,
    val password: TextView,
    val confirmPassword: TextView,
    val image: ImageView,
    val changePhoto: Button,
    val cancelButton: Button,
    val saveButton: Button,
) {
    companion object {
        fun fromView(view: View): ProfileEditBinding {
            return ProfileEditBinding(
                name = view.findViewById(R.id.profile_edit_name_input),
                email = view.findViewById(R.id.profile_edit_email_input),
                cellphone = view.findViewById(R.id.profile_edit_cellphone_input),
                password = view.findViewById(R.id.profile_edit_password_input),
                confirmPassword = view.findViewById(R.id.profile_edit_confirm_password_input),
                image = view.findViewById(R.id.profile_edit_photo),
                changePhoto = view.findViewById(R.id.profile_edit_photo_change),
                cancelButton = view.findViewById(R.id.profile_edit_cancel_button),
                saveButton = view.findViewById(R.id.profile_edit_save_button),
            )
        }
    }
}
