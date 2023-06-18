package mb.safeEat.services.api.models

import mb.safeEat.components.Restaurant


data class Advertisement (
        val id: String? = null,
        val title: String? = null,
        val image: String? = null,
        val restaurant: Restaurant? = null

)