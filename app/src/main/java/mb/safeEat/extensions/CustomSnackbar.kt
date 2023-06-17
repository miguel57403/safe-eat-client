package mb.safeEat.extensions

import com.google.android.material.snackbar.Snackbar

class CustomSnackbar(private val instance: Snackbar) {
    companion object {
        fun make(
            view: android.view.View,
            text: CharSequence,
            duration: Int,
            colors: AlertColors
        ) = CustomSnackbar(Snackbar.make(view, text, duration))
            .withDefaults()
            .withAlertColors(colors)
    }

    fun withDefaults(): CustomSnackbar {
        instance.animationMode = Snackbar.ANIMATION_MODE_SLIDE
        instance.view.setOnClickListener { instance.dismiss() }
        return this
    }

    fun withAlertColors(colors: AlertColors): CustomSnackbar {
        instance.setBackgroundTint(colors.backgroundColor)
        instance.setTextColor(colors.textColor)
        return this
    }

    fun unwrap(): Snackbar {
        return instance
    }
}
