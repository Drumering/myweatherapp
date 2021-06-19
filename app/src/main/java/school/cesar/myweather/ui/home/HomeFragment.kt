package school.cesar.myweather.ui.home

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import school.cesar.myweather.connector.RequestManager
import school.cesar.myweather.connector.RetrofitInitializer
import school.cesar.myweather.databinding.FragmentHomeBinding
import school.cesar.myweather.models.City
import school.cesar.myweather.models.Weather
import school.cesar.myweather.utils.Utils

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

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
                    }
                }
            }
        }

        binding.recyclerViewAllWeathers.layoutManager = LinearLayoutManager(context)

        return root
    }

    private fun showWeather(weather: Weather) {
        binding.recyclerViewAllWeathers.adapter = WeatherRecyclerViewAdapter(weather.list as MutableList<City>)
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