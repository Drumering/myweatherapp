package school.cesar.myweather.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import school.cesar.myweather.R
import school.cesar.myweather.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private lateinit var notificationsViewModel: SettingsViewModel
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val sharedPreferences = context?.getSharedPreferences(getString(R.string.pref_file), Context.MODE_PRIVATE)

        sharedPreferences?.let {
            val langPT = it.getBoolean("lang_PT", false)
            val tempC = it.getBoolean("temp_C", false)

            if (langPT) {
                binding.rbPtBr.isChecked = true
            } else {
                binding.rbEnUs.isChecked = true
            }

            if (tempC) {
                binding.rbCelsius.isChecked= true
            } else {
                binding.rbFarenheit.isChecked = true
            }
        }

        binding.btnSave.setOnClickListener {
            with(sharedPreferences?.edit()) {
                this?.putBoolean("lang_PT", binding.rbPtBr.isChecked)
                this?.putBoolean("temp_C", binding.rbCelsius.isChecked)
                this?.apply()
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}