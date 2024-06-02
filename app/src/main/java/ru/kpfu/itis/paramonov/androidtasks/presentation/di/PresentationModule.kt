package ru.kpfu.itis.paramonov.androidtasks.presentation.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.viewmodel.ContactsViewModel
import ru.kpfu.itis.paramonov.androidtasks.presentation.ui.viewmodel.DebugResponseLogViewModel

@Module
abstract class PresentationModule {
    @Binds
    @IntoMap
    @ViewModelKey(DebugResponseLogViewModel::class)
    abstract fun bindDebugResponseLogViewModel(viewModel: DebugResponseLogViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ContactsViewModel::class)
    abstract fun bindContactsViewModel(viewModel: ContactsViewModel): ViewModel
}