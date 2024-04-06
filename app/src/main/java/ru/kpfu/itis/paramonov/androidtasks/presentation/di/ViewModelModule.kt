package ru.kpfu.itis.paramonov.androidtasks.presentation.di

import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
interface ViewModelModule {

    @Binds
    fun bindDaggerFactoryToViewModelFactory(impl: DaggerViewModelFactory): ViewModelProvider.Factory
}