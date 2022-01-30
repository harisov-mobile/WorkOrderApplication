package ru.internetcloud.workorderapplication.di

import dagger.Component

@Component(modules = [ContextModule::class, DataModule::class, DomainModule::class])
interface ApplicationComponent {
}