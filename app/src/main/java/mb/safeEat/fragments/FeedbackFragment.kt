package mb.safeEat.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mb.safeEat.R
import mb.safeEat.activities.NavigationListener
import mb.safeEat.extensions.Alertable
import mb.safeEat.functions.initHeader
import mb.safeEat.functions.suspendToLiveData
import mb.safeEat.services.api.api
import mb.safeEat.services.api.dto.FeedbackDto
import mb.safeEat.services.api.models.Order

data class FeedbackParams(val orderId: String)

class FeedbackFragment(
    private val navigation: NavigationListener,
    private val params: FeedbackParams
) : Fragment(), Alertable {
    private var score = 0
    private var loading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_feedback, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader(view, navigation, R.string.t_feedback)
        initStars(view)
        initScreenEvents(view)
        loadInitialData(view)
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

    private fun initScreenEvents(view: View) {
        val submitButton = view.findViewById<Button>(R.id.feedback_submit)
        submitButton.setOnClickListener { doFeedback(submitButton, view, params.orderId) }
    }

    private fun loadInitialData(view: View) {
        suspendToLiveData { api.orders.findById(params.orderId) }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = { order ->
                updateUi(view, order)
            }, onFailure = {
                alertThrowable(it)
            })
        }
    }

    private fun doFeedback(button: Button , view: View, orderId: String) {
        if (loading) return
        loading = true
        button.isEnabled = false
        val comment = view.findViewById<TextInputEditText>(R.id.feedback_comment_input)
        val body = FeedbackDto(rating = score, comment = comment.text.toString())
        suspendToLiveData {
            api.feedbacks.create(body, orderId)
        }.observe(viewLifecycleOwner) { result ->
            result.fold(onSuccess = {
                alertSuccess("Feedback sent!")
                lifecycleScope.launch {
                    delay(1500)
                    button.isEnabled = true
                    loading = false
                    navigation.onBackPressed()
                }
                navigation.onBackPressed()
            }, onFailure = {
                loading = false
                button.isEnabled = true
                alertThrowable(it)
            })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUi(view: View, order: Order) {
        val feedBackText = view.findViewById<TextView>(R.id.feedback_text)
        val imageFeedBack = view.findViewById<ImageView>(R.id.feedback_restaurant_image)

        feedBackText.text = "How to as the ${order.restaurant?.name}"

        Glide.with(view) //
            .load(order.restaurant?.logo) //
            .apply(RequestOptions.centerInsideTransform()) //
            .transition(DrawableTransitionOptions.withCrossFade()) //
            .into(imageFeedBack)
    }
}