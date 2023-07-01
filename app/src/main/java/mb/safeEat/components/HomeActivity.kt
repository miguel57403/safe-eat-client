package mb.safeEat.components

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import mb.safeEat.R
import mb.safeEat.functions.cleanIntentStack
//import mb.safeEat.services.api.authorization
//import mb.safeEat.services.state.state
import java.lang.Exception

// Bottom Navigation: https://www.youtube.com/watch?v=Bb8SgfI4Cm4&ab_channel=Foxandroid

class HomeActivity : AppCompatActivity(), NavigationListener {
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        if (savedInstanceState == null) onInit()
    }

    private fun onInit() {
        initScreenEvents()
        navigateTo(HomeInitialFragment(this))
    }

    private fun initScreenEvents() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigation.setOnItemSelectedListener {
            val fragment = when (it.itemId) {
                R.id.menu_item_home -> HomeInitialFragment(this)
                R.id.menu_item_search -> SearchCategoryFragment(this)
                R.id.menu_item_cart -> CartFragment(this)
                R.id.menu_item_user -> ProfileFragment(this)
                R.id.menu_item_notification -> NotificationFragment(this)
                else -> throw Exception("Menu item id not defined")
            }
            navigateTo(fragment)
            true
        }

        // TODO: Uncomment this
//        state.user.observe(this) {
//            if (it == null) {
//                authorization.clearAuthorization()
//                navigateToLogin()
//            }
//        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        cleanIntentStack(intent)
        startActivity(intent)
    }

    // Navigation
    override fun navigateTo(fragment: Fragment) {
        if (fragment is HomeInitialFragment) {
            // Clear the back stack
            supportFragmentManager.popBackStackImmediate(
                null,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        } else {
            count++
        }
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.main_body, fragment)
            .commit()
    }

    override fun onBackPressed() {
        if (count == 0) {
            // Close the app if user is on the home fragment
            finish()
        } else {
            count--
            onBackPressedDispatcher.onBackPressed()
        }
    }
}

interface NavigationListener {
    fun navigateTo(fragment: Fragment)
    fun onBackPressed()
    fun getSupportFragmentManager(): FragmentManager
}