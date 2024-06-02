package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.fragments.weather

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import ru.kpfu.itis.paramonov.androidtasks.BuildConfig
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentWeatherCityBinding
import ru.kpfu.itis.paramonov.androidtasks.presentation.base.BaseFragment
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.forecast.ForecastListWeatherUiModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.model.weather.WeatherUiModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.adapter.ForecastAdapter
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.viewmodel.CityWeatherViewModel
import ru.kpfu.itis.paramonov.androidtasks.utils.SpacingItemDecorator
import ru.kpfu.itis.paramonov.androidtasks.utils.Params
import ru.kpfu.itis.paramonov.androidtasks.utils.ResourceManager
import ru.kpfu.itis.paramonov.androidtasks.utils.appComponent
import ru.kpfu.itis.paramonov.androidtasks.utils.gone
import ru.kpfu.itis.paramonov.androidtasks.utils.lazyViewModel
import ru.kpfu.itis.paramonov.androidtasks.utils.show
import javax.inject.Inject

class CityWeatherFragment: BaseFragment() {

    private val binding: FragmentWeatherCityBinding by viewBinding(FragmentWeatherCityBinding::bind)

    private var currentTemperature: Double? = null

    @Inject
    lateinit var resourceManager: ResourceManager

    private val viewModel: CityWeatherViewModel by lazyViewModel {
        val city = requireArguments().getString(CITY_KEY, Params.CITY_EMPTY_DATA)
        val longitude = requireArguments().getDouble(LONGITUDE_KEY)
        val latitude = requireArguments().getDouble(LATITUDE_KEY)
        requireContext().appComponent.cityWeatherViewModelFactory().create(
            longitude, latitude, city
        )
    }

    override fun layout(): Int = R.layout.fragment_weather_city

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireContext().appComponent.inject(this)
    }

    private var permission: String = ""

    private val permissionActivityResult = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) onPermissionGranted()
        else {
            if (permission.isNotEmpty() &&
                shouldShowRequestPermissionRationale(permission)) showRationale()
            else onPermissionDenied()
        }
    }

    private fun onPermissionGranted() {
        val city = requireArguments().getString(CITY_KEY, Params.CITY_EMPTY_DATA)
        ContactsBottomSheetFragment.newInstance(
            city = city, temperature = currentTemperature ?: 0.0
        ).show(childFragmentManager, ContactsBottomSheetFragment.CONTACTS_BOTTOM_SHEET_TAG)
    }

    private fun showRationale() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.provide_permissions)
            .setMessage(R.string.contacts_permission_rationale)
            .setPositiveButton(R.string.allow) { _, _ ->
                permissionActivityResult.launch(permission)
            }
            .setNegativeButton(R.string.deny) {_, _ -> onPermissionDenied() }
            .show()
    }

    private fun onPermissionDenied() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.permission_settings)
            .setMessage(R.string.permission_settings_text)
            .setPositiveButton(R.string.go_to_settings) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse(
                    "package:${requireActivity().packageName}"))
                startActivity(intent)
            }
            .setNegativeButton(R.string.deny) {_, _ ->}
            .show()
    }

    override fun init() {
        with(binding) {
            fabShareWeather.setOnClickListener {
                currentTemperature?.let { _ ->
                    permission = Manifest.permission.READ_CONTACTS
                    permissionActivityResult.launch(Manifest.permission.READ_CONTACTS)
                } ?: showError(getString(R.string.no_share_data))
            }
        }
    }

    private fun initRecyclerView(forecasts: List<ForecastListWeatherUiModel>) {
        with(binding.rvForecast) {
            val adapter = ForecastAdapter(
                requireContext(), resourceManager
            )
            adapter.setItems(forecasts)
            this.adapter = adapter
            val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            this.layoutManager = layoutManager
            addItemDecoration(SpacingItemDecorator(20, SpacingItemDecorator.Side.RIGHT))
        }
    }

    override fun observeData() {
        viewModel.getWeatherInfo()
        viewModel.getForecastInfo()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(state = Lifecycle.State.CREATED) {
                launch {
                    collectWeatherData()
                }
                launch {
                    collectWeatherLoadingData()
                }
                launch {
                    collectForecastData()
                }
            }
        }
    }

    private suspend fun collectForecastData() {
        viewModel.forecastFlow.collect { result ->
            result?.let {
                when (it) {
                    is CityWeatherViewModel.ForecastDataResult.Success ->
                        initRecyclerView(it.getValue().forecasts)
                    is CityWeatherViewModel.ForecastDataResult.Failure ->
                        showError(it.getException())
                }
            }
        }
    }

    private suspend fun collectWeatherLoadingData() {
        viewModel.weatherLoadingFlow.collect {loading ->
            with(binding) {
                if (loading) pbWeatherLoading.show()
                else pbWeatherLoading.gone()
            }
        }
    }

    private suspend fun collectWeatherData() {
        viewModel.currentWeatherFlow.collect { result ->
            result?.let {
                when (it) {
                    is CityWeatherViewModel.WeatherDataResult.Success -> {
                        val data = it.getValue()
                        onWeatherDataReceived(data)
                    }
                    is CityWeatherViewModel.WeatherDataResult.Failure ->
                        showError(it.getException())
                }
            }
        }
    }

    private fun onWeatherDataReceived(weatherUiModel: WeatherUiModel) {
        with(weatherUiModel) {
            currentTemperature = temperature
            with(binding) {
                tvCity.text = city
                llWeatherInfo.show()
                tvMain.text = weatherData.main
                tvDesc.text = weatherData.description
                tvTemperature.text = getString(R.string.temperature, temperature)
                loadWeatherIcon(weatherData.icon)
            }
        }
    }

    private fun loadWeatherIcon(icon: String) {
        val url = String.format(BuildConfig.OPEN_WEATHER_ICON_URL, icon)
        Glide.with(requireContext())
            .load(url)
            .error(R.drawable.placeholder_img)
            .into(binding.ivIcon)
    }


    companion object {
        const val CITY_WEATHER_FRAGMENT_TAG = "CITY_WEATHER_FRAGMENT"

        private const val ICON_KEY = "icon"
        private const val CITY_KEY = "key"
        private const val LONGITUDE_KEY = "longitude"
        private const val LATITUDE_KEY = "latitude"

        fun newInstance(icon: String, city: String, longitude: Double, latitude: Double) =
            CityWeatherFragment().apply {
            arguments = bundleOf(
                ICON_KEY to icon,
                CITY_KEY to city,
                LONGITUDE_KEY to longitude,
                LATITUDE_KEY to latitude
            )
        }
    }
}