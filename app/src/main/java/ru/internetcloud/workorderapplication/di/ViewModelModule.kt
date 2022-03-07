package ru.internetcloud.workorderapplication.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.internetcloud.workorderapplication.presentation.logon.LogonViewModel
import ru.internetcloud.workorderapplication.presentation.sendemail.SendWorkOrderByIdToEmailViewModel
import ru.internetcloud.workorderapplication.presentation.synchro.DataSynchronizationFragmentViewModel
import ru.internetcloud.workorderapplication.presentation.workorder.detail.WorkOrderViewModel
import ru.internetcloud.workorderapplication.presentation.workorder.detail.car.CarListViewModel
import ru.internetcloud.workorderapplication.presentation.workorder.detail.carjob.CarJobListViewModel
import ru.internetcloud.workorderapplication.presentation.workorder.detail.department.DepartmentListViewModel
import ru.internetcloud.workorderapplication.presentation.workorder.detail.employee.EmployeeListViewModel
import ru.internetcloud.workorderapplication.presentation.workorder.detail.partner.PartnerListViewModel
import ru.internetcloud.workorderapplication.presentation.workorder.detail.repairtype.RepairTypeListViewModel
import ru.internetcloud.workorderapplication.presentation.workorder.detail.workinghour.WorkingHourListViewModel
import ru.internetcloud.workorderapplication.presentation.workorder.list.WorkOrderListViewModel

@Module
interface ViewModelModule {

    // перечислить тут все вью-модели

    @IntoMap
    @ViewModelKey(LogonViewModel::class)
    @Binds
    fun bindLogonViewModel(impl: LogonViewModel): ViewModel

    @IntoMap
    @ViewModelKey(SendWorkOrderByIdToEmailViewModel::class)
    @Binds
    fun bindSendWorkOrderByIdToEmailViewModel(impl: SendWorkOrderByIdToEmailViewModel): ViewModel

    @IntoMap
    @ViewModelKey(DataSynchronizationFragmentViewModel::class)
    @Binds
    fun bindDataSynchronizationFragmentViewModel(impl: DataSynchronizationFragmentViewModel): ViewModel

    @IntoMap
    @ViewModelKey(CarListViewModel::class)
    @Binds
    fun bindCarListViewModel(impl: CarListViewModel): ViewModel

    @IntoMap
    @ViewModelKey(CarJobListViewModel::class)
    @Binds
    fun bindCarJobListViewModel(impl: CarJobListViewModel): ViewModel

    @IntoMap
    @ViewModelKey(DepartmentListViewModel::class)
    @Binds
    fun bindDepartmentListViewModel(impl: DepartmentListViewModel): ViewModel

    @IntoMap
    @ViewModelKey(EmployeeListViewModel::class)
    @Binds
    fun bindEmployeeListViewModel(impl: EmployeeListViewModel): ViewModel

    @IntoMap
    @ViewModelKey(PartnerListViewModel::class)
    @Binds
    fun bindPartnerListViewModel(impl: PartnerListViewModel): ViewModel

//    @IntoMap
//    @ViewModelKey(PerformerDetailViewModel::class)
//    @Binds
//    fun bindPerformerDetailViewModel(impl: PerformerDetailViewModel): ViewModel

    @IntoMap
    @ViewModelKey(RepairTypeListViewModel::class)
    @Binds
    fun bindRepairTypeListViewModel(impl: RepairTypeListViewModel): ViewModel

    @IntoMap
    @ViewModelKey(WorkingHourListViewModel::class)
    @Binds
    fun bindWorkingHourListViewModel(impl: WorkingHourListViewModel): ViewModel

    @IntoMap
    @ViewModelKey(WorkOrderViewModel::class)
    @Binds
    fun bindWorkOrderViewModel(impl: WorkOrderViewModel): ViewModel

    @IntoMap
    @ViewModelKey(WorkOrderListViewModel::class)
    @Binds
    fun bindWorkOrderListViewModel(impl: WorkOrderListViewModel): ViewModel
}
