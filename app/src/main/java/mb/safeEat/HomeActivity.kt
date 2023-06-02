package mb.safeEat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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
            val fragment = when (it.itemId) {
                R.id.menu_item_home -> HomeInitialFragment(this)
                R.id.menu_item_search -> SearchCategoryFragment(this)
                R.id.menu_item_cart -> CartFragment()
                R.id.menu_item_user -> ProfileFragment(this)
                R.id.menu_item_notification -> NotificationFragment(this)
                else -> throw Exception("Menu item id not defined")
            }
            navigateTo(fragment)
            true
        }

        navigateTo(HomeInitialFragment(this))
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
    }
}

interface NavigationListener {
    fun navigateTo(fragment: Fragment)
    fun onBackPressed()
    fun getSupportFragmentManager(): FragmentManager
}