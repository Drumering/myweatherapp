package school.cesar.myweather.connector

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import school.cesar.myweather.infrastructure.WeatherInterface

class RetrofitInitializer {
    private val retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun getService() = retrofit.create(WeatherInterface::class.java)
}