package school.cesar.myweather.connector

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import school.cesar.myweather.models.Weather

class RequestManager {
    companion object {

        fun getWeather(city: String, callback: (Weather) -> Unit) {
            val call = RetrofitInitializer().getService().getWeather(city)

            call.enqueue(object: Callback<Weather> {
                override fun onResponse(call: Call<Weather>, response: Response<Weather>) {
                    response.body()?.let {
                        callback(it)
                    }
                }

                override fun onFailure(call: Call<Weather>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}