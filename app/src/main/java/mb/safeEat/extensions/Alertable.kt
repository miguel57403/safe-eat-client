package mb.safeEat.extensions

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar
import mb.safeEat.functions.errorMessageFromThrowable

interface Alertable {
    fun alertError(message: String) {
        CustomSnackbar.make(
            requireView(),
            message,
            Snackbar.LENGTH_SHORT,
            AlertColors.error(requireContext())
        ).unwrap().show()
    }

    fun alertInfo(message: String) {
        CustomSnackbar.make(
            requireView(),
            message,
            Snackbar.LENGTH_SHORT,
            AlertColors.info(requireContext())
        ).unwrap().show()
    }

    fun alertThrowable(throwable: Throwable) {
        alertError(errorMessageFromThrowable(throwable))
    }

    fun requireContext(): Context
    fun requireView(): View
}
