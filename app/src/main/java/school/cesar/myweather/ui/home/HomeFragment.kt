package school.cesar.myweather.ui.home

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
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
import school.cesar.myweather.connector.DatabaseConnector
import school.cesar.myweather.connector.RequestManager
import school.cesar.myweather.databinding.FragmentHomeBinding
import school.cesar.myweather.models.City
import school.cesar.myweather.models.Weather
import school.cesar.myweather.utils.Utils
import kotlin.math.log

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val weatherDao by lazy {
        context?.let { DatabaseConnector.getInstance(it).weatherDao }
    }

    private lateinit var adapter: WeatherRecyclerViewAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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
                Utils.hideKeyboard(context, binding.searchBarHomeInputText)

                if (isInternetAvailable(requireContext())){
                    if (!binding.searchBarHomeInputText.text.isNullOrEmpty()) {
                        RequestManager.getWeather(binding.searchBarHomeInputText.text.toString(), this::showWeather)
                    } else {
                        Toast.makeText(context, "Insert a search condition", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.recyclerViewAllWeathers.layoutManager = LinearLayoutManager(context)

        getFavorites()

        return root
    }

    private fun showWeather(weather: Weather) {
        adapter = WeatherRecyclerViewAdapter(weather.list as MutableList<City>)
        binding.recyclerViewAllWeathers.adapter = adapter

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                weatherDao?.insert(weather.list[0])
            }
        }
    }

    private fun getFavorites() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                weatherDao?.getAll()?.let {
                    adapter = WeatherRecyclerViewAdapter(it.toMutableList())
                    withContext(Dispatchers.Main) {
                        binding.recyclerViewAllWeathers.adapter = adapter
                    }
                }
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