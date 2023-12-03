package ru.internetcloud.workorderapplication.presentation.workorder.detail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.internetcloud.workorderapplication.domain.model.document.JobDetail
import ru.internetcloud.workorderapplication.domain.model.document.PerformerDetail
import ru.internetcloud.workorderapplication.domain.model.document.WorkOrder

@Parcelize
data class UiWorkOrderDetailState(
    val workOrder: WorkOrder = WorkOrder(),

    // вспомогательные поля:
    val shouldInit: Boolean = true, // чтобы различить - это ОС Андроид восстановила данные или ?
    val loading: Boolean = true,
    val saving: Boolean = false, // отобразим ПрогрессБар в кнопке "Save"
    val canFillDefaultJobs: Boolean = false, //

    val selectedPerformerDetail: PerformerDetail? = null,
    val selectedJobDetail: JobDetail? = null,

    val isModified: Boolean = false,
    val shouldCloseScreen: Boolean = false,
    val error: Throwable? = null,

    // поля, касающиеся валидации - правильности ввода
    val errorInputNumber: Boolean = false,      // не заполнен номер
    val errorDuplicateNumber: Boolean = false, // существует ли в Room другой заказ-наряд с таким номером
    val errorInputEmail: Boolean = false,      // неправильный или незаполненный email
    val errorInputPerformer: Boolean = false  // не указан исполнитель или исполнители
) : Parcelable
