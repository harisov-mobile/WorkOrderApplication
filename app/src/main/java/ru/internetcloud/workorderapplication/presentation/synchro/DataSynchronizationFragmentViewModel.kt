package ru.internetcloud.workorderapplication.presentation.synchro

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.internetcloud.workorderapplication.domain.common.UpdateState
import ru.internetcloud.workorderapplication.domain.usecase.synchrooperation.UpdateDataUseCase
import javax.inject.Inject

class DataSynchronizationFragmentViewModel @Inject constructor(
    private val updateDataUseCase: UpdateDataUseCase
) : ViewModel() {

    private val _updateState = MutableLiveData<UpdateState>()
    val updateState: LiveData<UpdateState>
        get() = _updateState

    fun synchonizeData() {
        viewModelScope.launch {
            _updateState.value = UpdateState.Loading

            _updateState.value = updateDataUseCase.updateData()
        }
    }
}
