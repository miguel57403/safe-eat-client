package mb.safeEat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.Exception

// Bottom Navigation: https://www.youtube.com/watch?v=Bb8SgfI4Cm4&ab_channel=Foxandroid

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) onInit()
    }

    private fun onInit() {
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigation.setOnItemSelectedListener {
            Log.d("Click", it.itemId.toString())
            val fragment = when (it.itemId) {
                R.id.menu_item_home -> HomeFragment()
                R.id.menu_item_search -> SearchFragment()
                R.id.menu_item_cart -> CartFragment()
                R.id.menu_item_user -> UserFragment()
                R.id.menu_item_notification -> NotificationsFragment()
                else -> throw Exception("Menu item id not defined")
            }

            supportFragmentManager
                .beginTransaction()
                .add(R.id.main_body, fragment)
                .commit()

            true
        }
    }
}
