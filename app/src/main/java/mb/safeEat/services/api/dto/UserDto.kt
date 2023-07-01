package mb.safeEat.services.api.dto

data class UserDto(
    val id: String? = null,
    val password: String? = null,
    val name: String? = null,
    val email: String? = null,
    val cellphone: String? = null,
    val restrictionIds: List<String>? = null,
)
