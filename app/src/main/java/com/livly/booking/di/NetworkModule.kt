@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.example.com") // Replace with actual base URL
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build())
            .build()
    }

    @Provides
    @Singleton
    fun provideAmenityBookingService(retrofit: Retrofit): AmenityBookingService {
        return retrofit.create(AmenityBookingService::class.java)
    }
}
