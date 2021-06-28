package school.cesar.myweather.infrastructure

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import school.cesar.myweather.Constants
import school.cesar.myweather.models.CurrentWeather

interface WeatherInterface {

    @GET("find")
    fun getWeatherByName(@Query("q") city: String,
                         @Query("units") unit: String = "metric",
                         @Query("appid") appid:String = Constants.APPID) : Call<CurrentWeather>

    @GET("weather")
    fun getWeatherById(@Query("id") id: Long,
                       @Query("Units") unit: String = "metric",
                       @Query("appid") appid: String = Constants.APPID) : Call<CurrentWeather>
}