package ru.kpfu.itis.paramonov.androidtasks.di

import dagger.Binds
import dagger.Module
import ru.kpfu.itis.paramonov.androidtasks.data.repository.WeatherRepositoryImpl
import ru.kpfu.itis.paramonov.androidtasks.domain.repository.WeatherRepository
import ru.kpfu.itis.paramonov.androidtasks.utils.ResManager
import ru.kpfu.itis.paramonov.androidtasks.utils.ResManagerImpl

@Module
interface BinderModule {

    @Binds
    fun bindWeatherRepositoryToImpl(
        impl: WeatherRepositoryImpl
    ): WeatherRepository

    @Binds
    fun bindResManagerToImpl(
        impl: ResManagerImpl
    ): ResManager
}