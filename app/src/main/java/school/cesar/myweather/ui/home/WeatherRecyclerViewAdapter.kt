package school.cesar.myweather.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import school.cesar.myweather.R
import school.cesar.myweather.databinding.LayoutWeatherBinding
import school.cesar.myweather.models.City

class WeatherRecyclerViewAdapter(private val cities: MutableList<City>) : RecyclerView.Adapter<WeatherRecyclerViewAdapter.ViewHolder>() {
    class ViewHolder(view: LayoutWeatherBinding) : RecyclerView.ViewHolder(view.root) {
        val cityName = view.tvCityName
        val cityTemp = view.tvCityTemp
        val cityWeather = view.imgWeather
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_weather, parent, false)
        val binding = LayoutWeatherBinding.bind(view)
        val viewHolder = ViewHolder(binding)

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = cities[position]
        holder.cityName.text = city.name
        holder.cityName.text = city.main.temp.toString()
    }

    override fun getItemCount(): Int = cities.size
}