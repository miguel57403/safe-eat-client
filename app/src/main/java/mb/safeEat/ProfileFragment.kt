package mb.safeEat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        onInit()
        return view
    }

    private fun onInit() {
        navigateTo(ProfileInitialFragment())
    }

    private fun navigateTo(fragment: Fragment) {
        childFragmentManager
            .beginTransaction()
            .add(R.id.user_container, fragment)
            .commit()
    }
}