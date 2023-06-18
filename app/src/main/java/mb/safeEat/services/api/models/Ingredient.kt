package mb.safeEat.services.api.models

data class Ingredient(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val restrictions: List<Restriction>? = null,
    val isRestricted: Boolean? = null,
)
