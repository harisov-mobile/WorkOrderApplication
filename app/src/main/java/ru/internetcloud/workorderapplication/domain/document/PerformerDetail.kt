package ru.internetcloud.workorderapplication.domain.document

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.internetcloud.workorderapplication.domain.catalog.Employee

// это строка табличной части "Исполнители"
@Parcelize
data class PerformerDetail(
    var id: String = "",
    var lineNumber: Int = 0,
    var employee: Employee? = null,
    var isSelected: Boolean = false
) : Parcelable {

    fun copyFields(anotherPerformerDetail: PerformerDetail) {
        this.id = anotherPerformerDetail.id
        this.lineNumber = anotherPerformerDetail.lineNumber
        this.employee = anotherPerformerDetail.employee
        this.isSelected = anotherPerformerDetail.isSelected
    }

    companion object {
        fun getNewPerformerDetail(order: WorkOrder): PerformerDetail {
            var lineNumber = order.performers.size
            lineNumber++
            val id = order.id + "_" + lineNumber.toString()
            return PerformerDetail(id = id, lineNumber = lineNumber)
        }
    }
}
