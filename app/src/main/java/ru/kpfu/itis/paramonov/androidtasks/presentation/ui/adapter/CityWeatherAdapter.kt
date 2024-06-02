package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemCityWeatherBinding
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.weather.WeatherUiModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.adapter.diffutil.CityWeatherDiffUtilCallback
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.holder.CityWeatherViewHolder
import ru.kpfu.itis.paramonov.androidtasks.utils.ResourceManager

class CityWeatherAdapter(
    private val onCityClicked: (WeatherUiModel) -> Unit,
    private val resourceManager: ResourceManager
): ListAdapter<WeatherUiModel, CityWeatherViewHolder>(CityWeatherDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityWeatherViewHolder {
        return CityWeatherViewHolder(
            binding = ItemCityWeatherBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            onCityClicked = onCityClicked,
            resourceManager = resourceManager
        )
    }

    override fun onBindViewHolder(holder: CityWeatherViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: CityWeatherViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val changes = payloads.first() as CityWeatherDiffUtilCallback.WeatherModelPayload
            changes.description?.let { holder.updateDescription(it) }
            changes.city?.let { holder.updateCity(it) }
            changes.temperature?.let { holder.updateTemperature(it) }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }
}