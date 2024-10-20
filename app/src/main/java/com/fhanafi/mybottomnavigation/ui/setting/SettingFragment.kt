package com.fhanafi.mybottomnavigation.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.fhanafi.mybottomnavigation.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    // Initialize the ViewModel
    private val settingViewModel: SettingViewModel by viewModels {
        SettingsViewModelFactory(SettingPreferences.getInstance(requireContext().dataStore))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Access the Switch using View Binding
        val switchTheme = binding.switchTheme

        // Observe the theme setting and set the initial state of the switch
        settingViewModel.getThemeSettings().observe(viewLifecycleOwner, Observer { isDarkModeActive ->
            switchTheme.isChecked = isDarkModeActive
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
        })

        // Set up the listener for the switch to save the theme setting
        switchTheme.setOnCheckedChangeListener { _, isChecked ->
            settingViewModel.saveThemeSetting(isChecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
