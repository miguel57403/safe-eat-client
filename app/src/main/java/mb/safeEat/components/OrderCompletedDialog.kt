package mb.safeEat.components

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.DialogFragment
import mb.safeEat.R

typealias OnDismissListener = () -> Unit

class OrderCompletedDialog : DialogFragment() {
    private var onDismissListener: OnDismissListener = {}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_order_completed, container)

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT, // width
            WindowManager.LayoutParams.WRAP_CONTENT // height
        )
        val okButton = dialog!!.window!!.findViewById<Button>(R.id.order_completed_ok_button)
        okButton.setOnClickListener { dialog!!.dismiss() }
    }

    fun setOnDismissListener(listener: OnDismissListener) {
        onDismissListener = listener
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener()
    }
}