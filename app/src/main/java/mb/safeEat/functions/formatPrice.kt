package mb.safeEat.functions

import java.text.DecimalFormat

fun formatPrice(unit: String, price: Double?): String {
    return if (price == null) {
        "NaN"
    } else if (price < 0.01) {
        unit + "0"
    } else {
        unit + priceFormatter.format(price)
    }
}

val priceFormatter = DecimalFormat("#.##")
