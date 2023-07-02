package mb.safeEat.services.api.dto

data class UserUpdateDto(
    var password: String? = null,
    val name: String? = null,
    val email: String? = null,
    val cellphone: String? = null,
    val restrictionIds: List<String>? = null,
)
