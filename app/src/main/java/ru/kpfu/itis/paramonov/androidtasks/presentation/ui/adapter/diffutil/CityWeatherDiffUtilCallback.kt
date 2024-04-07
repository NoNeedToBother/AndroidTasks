package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.weather.WeatherUiModel
import ru.kpfu.itis.paramonov.androidtasks.utils.Keys

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
                val temperature = o.temperature == n.temperature
                val city = o.city == n.city
                icon && main && description && temperature && city
            }
        }
    }

    override fun getChangePayload(oldItem: WeatherUiModel, newItem: WeatherUiModel): Any {
        return oldItem.let {o ->
            newItem.let { n ->
                val changes: MutableMap<String, Any> = mutableMapOf()
                addChangeIfNecessary(o.weatherData.icon, n.weatherData.icon,
                    Keys.PAYLOAD_ICON_KEY, changes)
                addChangeIfNecessary(o.weatherData.main, n.weatherData.main,
                    Keys.PAYLOAD_MAIN_KEY, changes)
                addChangeIfNecessary(o.weatherData.description, n.weatherData.description,
                    Keys.PAYLOAD_DESC_KEY, changes)
                addChangeIfNecessary(o.temperature, n.temperature, Keys.PAYLOAD_TEMP_KEY, changes)
                addChangeIfNecessary(o.city, n.city, Keys.PAYLOAD_CITY_KEY, changes)
                changes
            }
        }
    }

    private fun <T : Any> addChangeIfNecessary(valueOld: T, valueNew: T, key: String, map: MutableMap<String, Any>) {
        if (valueOld != valueNew) map[key] = valueNew
    }
}