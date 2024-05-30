package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.adapter.diffutil

import androidx.recyclerview.widget.DiffUtil
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.weather.WeatherUiModel

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
                val payload = WeatherModelPayload()
                if (o.city != n.city) payload.city = n.city
                if (o.weatherData.description != n.weatherData.description)
                    payload.description = n.weatherData.description
                if (o.temperature != n.temperature) payload.temperature = n.temperature
                payload
            }
        }
    }

    class WeatherModelPayload {
        var description: String? = null
        var temperature: Double? = null
        var city: String? = null
    }
}