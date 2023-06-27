package mb.safeEat.services.api.dto

data class AddressDto(
    val id: String? = null,
    val name: String? = null,
    val street: String? = null,
    val number: String? = null,
    val complement: String? = null,
    val city: String? = null,
    val postalCode: String? = null,
)
