package ru.internetcloud.workorderapplication.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ru.internetcloud.workorderapplication.presentation.logon.LogonFragment
import ru.internetcloud.workorderapplication.presentation.sendemail.SendWorkOrderByIdToEmailDialogFragment
import ru.internetcloud.workorderapplication.presentation.synchro.DataSynchronizationFragment
import ru.internetcloud.workorderapplication.presentation.workorder.detail.WorkOrderFragment
import ru.internetcloud.workorderapplication.presentation.workorder.detail.car.CarPickerFragment
import ru.internetcloud.workorderapplication.presentation.workorder.detail.carjob.CarJobPickerFragment
import ru.internetcloud.workorderapplication.presentation.workorder.detail.department.DepartmentPickerFragment
import ru.internetcloud.workorderapplication.presentation.workorder.detail.employee.EmployeePickerFragment
import ru.internetcloud.workorderapplication.presentation.workorder.detail.partner.PartnerPickerFragment
import ru.internetcloud.workorderapplication.presentation.workorder.detail.repairtype.RepairTypePickerFragment
import ru.internetcloud.workorderapplication.presentation.workorder.detail.workinghour.WorkingHourPickerFragment
import ru.internetcloud.workorderapplication.presentation.workorder.list.WorkOrderListFragment

@Component(modules = [DomainModule::class, DataModule::class, ViewModelModule::class])
@ApplicationScope
interface ApplicationComponent {

    fun inject(fragment: LogonFragment)

    fun inject(fragment: CarPickerFragment)

    fun inject(fragment: DepartmentPickerFragment)

    fun inject(fragment: EmployeePickerFragment)

    fun inject(fragment: PartnerPickerFragment)

    fun inject(fragment: RepairTypePickerFragment)

    fun inject(fragment: WorkingHourPickerFragment)

    fun inject(fragment: WorkOrderFragment)

    fun inject(fragment: WorkOrderListFragment)

    fun inject(fragment: SendWorkOrderByIdToEmailDialogFragment)

    fun inject(fragment: DataSynchronizationFragment)

    fun inject(fragment: CarJobPickerFragment)

    @Component.Factory
    interface Factory {

        fun create(@BindsInstance application: Application): ApplicationComponent
    }
}
