package school.cesar.myweather.ui.favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import school.cesar.myweather.connector.DatabaseConnector
import school.cesar.myweather.databinding.FragmentFavoriteBinding
import school.cesar.myweather.models.FavoriteCity

class FavoriteFragment : Fragment() {

    private lateinit var dashboardViewModel: FavoriteViewModel
    private var _binding: FragmentFavoriteBinding? = null

    private val favoriteCityRecyclerViewAdapter by lazy {
//        FavoriteCityRecyclerViewAdapter(this::removeFavorite)
    }
    private val weatherDao by lazy {
        context?.let { DatabaseConnector.getInstance(it).weatherDao }
    }

    private lateinit var favoriteCitiesIds: List<FavoriteCity>

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

        getFavorites()

        return root
    }

    private fun removeFavorite(id: Long) {
        //TO DO
    }

    private fun getFavorites() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                weatherDao?.getAll()?.let {
                    favoriteCitiesIds = it
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}