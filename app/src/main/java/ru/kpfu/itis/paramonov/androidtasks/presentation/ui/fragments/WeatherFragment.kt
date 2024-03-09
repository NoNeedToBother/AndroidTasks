package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentWeatherBinding
import ru.kpfu.itis.paramonov.androidtasks.di.ServiceLocator
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.viewmodel.WeatherViewModel

class WeatherFragment : Fragment(R.layout.fragment_weather) {
    private val binding: FragmentWeatherBinding by viewBinding(FragmentWeatherBinding::bind)

    private val viewModel: WeatherViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        with(binding) {
            btnGetWeather.setOnClickListener {
                llWeatherInfo.visibility = View.GONE
                val city = etCity.text.toString()
                viewModel.getWeatherInfo(city)
            }
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                launch {
                    collectWeatherData()
                }
                launch {
                    collectLoadingData()
                }
            }
        }
    }

    private suspend fun collectWeatherData() {
        viewModel.currentWeatherFlow.collect { uiModel ->
            with(binding) {
                if (uiModel != null) {
                    with(uiModel) {
                        llWeatherInfo.visibility = View.VISIBLE
                        tvMain.text = weatherData.main
                        tvDesc.text = weatherData.description
                        tvTemperature.text = temperatureData.temp.toString()
                    }
                }
            }
        }
    }

    private suspend fun collectLoadingData() {
        viewModel.loadingFlow.collect {loading ->
            with(binding) {
                if (loading) pbLoading.visibility = View.VISIBLE
                else pbLoading.visibility = View.GONE
            }
        }
    }
}