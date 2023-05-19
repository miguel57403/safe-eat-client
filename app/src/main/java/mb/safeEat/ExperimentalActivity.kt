package mb.safeEat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

// Dialog: https://www.youtube.com/watch?v=ZPbOKrJzXno&ab_channel=TiagoAguiar

class ExperimentalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_experimental)

        val openDialog = findViewById<Button>(R.id.open_dialog)
        openDialog.setOnClickListener {
            val dialog = PictureDialogFragment()
            dialog.show(supportFragmentManager, dialog.tag)
        }
    }
}
