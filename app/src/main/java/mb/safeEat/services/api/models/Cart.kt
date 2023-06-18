package mb.safeEat.services.api.models

import android.content.ClipData.Item

data class Cart (
    val id: String? = null,
    val quantity: Int = 0,
    val subtotal: Double = 0.0,
    val items: List<Item> = ArrayList()
)
