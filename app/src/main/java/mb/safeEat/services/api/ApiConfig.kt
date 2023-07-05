package mb.safeEat.services.api

enum class ApiLogLevel {
    None, Basic, Full
}

data class ApiConfig(
    val baseUrl: String,
    val logLevel: ApiLogLevel,
)

val config = ApiConfig(
    baseUrl = "https://safe-eat-api.azurewebsites.net/",
    logLevel = ApiLogLevel.Full,
)
