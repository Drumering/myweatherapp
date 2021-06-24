package school.cesar.myweather.connector

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import school.cesar.myweather.models.CurrentWeather

class RequestManager {
    companion object {

        fun getWeather(city: String, callback: (CurrentWeather) -> Unit) {
            val call = RetrofitInitializer().getService().getWeather(city)

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
    }
}