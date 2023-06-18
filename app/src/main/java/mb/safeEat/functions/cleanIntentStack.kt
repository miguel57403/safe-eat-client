package mb.safeEat.functions

import android.content.Intent

fun cleanIntentStack(intent: Intent) {
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
}