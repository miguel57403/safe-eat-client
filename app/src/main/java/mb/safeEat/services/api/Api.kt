package mb.safeEat.services.api

import mb.safeEat.services.api.endpoints.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Api {
    val auth: AuthEndpoint = jsonApi("${config.baseUrl}/auth/").create(AuthEndpoint::class.java)
    val address: AddressEndpoint = jsonApi("${config.baseUrl}/address/").create(AddressEndpoint::class.java)
    val advertisement: AdvertisementEndpoint = jsonApi("${config.baseUrl}/advertisement/").create(AdvertisementEndpoint::class.java)
    val cart: CartEndpoint = jsonApi("${config.baseUrl}/cart/").create(CartEndpoint::class.java)
    val category: CategoryEndpoint = jsonApi("${config.baseUrl}/category/").create(CategoryEndpoint::class.java)
    val delivery: DeliveryEndpoint = jsonApi("${config.baseUrl}/delivery/").create(DeliveryEndpoint::class.java)
    val feedback: FeedbackEndpoint = jsonApi("${config.baseUrl}/feedback/").create(FeedbackEndpoint::class.java)
    val home: HomeEndpoint = jsonApi("${config.baseUrl}/home/").create(HomeEndpoint::class.java)
    val ingredient: IngredientEndpoint = jsonApi("${config.baseUrl}/ingredient/").create(IngredientEndpoint::class.java)
    val item: ItemEndpoint = jsonApi("${config.baseUrl}/item/").create(ItemEndpoint::class.java)
    val login: LoginEndpoint = jsonApi("${config.baseUrl}/login/").create(LoginEndpoint::class.java)
    val notification: NotificationEndpoint = jsonApi("${config.baseUrl}/notification/").create(NotificationEndpoint::class.java)
    val order: OrderEndpoint = jsonApi("${config.baseUrl}/order/").create(OrderEndpoint::class.java)
    val payment: PaymentEndpoint = jsonApi("${config.baseUrl}/payment/").create(PaymentEndpoint::class.java)
    val product: ProductEndpoint = jsonApi("${config.baseUrl}/product/").create(ProductEndpoint::class.java)
    val productSection: ProductSectionEndpoint = jsonApi("${config.baseUrl}/productSection/").create(ProductSectionEndpoint::class.java)
    val restaurant: RestaurantEndpoint = jsonApi("${config.baseUrl}/restaurant/").create(RestaurantEndpoint::class.java)
    val restaurantSection: RestaurantSectionEndpoint = jsonApi("${config.baseUrl}/restaurantSection/").create(RestaurantSectionEndpoint::class.java)
    val restriction: RestrictionEndpoint = jsonApi("${config.baseUrl}/restriction/").create(RestrictionEndpoint::class.java)
    val user: UserEndpoint = jsonApi("${config.baseUrl}/user/").create(UserEndpoint::class.java)

    companion object {
        fun jsonApi(baseUrl: String): Retrofit {
            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.SECONDS)
                .connectTimeout(1, TimeUnit.SECONDS)
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
