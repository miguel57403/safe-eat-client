package mb.safeEat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class NotificationFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notification, container, false)
        onInit()
        return view
    }

    private fun onInit() {
        navigateTo(NotificationInitialFragment())
    }

    private fun navigateTo(fragment: Fragment) {
        childFragmentManager
            .beginTransaction()
            .add(R.id.notification_container, fragment)
            .commit()
    }
}