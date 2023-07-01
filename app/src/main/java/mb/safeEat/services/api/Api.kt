package mb.safeEat.services.api

import mb.safeEat.services.api.endpoints.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Api {
    val auth: AuthEndpoint = jsonApi("${config.baseUrl}/").create(AuthEndpoint::class.java)
    val addresses: AddressEndpoint = jsonApi("${config.baseUrl}/addresses/").create(AddressEndpoint::class.java)
    val advertisements: AdvertisementEndpoint = jsonApi("${config.baseUrl}/advertisements/").create(AdvertisementEndpoint::class.java)
    val carts: CartEndpoint = jsonApi("${config.baseUrl}/carts/").create(CartEndpoint::class.java)
    val categories: CategoryEndpoint = jsonApi("${config.baseUrl}/categories/").create(CategoryEndpoint::class.java)
    val deliveries: DeliveryEndpoint = jsonApi("${config.baseUrl}/deliveries/").create(DeliveryEndpoint::class.java)
    val feedbacks: FeedbackEndpoint = jsonApi("${config.baseUrl}/feedbacks/").create(FeedbackEndpoint::class.java)
    val hello: HelloEndpoint = jsonApi("${config.baseUrl}/").create(HelloEndpoint::class.java)
    val homes: HomeEndpoint = jsonApi("${config.baseUrl}/homes/").create(HomeEndpoint::class.java)
    val ingredients: IngredientEndpoint = jsonApi("${config.baseUrl}/ingredients/").create(IngredientEndpoint::class.java)
    val items: ItemEndpoint = jsonApi("${config.baseUrl}/items/").create(ItemEndpoint::class.java)
    val notifications: NotificationEndpoint = jsonApi("${config.baseUrl}/notifications/").create(NotificationEndpoint::class.java)
    val orders: OrderEndpoint = jsonApi("${config.baseUrl}/orders/").create(OrderEndpoint::class.java)
    val payments: PaymentEndpoint = jsonApi("${config.baseUrl}/payments/").create(PaymentEndpoint::class.java)
    val products: ProductEndpoint = jsonApi("${config.baseUrl}/products/").create(ProductEndpoint::class.java)
    val productSections: ProductSectionEndpoint = jsonApi("${config.baseUrl}/productSections/").create(ProductSectionEndpoint::class.java)
    val restaurants: RestaurantEndpoint = jsonApi("${config.baseUrl}/restaurants/").create(RestaurantEndpoint::class.java)
    val restaurantSections: RestaurantSectionEndpoint = jsonApi("${config.baseUrl}/restaurantSections/").create(RestaurantSectionEndpoint::class.java)
    val restrictions: RestrictionEndpoint = jsonApi("${config.baseUrl}/restrictions/").create(RestrictionEndpoint::class.java)
    val users: UserEndpoint = jsonApi("${config.baseUrl}/users/").create(UserEndpoint::class.java)

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
