package mb.safeEat.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.DialogFragment
import mb.safeEat.R

typealias OnConfirm = (confirm: Boolean) -> Unit

class RestrictionAlertDialog : DialogFragment() {
    private var onConfirmListener: OnConfirm = { _ -> }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_restriction_alert, container)

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT, // width
            WindowManager.LayoutParams.WRAP_CONTENT // height
        )
        val yesButton = dialog?.window?.findViewById<Button>(R.id.restriction_alert_button_yes)
        val noButton = dialog?.window?.findViewById<Button>(R.id.restriction_alert_button_no)
        yesButton?.setOnClickListener {
            onConfirmListener(true)
            dismiss()
        }
        noButton?.setOnClickListener {
            onConfirmListener(false)
            dismiss()
        }
    }

    fun setOnConfirmListener(listener: OnConfirm) {
        onConfirmListener = listener
    }
}
