package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentWeatherBinding
import ru.kpfu.itis.paramonov.androidtasks.di.ServiceLocator

class WeatherFragment : Fragment(R.layout.fragment_weather) {
    private val binding: FragmentWeatherBinding by viewBinding(FragmentWeatherBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnGetWeather.setOnClickListener {
                lifecycleScope.launch {
                    ServiceLocator.getWeatherUseCase.invoke(etCity.text.toString())
                }
            }
        }
    }
}