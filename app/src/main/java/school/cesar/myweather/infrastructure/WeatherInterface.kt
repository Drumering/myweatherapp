package school.cesar.myweather.infrastructure

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import school.cesar.myweather.Constants
import school.cesar.myweather.models.Weather

interface WeatherInterface {

    @GET("find?APPID=${Constants.APPID}")
    fun getWeather(@Query("q") city: String,
                   @Query("units") unit: String = "metric") : Call<Weather>
}