package ru.internetcloud.workorderapplication.di

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

interface AssistedSavedStateViewModelFactory<T : ViewModel> {

    fun create(savedStateHandle: SavedStateHandle): T
}
