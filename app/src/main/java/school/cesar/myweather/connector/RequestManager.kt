package school.cesar.myweather.connector

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import school.cesar.myweather.models.City
import school.cesar.myweather.models.CurrentWeather

class RequestManager {
    companion object {

        fun getWeatherByName(city: String, callback: (CurrentWeather) -> Unit) {
            val call = RetrofitInitializer().getService().getWeatherByName(city)

            call.enqueue(object: Callback<CurrentWeather> {
                override fun onResponse(call: Call<CurrentWeather>, response: Response<CurrentWeather>) {
                    response.body()?.let {
                        callback(it)
                    }
                }

                override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }

        fun getWeatherById(id: Long, callback: (City) -> Unit) {
            val call = RetrofitInitializer().getService().getWeatherById(id)

            call.enqueue(object: Callback<City>{
                override fun onResponse(call: Call<City>, response: Response<City>) {
                    response.body()?.let {
                        callback(it)
                    }
                }

                override fun onFailure(call: Call<City>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}