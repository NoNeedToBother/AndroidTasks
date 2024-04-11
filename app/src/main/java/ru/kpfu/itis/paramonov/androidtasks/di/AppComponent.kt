package ru.kpfu.itis.paramonov.androidtasks.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import ru.kpfu.itis.paramonov.androidtasks.data.di.NetworkModule
import ru.kpfu.itis.paramonov.androidtasks.presentation.di.PresentationModule
import ru.kpfu.itis.paramonov.androidtasks.presentation.di.ViewModelModule
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.MainActivity
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.fragments.CityWeatherFragment
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.fragments.DebugResponseLogFragment
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.fragments.WeatherFragment
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.viewmodel.CityWeatherViewModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.viewmodel.WeatherViewModel
import javax.inject.Singleton

@Component(
    modules = [
        CommonModule::class,
        BinderModule::class,
        NetworkModule::class,
        PresentationModule::class,
        ViewModelModule::class
    ]
)
@Singleton
interface AppComponent {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(ctx:  Context): Builder

        fun build(): AppComponent
    }

    fun inject(activity: MainActivity)

    fun inject(weatherFragment: WeatherFragment)

    fun inject(cityWeatherFragment: CityWeatherFragment)

    fun inject(responseLogFragment: DebugResponseLogFragment)

    fun weatherViewModelFactory(): WeatherViewModel.Factory

    fun cityWeatherViewModelFactory(): CityWeatherViewModel.Factory
}