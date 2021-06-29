package school.cesar.myweather.ui.favorites

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import school.cesar.myweather.R
import school.cesar.myweather.connector.DatabaseConnector
import school.cesar.myweather.connector.RequestManager
import school.cesar.myweather.databinding.FragmentFavoriteBinding
import school.cesar.myweather.models.City
import school.cesar.myweather.models.FavoriteCity
import school.cesar.myweather.ui.home.WeatherRecyclerViewAdapter
import kotlin.properties.Delegates

class FavoriteFragment : Fragment() {

    private lateinit var dashboardViewModel: FavoriteViewModel
    private var _binding: FragmentFavoriteBinding? = null

    private val weatherRecyclerViewAdapter by lazy {
        WeatherRecyclerViewAdapter(this::removeFavorite)
    }

    private val weatherDao by lazy {
        context?.let { DatabaseConnector.getInstance(it).weatherDao }
    }

    private val sharedPreferences by lazy {
        context?.getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE)
    }

    private var favoriteCitiesIds: MutableList<FavoriteCity> by Delegates.observable(mutableListOf(), onChange = { _, _, newValue ->
        newValue.forEach {
            if (unit) {
                RequestManager.getWeatherById(it.id, "metric", this::showWeather)
            } else {
                RequestManager.getWeatherById(it.id, "imperial", this::showWeather)
            }
        }
    })

    private var unit = false
    private var lang = false

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(FavoriteViewModel::class.java)

        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recyclerViewFavoriteWeathers.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewFavoriteWeathers.adapter = weatherRecyclerViewAdapter

        sharedPreferences?.let {
            unit = it.getBoolean("temp_C", false)
        }

        getFavorites()

        return root
    }

    private fun removeFavorite(position: Int) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                weatherDao?.delete(favoriteCitiesIds[position])
                weatherRecyclerViewAdapter.cities.removeAt(position)
                withContext(Dispatchers.Main) {
                    weatherRecyclerViewAdapter.notifyItemRemoved(position)
                    if (weatherRecyclerViewAdapter.cities.isEmpty()) {
                        binding.tvAlertInsert.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun showWeather(city: City) {
        binding.progressCircular.visibility = View.GONE
        binding.tvAlertInsert.visibility = View.GONE
        weatherRecyclerViewAdapter.cities.add(city)
        weatherRecyclerViewAdapter.notifyDataSetChanged()
    }

    private fun getFavorites() {

        if (isInternetAvailable(requireContext())){
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    weatherDao?.getAll()?.apply {
                        if (!isEmpty()) {
                            favoriteCitiesIds = toMutableList()
                        } else {
                            withContext(Dispatchers.Main) {
                                binding.progressCircular.visibility = View.GONE
                                binding.tvAlertInsert.visibility = View.VISIBLE
                            }
                        }
                    }
                }
            }
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isInternetAvailable(context: Context): Boolean {
        var result = false

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

        cm.getNetworkCapabilities(cm.activeNetwork)?.run {
            result = when {
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }

        return result
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}