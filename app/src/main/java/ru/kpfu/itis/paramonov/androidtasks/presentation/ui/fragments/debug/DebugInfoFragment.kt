package ru.kpfu.itis.paramonov.androidtasks.presentation.ui.fragments.debug

import android.os.Build
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.kpfu.itis.paramonov.androidtasks.BuildConfig
import ru.kpfu.itis.paramonov.androidtasks.R
import ru.kpfu.itis.paramonov.androidtasks.databinding.FragmentDebugInfoBinding
import ru.kpfu.itis.paramonov.androidtasks.presentation.base.BaseFragment

class DebugInfoFragment(): BaseFragment() {

    private val binding: FragmentDebugInfoBinding by viewBinding(FragmentDebugInfoBinding::bind)

    override fun layout(): Int = R.layout.fragment_debug_info

    override fun init() {
        showDebugInfo()
    }

    override fun observeData() {}

    private fun showDebugInfo() {
        with (binding) {
            tvAppName.text = getString(
                R.string.debug_app_name,
                getString(R.string.app_name)
            )
            tvBaseUrl.text = getString(
                R.string.debug_base_url,
                String.format(BuildConfig.OPEN_WEATHER_BASE_URL, "")
            )
            tvVersionCodeName.text = getString(
                R.string.debug_version_code_name,
                BuildConfig.VERSION_NAME,
                BuildConfig.VERSION_CODE
            )
            tvManufacturerModel.text = getString(
                R.string.debug_manufacturer_model,
                Build.MANUFACTURER,
                Build.MODEL
            )
            tvVersionApi.text = getString(
                R.string.debug_version_api,
                Build.VERSION.RELEASE,
                Build.VERSION.SDK_INT
            )
        }
    }
}