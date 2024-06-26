package ru.internetcloud.workorderapplication.synchro.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.common.domain.common.UpdateState
import ru.internetcloud.workorderapplication.synchro.domain.usecase.UpdateDataUseCase

@HiltViewModel
class DataSynchronizationFragmentViewModel @Inject constructor(
    private val updateDataUseCase: UpdateDataUseCase
) : ViewModel() {

    private val _updateState = MutableLiveData<UpdateState>()
    val updateState: LiveData<UpdateState>
        get() = _updateState

    init {
        synchonizeData()
    }
    private fun synchonizeData() {
        viewModelScope.launch {
            _updateState.value = UpdateState.Loading
            _updateState.value = updateDataUseCase.updateData()
        }
    }
}
