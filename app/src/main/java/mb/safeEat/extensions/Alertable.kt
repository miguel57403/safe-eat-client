package mb.safeEat.extensions

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar

interface Alertable {
    fun alertError(message: String) {
        CustomSnackbar.make(
            requireView(),
            message,
            Snackbar.LENGTH_SHORT,
            AlertColors.error(requireContext())
        ).unwrap().show()
    }

    fun requireContext(): Context
    fun requireView(): View
}
