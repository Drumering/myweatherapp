package school.cesar.myweather.ui.home

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
import school.cesar.myweather.databinding.FragmentHomeBinding
import school.cesar.myweather.models.CurrentWeather
import school.cesar.myweather.models.FavoriteCity
import school.cesar.myweather.utils.Utils

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val weatherDao by lazy {
        context?.let { DatabaseConnector.getInstance(it).weatherDao }
    }
    private val weatherRecyclerViewAdapter by lazy {
        WeatherRecyclerViewAdapter(this::setFavorite)
    }

    private val sharedPreferences by lazy {
        context?.getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE)
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var unit = false
    private var lang = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.searchBarHome.setEndIconOnClickListener {
            context?.let { context ->
                binding.searchBarHomeInputText.clearFocus()
                binding.tvAlertInsert.visibility = View.GONE
                Utils.hideKeyboard(context, binding.searchBarHomeInputText)

                if (isInternetAvailable(requireContext())){
                    if (!binding.searchBarHomeInputText.text.isNullOrEmpty()) {
                        binding.progressCircular.visibility = View.VISIBLE
                        if (unit) {
                            RequestManager.getWeatherByName(binding.searchBarHomeInputText.text.toString(),"metric" ,this::showWeather)
                        } else {
                            RequestManager.getWeatherByName(binding.searchBarHomeInputText.text.toString(),"imperial" ,this::showWeather)
                        }
                    } else {
                        binding.tvAlertInsert.visibility = View.VISIBLE
                        Toast.makeText(context, "Insert a search condition", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.recyclerViewAllWeathers.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewAllWeathers.adapter = weatherRecyclerViewAdapter

        sharedPreferences?.let {
            unit = it.getBoolean("temp_C", false)
        }

        return root
    }

    private fun showWeather(weather: CurrentWeather) {
        binding.progressCircular.visibility = View.GONE
        if (weather.list.isEmpty()) {
            binding.tvAlertInsert.visibility = View.VISIBLE
        } else {
            weatherRecyclerViewAdapter.cities = weather.list.toMutableList()
        }
        weatherRecyclerViewAdapter.notifyDataSetChanged()
    }

    private fun setFavorite(position: Int) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                weatherDao?.insert(FavoriteCity(weatherRecyclerViewAdapter.cities[position].id))
            }
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