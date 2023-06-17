package mb.safeEat.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.google.android.material.card.MaterialCardView
import mb.safeEat.R

class PictureDialog : DialogFragment() {
    var imageResource: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_picture, container)
    }

    override fun onStart() {
        if (imageResource == null)
            throw NullPointerException("PictureDialog.imageResource is null")
        super.onStart()
        dialog!!.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT, // width
            WindowManager.LayoutParams.WRAP_CONTENT // height
        )
        val closeButton = dialog!!.window!!.findViewById<MaterialCardView>(R.id.picture_close)
        val image = dialog!!.window!!.findViewById<ImageView>(R.id.picture_image)
        closeButton.setOnClickListener { dialog!!.dismiss() }
        image.setImageResource(imageResource!!)
    }
}
