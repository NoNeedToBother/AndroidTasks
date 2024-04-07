package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.holder

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.kpfu.itis.paramonov.androidtasks.BuildConfig
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemForecastBinding
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.forecast.ForecastListWeatherUiModel
import ru.kpfu.itis.paramonov.androidtasks.utils.ResourceManager
import java.util.Calendar

class ForecastViewHolder(
    private val context: Context,
    private val binding: ItemForecastBinding,
    private val resourceManager: ResourceManager
): RecyclerView.ViewHolder(binding.root) {

    fun onBind(item: ForecastListWeatherUiModel) {
        with (binding) {
            loadWeatherIcon(item.weatherData.icon)
            tvDesc.text = item.weatherData.description
            tvTemperature.text = resourceManager.getString(R.string.temperature, item.temperature)
            tvDate.text = resourceManager.getString(R.string.date,
                item.time.get(Calendar.DAY_OF_MONTH), item.time.get(Calendar.MONTH) + 1)
            tvTime.text = resourceManager.getString(R.string.time,
                item.time.get(Calendar.HOUR_OF_DAY), item.time.get(Calendar.MINUTE))
        }
    }

    private fun loadWeatherIcon(icon: String) {
        val url = String.format(BuildConfig.OPEN_WEATHER_ICON_URL, icon)
        Glide.with(context)
            .load(url)
            .error(R.drawable.placeholder_img)
            .into(binding.ivIcon)
    }
}