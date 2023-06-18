package mb.safeEat.services.api.models

data class Home (
        val id: String? = null,
        val advertisements: List<Advertisement> = ArrayList(),
        //val restaurantSections: List<RestaurantSection> = ArrayList()

)