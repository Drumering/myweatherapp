package school.cesar.myweather.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import school.cesar.myweather.Constants
import school.cesar.myweather.R
import school.cesar.myweather.databinding.LayoutWeatherBinding
import school.cesar.myweather.models.City

class WeatherRecyclerViewAdapter() : RecyclerView.Adapter<WeatherRecyclerViewAdapter.ViewHolder>() {

    public lateinit var cities: MutableList<City>

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
        holder.cityTemp.text = city.main.temp.toString()
        Glide.with(holder.itemView).load(Constants.BASE_ICON_URL.replace("icon", city.icons[0].icon)).centerCrop().into(holder.cityWeather)
    }

    override fun getItemCount(): Int = cities.size
}
