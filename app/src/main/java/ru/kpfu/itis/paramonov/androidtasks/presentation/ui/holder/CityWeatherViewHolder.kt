package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.holder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemCityWeatherBinding
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.WeatherUiModel

class CityWeatherViewHolder(
    private val context: Context,
    private val binding: ItemCityWeatherBinding,
    private val onCityClicked: (WeatherUiModel) -> Unit,
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

        with(binding) {
            Glide.with(context)
                .load(item.weatherData.icon)
                .error(R.drawable.placeholder_img)
                .into(ivIcon)

            tvCity.text = item.city
            tvDesc.text = item.weatherData.description
            tvTemperature.text = context.getString(R.string.temperature, item.temperatureData.temp)
        }
    }

    fun updateIcon(icon: String) {
        Glide.with(context)
            .load(icon)
            .error(R.drawable.placeholder_img)
            .into(binding.ivIcon)
    }

    fun updateCity(city: String) {
        binding.tvCity.text = city
    }

    fun updateDescription(description: String) {
        binding.tvDesc.text = description
    }

    fun updateTemperature(temperature: Double) {
        binding.tvTemperature.text = context.getString(R.string.temperature, temperature)
    }
}