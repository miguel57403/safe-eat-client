package mb.safeEat.services.api.models

import mb.safeEat.components.Product


data class Item (
    val id: String? = null,
    val product: Product? = null,
    val quantity:Int = 0,
    val subtotal:Double = 0.0
)