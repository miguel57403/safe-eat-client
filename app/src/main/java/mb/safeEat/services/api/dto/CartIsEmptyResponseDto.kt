package mb.safeEat.services.api.dto

data class CartIsEmptyResponseDto(
    val isEmpty: Boolean,
    val restaurantId: String?,
)