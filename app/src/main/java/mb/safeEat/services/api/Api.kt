package mb.safeEat.services.api

import mb.safeEat.services.api.endpoints.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Api {
    val auth: AuthEndpoint = jsonApi().create(AuthEndpoint::class.java)
    val addresses: AddressEndpoint = jsonApi().create(AddressEndpoint::class.java)
    val advertisements: AdvertisementEndpoint = jsonApi().create(AdvertisementEndpoint::class.java)
    val carts: CartEndpoint = jsonApi().create(CartEndpoint::class.java)
    val categories: CategoryEndpoint = jsonApi().create(CategoryEndpoint::class.java)
    val deliveries: DeliveryEndpoint = jsonApi().create(DeliveryEndpoint::class.java)
    val feedbacks: FeedbackEndpoint = jsonApi().create(FeedbackEndpoint::class.java)
    val hello: HelloEndpoint = jsonApi().create(HelloEndpoint::class.java)
    val homes: HomeEndpoint = jsonApi().create(HomeEndpoint::class.java)
    val ingredients: IngredientEndpoint = jsonApi().create(IngredientEndpoint::class.java)
    val items: ItemEndpoint = jsonApi().create(ItemEndpoint::class.java)
    val notifications: NotificationEndpoint = jsonApi().create(NotificationEndpoint::class.java)
    val orders: OrderEndpoint = jsonApi().create(OrderEndpoint::class.java)
    val payments: PaymentEndpoint = jsonApi().create(PaymentEndpoint::class.java)
    val products: ProductEndpoint = jsonApi().create(ProductEndpoint::class.java)
    val productSections: ProductSectionEndpoint = jsonApi().create(ProductSectionEndpoint::class.java)
    val restaurants: RestaurantEndpoint = jsonApi().create(RestaurantEndpoint::class.java)
    val restaurantSections: RestaurantSectionEndpoint = jsonApi().create(RestaurantSectionEndpoint::class.java)
    val restrictions: RestrictionEndpoint = jsonApi().create(RestrictionEndpoint::class.java)
    val users: UserEndpoint = jsonApi().create(UserEndpoint::class.java)

    companion object {
        fun jsonApi(): Retrofit {
            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)
                .addInterceptor(authorization)
                .build()
            return Retrofit.Builder()
                .baseUrl(config.baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}

val api = Api()
