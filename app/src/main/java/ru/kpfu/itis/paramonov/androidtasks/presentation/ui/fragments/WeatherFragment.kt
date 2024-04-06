package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.androidtasks.BuildConfig
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentWeatherBinding
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.viewmodel.WeatherViewModel
import ru.kpfu.itis.paramonov.androidtasks.utils.appComponent
import javax.inject.Inject

class WeatherFragment : Fragment(R.layout.fragment_weather) {
    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val viewModel: WeatherViewModel by viewModels { factory }

    private val binding: FragmentWeatherBinding by viewBinding(FragmentWeatherBinding::bind)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireContext().appComponent.inject(this)
    }

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
        viewModel.currentWeatherFlow.collect { weatherDataResult ->
            with(binding) {
                weatherDataResult?.let {
                    when (it) {
                        is WeatherViewModel.WeatherDataResult.Success -> {
                            val result = it.getValue()[0]
                            llWeatherInfo.visibility = View.VISIBLE
                            tvMain.text = result.weatherData.main
                            tvDesc.text = result.weatherData.description
                            tvTemperature.text = getString(R.string.temperature, result.temperatureData.temp)
                            loadImage(result.weatherData.icon)
                        }
                        is WeatherViewModel.WeatherDataResult.Failure ->
                            showError(it.getException())
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

    private fun showError(error: Throwable) {
        val errorMessage = error.message ?: getString(R.string.default_exception)
        Toast.makeText(
            requireContext(),
            errorMessage,
            Toast.LENGTH_SHORT
        ).show()
    }
}