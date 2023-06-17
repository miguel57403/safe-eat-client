package mb.safeEat.services.api

data class ApiConfig(
    val baseUrl: String
)

val config = ApiConfig("https://sm-activity-57060.free.beeceptor.com")
