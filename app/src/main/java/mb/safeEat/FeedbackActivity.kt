package mb.safeEat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class FeedbackActivity : AppCompatActivity() {
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        val stars = arrayListOf<ImageView>(
            findViewById(R.id.feedback_star1),
            findViewById(R.id.feedback_star2),
            findViewById(R.id.feedback_star3),
            findViewById(R.id.feedback_star4),
            findViewById(R.id.feedback_star5)
        )

        fun updateScoreUI(newScore: Int) {
            score = newScore
            for (j in stars.indices) {
                if (j < newScore) {
                    stars[j].setImageResource(R.drawable.baseline_star_24)
                } else {
                    stars[j].setImageResource(R.drawable.baseline_star_border_24)
                }
            }
        }

        for (i in stars.indices) {
            val star = stars[i]
            star.setOnClickListener { updateScoreUI(i + 1) }
        }

        updateScoreUI(3)
    }
}