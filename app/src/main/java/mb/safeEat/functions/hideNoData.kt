package mb.safeEat.functions

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import mb.safeEat.R

// Used with component_no_data.xml
fun hideNoData(view: View) {
    val noData = view.findViewById<ConstraintLayout>(R.id.no_data_layout)
    noData.visibility = View.GONE
}
