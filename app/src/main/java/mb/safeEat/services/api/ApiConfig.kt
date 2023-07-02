package mb.safeEat.services.api

data class ApiConfig(
    val baseUrl: String
)

val config = ApiConfig("https://safe-eat-api.azurewebsites.net/")
