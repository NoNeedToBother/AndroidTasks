package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.holder

import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemCityWeatherBinding
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.weather.WeatherUiModel
import ru.kpfu.itis.paramonov.androidtasks.utils.ResourceManager

class CityWeatherViewHolder(
    private val binding: ItemCityWeatherBinding,
    private val onCityClicked: (WeatherUiModel) -> Unit,
    private val resourceManager: ResourceManager
): RecyclerView.ViewHolder(binding.root) {

    private var item: WeatherUiModel? = null

    init {
        with (binding) {
            root.setOnClickListener {
                item?.let { onCityClicked(it) }
            }
        }
    }

    fun onBind(item: WeatherUiModel) {
        this.item = item

        updateCity(item.city)
        updateDescription(item.weatherData.description)
        updateTemperature(item.temperature)
    }

    fun updateCity(city: String) {
        binding.tvCity.text = city
    }

    fun updateDescription(description: String) {
        binding.tvDesc.text = description
    }

    fun updateTemperature(temperature: Double) {
        binding.tvTemperature.text = resourceManager.getString(R.string.temperature, temperature)
    }
}