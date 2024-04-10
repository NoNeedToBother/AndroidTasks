package ru.kpfu.itis.paramonov.androidtasks.di

import dagger.Binds
import dagger.Module
import ru.kpfu.itis.paramonov.androidtasks.data.repository.ResponseRepositoryImpl
import ru.kpfu.itis.paramonov.androidtasks.data.repository.WeatherRepositoryImpl
import ru.kpfu.itis.paramonov.androidtasks.domain.repository.ResponseRepository
import ru.kpfu.itis.paramonov.androidtasks.domain.repository.WeatherRepository
import ru.kpfu.itis.paramonov.androidtasks.utils.ResourceManager
import ru.kpfu.itis.paramonov.androidtasks.utils.ResourceManagerImpl

@Module
interface BinderModule {

    @Binds
    fun bindWeatherRepositoryToImpl(
        impl: WeatherRepositoryImpl
    ): WeatherRepository

    @Binds
    fun bindResourceManagerToImpl(
        impl: ResourceManagerImpl
    ): ResourceManager

    @Binds
    fun bindResponseRepositoryToImpl(
        impl: ResponseRepositoryImpl
    ): ResponseRepository
}