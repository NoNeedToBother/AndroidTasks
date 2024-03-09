package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.androidtasks.BuildConfig
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentWeatherBinding
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
                launch {
                    checkErrors()
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
                        loadImage(weatherData.icon)
                    }
                }
            }
        }
    }

    private fun loadImage(icon: String) {
        val url = String.format(BuildConfig.OPEN_WEATHER_ICON_URL, icon)
        Glide.with(requireContext())
            .load(url)
            .error(R.drawable.placeholder_img)
            .into(binding.ivIcon)
    }

    private suspend fun collectLoadingData() {
        viewModel.loadingFlow.collect {loading ->
            with(binding) {
                if (loading) pbLoading.visibility = View.VISIBLE
                else pbLoading.visibility = View.GONE
            }
        }
    }

    private suspend fun checkErrors() {
        viewModel.errorsChannel.consumeEach {error ->
            val errorMessage = error.message ?: getString(R.string.default_exception)
            Toast.makeText(
                requireContext(),
                errorMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}