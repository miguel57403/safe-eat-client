package mb.safeEat.services.api

import mb.safeEat.services.api.endpoints.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Api {
    val auth: AuthEndpoint = jsonApi(config.baseUrl).create(AuthEndpoint::class.java)
    val addresses: AddressEndpoint = jsonApi(config.baseUrl).create(AddressEndpoint::class.java)
    val advertisements: AdvertisementEndpoint = jsonApi(config.baseUrl).create(AdvertisementEndpoint::class.java)
    val carts: CartEndpoint = jsonApi(config.baseUrl).create(CartEndpoint::class.java)
    val categories: CategoryEndpoint = jsonApi(config.baseUrl).create(CategoryEndpoint::class.java)
    val deliveries: DeliveryEndpoint = jsonApi(config.baseUrl).create(DeliveryEndpoint::class.java)
    val feedbacks: FeedbackEndpoint = jsonApi(config.baseUrl).create(FeedbackEndpoint::class.java)
    val hello: HelloEndpoint = jsonApi(config.baseUrl).create(HelloEndpoint::class.java)
    val homes: HomeEndpoint = jsonApi(config.baseUrl).create(HomeEndpoint::class.java)
    val ingredients: IngredientEndpoint = jsonApi(config.baseUrl).create(IngredientEndpoint::class.java)
    val items: ItemEndpoint = jsonApi(config.baseUrl).create(ItemEndpoint::class.java)
    val notifications: NotificationEndpoint = jsonApi(config.baseUrl).create(NotificationEndpoint::class.java)
    val orders: OrderEndpoint = jsonApi(config.baseUrl).create(OrderEndpoint::class.java)
    val payments: PaymentEndpoint = jsonApi(config.baseUrl).create(PaymentEndpoint::class.java)
    val products: ProductEndpoint = jsonApi(config.baseUrl).create(ProductEndpoint::class.java)
    val productSections: ProductSectionEndpoint = jsonApi(config.baseUrl).create(ProductSectionEndpoint::class.java)
    val restaurants: RestaurantEndpoint = jsonApi(config.baseUrl).create(RestaurantEndpoint::class.java)
    val restaurantSections: RestaurantSectionEndpoint = jsonApi(config.baseUrl).create(RestaurantSectionEndpoint::class.java)
    val restrictions: RestrictionEndpoint = jsonApi(config.baseUrl).create(RestrictionEndpoint::class.java)
    val users: UserEndpoint = jsonApi(config.baseUrl).create(UserEndpoint::class.java)

    companion object {
        fun jsonApi(baseUrl: String): Retrofit {
            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(authorization)
                .build()
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}

val api = Api()
