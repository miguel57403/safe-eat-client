package mb.safeEat.functions

import java.text.DecimalFormat

// TODO: Spread this function to other files
fun formatPrice(unit: String, price: Double?): String {
    return if (price == null) {
        "NaN"
    } else {
        unit + priceFormatter.format(price)
    }
}

val priceFormatter = DecimalFormat("#.##")
