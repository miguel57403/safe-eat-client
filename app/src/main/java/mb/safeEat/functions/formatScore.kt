package mb.safeEat.functions

import java.text.DecimalFormat

fun formatScore(score: Double?): String {
    return scoreFormatter.format(score)
}

val scoreFormatter = DecimalFormat("0.0")