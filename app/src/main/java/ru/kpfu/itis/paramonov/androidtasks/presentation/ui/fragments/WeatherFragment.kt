package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentWeatherBinding
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.WeatherUiModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.adapter.CityWeatherAdapter
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.viewmodel.WeatherViewModel
import ru.kpfu.itis.paramonov.androidtasks.utils.appComponent
import ru.kpfu.itis.paramonov.androidtasks.utils.lazyViewModel
import javax.inject.Inject

class WeatherFragment : Fragment(R.layout.fragment_weather) {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        initRecyclerView()
        viewModel.startCollectingWeatherInfo()
        observeData()
    }

    private fun initRecyclerView() {
        val adapter = CityWeatherAdapter(
            context = requireContext(),
            onCityClicked = ::onCityClicked
        )
        this.adapter = adapter
        val layoutManager = LinearLayoutManager(requireContext())

        binding.rvCities.adapter = adapter
        binding.rvCities.layoutManager = layoutManager
    }

    private fun onCityClicked(weatherUiModel: WeatherUiModel) {

    }

    private fun observeData() {
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
                /*
                if (loading) pbLoading.visibility = View.VISIBLE
                else pbLoading.visibility = View.GONE*/
            }
        }
    }

    private fun showError(error: Throwable) {
        val errorMessage = error.message ?: getString(R.string.default_exception)
        Toast.makeText(
            requireContext(),
            errorMessage,
            Toast.LENGTH_SHORT
        ).show()
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