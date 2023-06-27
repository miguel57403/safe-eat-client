package mb.safeEat.services.api.dto

data class IngredientDto(
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    var restrictionIds: MutableList<String?>? = null,
)
