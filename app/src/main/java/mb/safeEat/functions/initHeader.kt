package mb.safeEat.functions

import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import com.google.android.material.card.MaterialCardView
import mb.safeEat.R
import mb.safeEat.activities.NavigationListener

// Used with component_header.xml
fun initHeader(view: View, navigation: NavigationListener, @StringRes titleId: Int) {
    val title = view.findViewById<TextView>(R.id.header_title)
    val backButton = view.findViewById<MaterialCardView>(R.id.header_back_button)

    title.text = view.resources.getString(titleId)
    backButton.setOnClickListener { navigation.onBackPressed() }
}
