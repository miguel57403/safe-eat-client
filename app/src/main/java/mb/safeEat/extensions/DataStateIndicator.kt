package mb.safeEat.extensions

import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import mb.safeEat.R

class DataStateIndicator(val view: View) {
    private val container = view.findViewById<ConstraintLayout>(R.id.data_state_indicator)
    private val loading = view.findViewById<FrameLayout>(R.id.data_state_loading)
    private val noData = view.findViewById<LinearLayout>(R.id.data_state_no_data)
    private val error = view.findViewById<LinearLayout>(R.id.data_state_error)

    fun showLoading() {
        container.visibility = View.VISIBLE
        loading.visibility = View.VISIBLE
        noData.visibility = View.GONE
        error.visibility = View.GONE
    }

    fun showNoData() {
        container.visibility = View.VISIBLE
        loading.visibility = View.GONE
        noData.visibility = View.VISIBLE
        error.visibility = View.GONE
    }

    fun showError() {
        container.visibility = View.VISIBLE
        loading.visibility = View.GONE
        noData.visibility = View.GONE
        error.visibility = View.VISIBLE
    }

    fun hide() {
        container.visibility = View.GONE
    }

    fun toggle(hasData: Boolean) {
        if (hasData) {
            hide()
        } else {
            showNoData()
        }
    }
}
