package mb.safeEat.services.api.models

data class Address(
    val id: String? = null,
    val name: String? = null,
    val street: String? = null,
    val number: String? = null,
    val complement: String? = null,
    val city: String? = null,
    val postalCode: String? = null,
    val isSelected: Boolean? = null,
) {
    fun fullAddress() = "$street, $number, $complement, $city, $postalCode"
}
