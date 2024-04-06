package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.WeatherUiModel
import ru.kpfu.itis.paramonov.androidtasks.utils.Params

class CityWeatherDiffUtilCallback : DiffUtil.ItemCallback<WeatherUiModel>() {

    override fun areItemsTheSame(oldItem: WeatherUiModel, newItem: WeatherUiModel): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: WeatherUiModel, newItem: WeatherUiModel): Boolean {
        return oldItem.let {o ->
            newItem.let {n ->
                val icon = o.weatherData.icon == n.weatherData.icon
                val main = o.weatherData.main == n.weatherData.main
                val description = o.weatherData.description == n.weatherData.description
                val temperature = o.temperatureData.temp == n.temperatureData.temp
                icon && main && description && temperature
            }
        }
    }

    override fun getChangePayload(oldItem: WeatherUiModel, newItem: WeatherUiModel): Any {
        return oldItem.let {o ->
            newItem.let { n ->
                val changes: MutableMap<String, Any> = mutableMapOf()
                addChangeIfNecessary(o.weatherData.icon, n.weatherData.icon,
                    Params.PAYLOAD_ICON_KEY, changes)
                addChangeIfNecessary(o.weatherData.main, n.weatherData.main,
                    Params.PAYLOAD_MAIN_KEY, changes)
                addChangeIfNecessary(o.weatherData.description, n.weatherData.description,
                    Params.PAYLOAD_DESC_KEY, changes)
                addChangeIfNecessary(o.temperatureData.temp, n.temperatureData.temp,
                    Params.PAYLOAD_TEMP_KEY, changes)
                changes
            }
        }
    }

    private fun <T : Any> addChangeIfNecessary(valueOld: T, valueNew: T, key: String, map: MutableMap<String, Any>) {
        if (!valueOld.equals(valueNew)) map[key] = valueNew
    }
}