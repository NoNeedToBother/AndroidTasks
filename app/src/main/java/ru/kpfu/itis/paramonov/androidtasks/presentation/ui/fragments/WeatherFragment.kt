package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.fragments

import android.content.Context
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentWeatherBinding
import ru.kpfu.itis.paramonov.androidtasks.presentation.base.BaseFragment
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.weather.WeatherUiModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.adapter.CityWeatherAdapter
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.viewmodel.WeatherViewModel
import ru.kpfu.itis.paramonov.androidtasks.utils.SpacingItemDecorator
import ru.kpfu.itis.paramonov.androidtasks.utils.ResourceManager
import ru.kpfu.itis.paramonov.androidtasks.utils.appComponent
import ru.kpfu.itis.paramonov.androidtasks.utils.gone
import ru.kpfu.itis.paramonov.androidtasks.utils.lazyViewModel
import ru.kpfu.itis.paramonov.androidtasks.utils.show
import javax.inject.Inject

class WeatherFragment : BaseFragment(R.layout.fragment_weather) {

    @Inject
    lateinit var resourceManager: ResourceManager

    private val viewModel: WeatherViewModel by lazyViewModel {
        val cities = resources.getStringArray(R.array.cities).asList()
        requireContext().appComponent.weatherViewModelFactory().create(cities)
    }

    private val binding: FragmentWeatherBinding by viewBinding(FragmentWeatherBinding::bind)

    private var adapter: CityWeatherAdapter? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireContext().appComponent.inject(this)
    }

    override fun init() {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val adapter = CityWeatherAdapter(
            onCityClicked = ::onCityClicked,
            resourceManager = resourceManager
        )
        this.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())

        with(binding.rvCities) {
            this.adapter = adapter
            this.layoutManager = layoutManager
            addItemDecoration(SpacingItemDecorator(24, SpacingItemDecorator.Side.BOTTOM))
        }
    }

    private fun onCityClicked(weatherUiModel: WeatherUiModel) {
        weatherUiModel.let {model ->
            parentFragmentManager.commit {
                replace(
                    R.id.main_activity_container,
                    CityWeatherFragment.newInstance(
                        icon = model.weatherData.icon,
                        city = model.city,
                        longitude = model.coordinates.longitude,
                        latitude = model.coordinates.latitude
                    ), CityWeatherFragment.CITY_WEATHER_FRAGMENT_TAG
                )
                addToBackStack(null)
            }
        }
    }

    override fun observeData() {
        viewModel.startCollectingWeatherInfo()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                launch {
                    collectWeatherData()
                }
                launch {
                    collectLoadingData()
                }
            }
        }
    }

    private suspend fun collectWeatherData() {
        viewModel.currentWeatherFlow.collect { weatherDataResult ->
            weatherDataResult?.let {
                when (it) {
                    is WeatherViewModel.WeatherDataResult.Success -> {
                        val result = it.getValue()
                        adapter?.submitList(result)
                    }
                    is WeatherViewModel.WeatherDataResult.Failure ->
                        showError(it.getException())
                }
            }
        }
    }

    private suspend fun collectLoadingData() {
        viewModel.loadingFlow.collect {loading ->
            with(binding) {
                if (loading) pbLoading.show()
                else pbLoading.gone()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopCollectingWeatherInfo()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopCollectingWeatherInfo()
    }
}