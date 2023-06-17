@file:Suppress("unused")

package mb.safeEat.components

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import mb.safeEat.R
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.LoginBody
import mb.safeEat.services.api.api

@Suppress("FunctionName")
class ExperimentalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_experimental)

        val openDialog = findViewById<Button>(R.id.open_dialog)
        openDialog.setOnClickListener {
            suspendToLiveData {
                api.auth.login(LoginBody("username", "password"))
            }.observe(this) { result ->
                result.fold(onSuccess = { println(it) }, onFailure = { println(it) })
            }
        }
    }

    private fun _testDialogs() {
        // Dialog: https://www.youtube.com/watch?v=ZPbOKrJzXno&ab_channel=TiagoAguiar
        val openDialog = findViewById<Button>(R.id.open_dialog)
        openDialog.setOnClickListener {
            val dialog = OrderCompletedDialog()
            dialog.show(supportFragmentManager, dialog.tag)
        }
    }
}
