package mb.safeEat.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView
import mb.safeEat.R

class FeedbackFragment(private val navigation: NavigationListener) : Fragment() {
    private var score = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_feedback, container, false)
        onInit(view)
        return view
    }

    private fun onInit(view: View) {
        initHeader(view)
        initStars(view)
        initScreenEvents(view)
    }

    private fun initScreenEvents(view: View) {
        val submitButton = view.findViewById<Button>(R.id.feedback_submit)
        submitButton.setOnClickListener { navigation.onBackPressed() }
    }

    private fun initHeader(view: View) {
        val title = view.findViewById<TextView>(R.id.header_title)
        val backButton = view.findViewById<MaterialCardView>(R.id.header_back_button)
        title.text = resources.getString(R.string.t_feedback)
        backButton.setOnClickListener { navigation.onBackPressed() }
    }

    private fun initStars(view: View) {
        val stars = arrayListOf<ImageView>(
            view.findViewById(R.id.feedback_star1),
            view.findViewById(R.id.feedback_star2),
            view.findViewById(R.id.feedback_star3),
            view.findViewById(R.id.feedback_star4),
            view.findViewById(R.id.feedback_star5)
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
            stars[i].setOnClickListener { updateScoreUI(i + 1) }
        }

        updateScoreUI(3)
    }
}