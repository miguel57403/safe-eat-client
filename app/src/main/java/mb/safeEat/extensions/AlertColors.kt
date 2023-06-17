package mb.safeEat.extensions

import android.content.Context
import androidx.core.content.ContextCompat
import mb.safeEat.R

@Suppress("unused")
data class AlertColors(
    val backgroundColor: Int,
    val textColor: Int
) {
    companion object {
        fun success(context: Context) = AlertColors(
            ContextCompat.getColor(context, R.color.success),
            ContextCompat.getColor(context, R.color.on_success),
        )
        fun error(context: Context) = AlertColors(
            ContextCompat.getColor(context, R.color.error),
            ContextCompat.getColor(context, R.color.on_error),
        )
        fun warning(context: Context) = AlertColors(
            ContextCompat.getColor(context, R.color.warning),
            ContextCompat.getColor(context, R.color.on_warning),
        )
        fun info(context: Context) = AlertColors(
            ContextCompat.getColor(context, R.color.info),
            ContextCompat.getColor(context, R.color.on_info),
        )
    }
}
