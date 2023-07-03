package mb.safeEat.functions

import java.text.DecimalFormat

fun formatPrice(unit: String, price: Double?): String {
    return if (price == null) {
        "NaN"
    } else {
        unit + priceFormatter.format(price)
    }
}

val priceFormatter = DecimalFormat("#.##")
