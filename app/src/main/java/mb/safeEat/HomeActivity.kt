package mb.safeEat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.Exception

// Bottom Navigation: https://www.youtube.com/watch?v=Bb8SgfI4Cm4&ab_channel=Foxandroid

class HomeActivity : AppCompatActivity(), NavigationListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        if (savedInstanceState == null) onInit()
    }

    private fun onInit() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigation.setOnItemSelectedListener {
            Log.d("Click", it.itemId.toString())
            val fragment = when (it.itemId) {
                R.id.menu_item_home -> HomeInitialFragment()
                R.id.menu_item_search -> SearchCategoryFragment()
                R.id.menu_item_cart -> CartFragment()
                R.id.menu_item_user -> ProfileFragment(this)
                R.id.menu_item_notification -> NotificationFragment(this)
                else -> throw Exception("Menu item id not defined")
            }
            navigateTo(fragment)
            true
        }

        navigateTo(HomeInitialFragment())
    }

    override fun navigateTo(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.main_body, fragment)
            .commit()
    }

    override fun onBackPressed() {
        onBackPressedDispatcher.onBackPressed()
        Log.d("Back Pressed", "Ok")
    }
}

interface NavigationListener {
    fun navigateTo(fragment: Fragment)
    fun onBackPressed()
}