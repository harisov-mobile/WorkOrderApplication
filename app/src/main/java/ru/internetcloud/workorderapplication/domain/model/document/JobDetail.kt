package ru.internetcloud.workorderapplication.domain.model.document

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.internetcloud.workorderapplication.domain.model.catalog.CarJob
import ru.internetcloud.workorderapplication.domain.model.catalog.WorkingHour
import java.math.BigDecimal

// это строка табличной части "Работы"
@Parcelize
data class JobDetail(
    var id: String = "",
    var lineNumber: Int = 0,
    var carJob: CarJob? = null,
    var quantity: BigDecimal = BigDecimal.ZERO,
    var timeNorm: BigDecimal = BigDecimal.ZERO,
    var workingHour: WorkingHour? = null, // нормо-часы
    var sum: BigDecimal = BigDecimal.ZERO,
    var isSelected: Boolean = false
) : Parcelable {

    fun copyFields(anotherJobDetail: JobDetail) {
        this.id = anotherJobDetail.id
        this.lineNumber = anotherJobDetail.lineNumber
        this.carJob = anotherJobDetail.carJob
        this.quantity = anotherJobDetail.quantity
        this.timeNorm = anotherJobDetail.timeNorm
        this.workingHour = anotherJobDetail.workingHour
        this.sum = anotherJobDetail.sum
        this.isSelected = anotherJobDetail.isSelected
    }

    companion object {
        fun getNewJobDetail(order: WorkOrder): JobDetail {
            var lineNumber = order.jobDetails.size
            lineNumber++
            val id = order.id + "_" + lineNumber.toString()
            return JobDetail(id = id, lineNumber = lineNumber)
        }
    }
}
