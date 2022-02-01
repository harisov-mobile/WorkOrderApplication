package ru.internetcloud.workorderapplication.di

import androidx.lifecycle.ViewModel
import dagger.Component

@Component(modules = [ContextModule::class, DataModule::class, DomainModule::class])
interface ApplicationComponent {
    fun inject(viewModel: ViewModel)
}