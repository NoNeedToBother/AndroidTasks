package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.kpfu.itis.paramonov.androidtasks.databinding.ItemForecastBinding
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.forecast.ForecastListWeatherUiModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.holder.ForecastViewHolder
import ru.kpfu.itis.paramonov.androidtasks.utils.ResourceManager

class ForecastAdapter(
    private val context: Context,
    private val resourceManager: ResourceManager
): RecyclerView.Adapter<ForecastViewHolder>() {

    private val forecasts: MutableList<ForecastListWeatherUiModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        return ForecastViewHolder(
            context = context,
            binding = ItemForecastBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            resourceManager = resourceManager
        )
    }

    override fun getItemCount(): Int {
        return forecasts.size
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.onBind(forecasts[position])
    }

    fun setItems(forecasts: List<ForecastListWeatherUiModel>) {
        this.forecasts.addAll(forecasts)
    }
}