package ru.kpfu.itis.paramonov.androidtasks.presentation.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import ru.kpfu.itis.paramonov.androidtasks.utils.DaggerViewModelFactory

@Module
interface ViewModelModule {

    @Binds
    fun bindDaggerFactoryToViewModelFactory(impl: DaggerViewModelFactory): ViewModelProvider.Factory
}