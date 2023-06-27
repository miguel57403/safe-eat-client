package mb.safeEat.services.api.dto

data class ProductSectionDto(
    val id: String? = null,
    val name: String? = null,
    val productIds: List<String>? = null,
)
