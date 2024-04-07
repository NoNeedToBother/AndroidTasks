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

    /*
    override fun onBindViewHolder(
        holder: CityWeatherViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val changes = payloads.first() as Map<String, Any>
            for (changeKey in changes.keys) {
                when (changeKey) {
                    Key.PAYLOAD_TEMP_KEY -> holder.updateTemperature(changes[changeKey] as Double)
                    Key.PAYLOAD_DESC_KEY -> holder.updateDescription(changes[changeKey] as String)
                    Key.PAYLOAD_MAIN_KEY -> holder.updateMain(changes[changeKey] as String)
                    Key.PAYLOAD_ICON_KEY -> holder.updateIcon(changes[changeKey] as String)
                }
            }
        }
    }*/
}