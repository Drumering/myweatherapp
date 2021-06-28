package school.cesar.myweather.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import school.cesar.myweather.Constants
import school.cesar.myweather.R
import school.cesar.myweather.connector.DatabaseConnector
import school.cesar.myweather.databinding.LayoutWeatherBinding
import school.cesar.myweather.models.City
import school.cesar.myweather.models.FavoriteCity

class WeatherRecyclerViewAdapter(private val onItemClickListener: (Long) -> Unit) : RecyclerView.Adapter<WeatherRecyclerViewAdapter.ViewHolder>() {

    lateinit var cities: MutableList<City>
    private lateinit var context: Context
    private val weatherDao by lazy {
        context.let { DatabaseConnector.getInstance(it).weatherDao }
    }

    class ViewHolder(view: LayoutWeatherBinding) : RecyclerView.ViewHolder(view.root) {
        val cityName = view.tvCityName
        val cityTemp = view.tvCityTemp
        val cityWeather = view.imgWeather
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        this.context = parent.context

        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_weather, parent, false)
        val binding = LayoutWeatherBinding.bind(view)
        val viewHolder = ViewHolder(binding)

        viewHolder.itemView.setOnClickListener {
            onItemClickListener(viewHolder.adapterPosition.toLong())
        }

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
