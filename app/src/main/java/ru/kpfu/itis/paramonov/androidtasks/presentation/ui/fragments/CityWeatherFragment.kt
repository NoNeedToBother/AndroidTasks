package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.fragments

import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.androidtasks.BuildConfig
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentWeatherCityBinding
import ru.kpfu.itis.paramonov.androidtasks.presentation.base.BaseFragment
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.weather.WeatherUiModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.viewmodel.CityWeatherViewModel
import ru.kpfu.itis.paramonov.androidtasks.utils.Params
import ru.kpfu.itis.paramonov.androidtasks.utils.appComponent
import ru.kpfu.itis.paramonov.androidtasks.utils.gone
import ru.kpfu.itis.paramonov.androidtasks.utils.lazyViewModel
import ru.kpfu.itis.paramonov.androidtasks.utils.show

class CityWeatherFragment: BaseFragment(R.layout.fragment_weather_city) {

    private val binding: FragmentWeatherCityBinding by viewBinding(FragmentWeatherCityBinding::bind)

    private val viewModel: CityWeatherViewModel by lazyViewModel {
        val city = requireArguments().getString(CITY_KEY, Params.CITY_EMPTY_DATA)
        val longitude = requireArguments().getDouble(LONGITUDE_KEY)
        val latitude = requireArguments().getDouble(LATITUDE_KEY)
        requireContext().appComponent.cityWeatherViewModelFactory().create(
            longitude, latitude, city
        )
    }

    override fun initView() {}

    override fun observeData() {
        viewModel.getWeatherInfo()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                launch {
                    collectWeatherData()
                }
                launch {
                    collectWeatherLoadingData()
                }
            }
        }
    }

    private suspend fun collectWeatherLoadingData() {
        viewModel.weatherLoadingFlow.collect {loading ->
            with(binding) {
                if (loading) pbWeatherLoading.show()
                else pbWeatherLoading.gone()
            }
        }
    }

    private suspend fun collectWeatherData() {
        viewModel.currentWeatherFlow.collect { result ->
            result?.let {
                when (it) {
                    is CityWeatherViewModel.WeatherDataResult.Success -> {
                        val data = it.getValue()
                        showWeather(data)
                    }
                    is CityWeatherViewModel.WeatherDataResult.Failure ->
                        showError(it.getException())
                }
            }
        }
    }

    private fun showWeather(weatherUiModel: WeatherUiModel) {
        with(weatherUiModel) {
            with(binding) {
                tvCity.text = city
                llWeatherInfo.show()
                tvMain.text = weatherData.main
                tvDesc.text = weatherData.description
                tvTemperature.text = getString(R.string.temperature, temperature)
                loadWeatherIcon(weatherData.icon)
            }
        }
    }

    private fun loadWeatherIcon(icon: String) {
        val url = String.format(BuildConfig.OPEN_WEATHER_ICON_URL, icon)
        Glide.with(requireContext())
            .load(url)
            .error(R.drawable.placeholder_img)
            .into(binding.ivIcon)
    }


    companion object {
        const val CITY_WEATHER_FRAGMENT_TAG = "CITY_WEATHER_FRAGMENT"

        private const val ICON_KEY = "icon"
        private const val CITY_KEY = "key"
        private const val LONGITUDE_KEY = "longitude"
        private const val LATITUDE_KEY = "latitude"

        fun newInstance(icon: String, city: String, longitude: Double, latitude: Double) =
            CityWeatherFragment().apply {
            arguments = bundleOf(
                ICON_KEY to icon,
                CITY_KEY to city,
                LONGITUDE_KEY to longitude,
                LATITUDE_KEY to latitude
            )
        }
    }
}