package ru.internetcloud.workorderapplication.di

import dagger.Module
import dagger.Provides
import ru.internetcloud.workorderapplication.di.qualifiers.repository.DbCarJobRepositoryQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.repository.DbCarModelRepositoryQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.repository.DbCarRepositoryQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.repository.DbDepartmentRepositoryQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.repository.DbEmployeeRepositoryQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.repository.DbPartnerRepositoryQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.repository.DbRepairTypeRepositoryQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.repository.DbWorkingHourRepositoryQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.repository.RemoteCarJobRepositoryQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.repository.RemoteCarModelRepositoryQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.repository.RemoteCarRepositoryQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.repository.RemoteDepartmentRepositoryQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.repository.RemoteEmployeeRepositoryQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.repository.RemotePartnerRepositoryQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.repository.RemoteRepairTypeRepositoryQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.repository.RemoteWorkingHourRepositoryQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.usecase.DbGetCarJobListUseCaseQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.usecase.DbGetCarListUseCaseQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.usecase.DbGetCarModelListUseCaseQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.usecase.DbGetDefaultRepairTypeJobsUseCaseQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.usecase.DbGetDepartmentListUseCaseQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.usecase.DbGetEmployeeListUseCaseQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.usecase.DbGetPartnerListUseCaseQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.usecase.DbGetRepairTypeListUseCaseQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.usecase.DbGetWorkingHourListUseCaseQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.usecase.RemoteGetCarJobListUseCaseQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.usecase.RemoteGetCarListUseCaseQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.usecase.RemoteGetCarModelListUseCaseQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.usecase.RemoteGetDepartmentListUseCaseQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.usecase.RemoteGetEmployeeListUseCaseQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.usecase.RemoteGetPartnerListUseCaseQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.usecase.RemoteGetRepairTypeListUseCaseQualifier
import ru.internetcloud.workorderapplication.di.qualifiers.usecase.RemoteGetWorkingHourListUseCaseQualifier
import ru.internetcloud.workorderapplication.domain.repository.CarJobRepository
import ru.internetcloud.workorderapplication.domain.repository.CarModelRepository
import ru.internetcloud.workorderapplication.domain.repository.CarRepository
import ru.internetcloud.workorderapplication.domain.repository.DepartmentRepository
import ru.internetcloud.workorderapplication.domain.repository.EmployeeRepository
import ru.internetcloud.workorderapplication.domain.repository.PartnerRepository
import ru.internetcloud.workorderapplication.domain.repository.RepairTypeRepository
import ru.internetcloud.workorderapplication.domain.repository.WorkingHourRepository
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.car.GetCarListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob.GetCarJobListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carmodel.GetCarModelListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.department.GetDepartmentListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.employee.GetEmployeeListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.partner.GetPartnerListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype.GetDefaultRepairTypeJobsUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype.GetRepairTypeListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.workinghour.GetWorkingHourListUseCase

@Module
class DomainModule {

    @DbGetDepartmentListUseCaseQualifier
    @Provides
    fun provideDbGetDepartmentListUseCase(
        @DbDepartmentRepositoryQualifier impl: DepartmentRepository
    ): GetDepartmentListUseCase {

        return GetDepartmentListUseCase(impl)
    }

    @RemoteGetDepartmentListUseCaseQualifier
    @Provides
    fun provideRemoteGetDepartmentListUseCase(
        @RemoteDepartmentRepositoryQualifier impl: DepartmentRepository
    ): GetDepartmentListUseCase {

        return GetDepartmentListUseCase(impl)
    }

    @DbGetEmployeeListUseCaseQualifier
    @Provides
    fun provideDbGetEmployeeListUseCase(
        @DbEmployeeRepositoryQualifier impl: EmployeeRepository
    ): GetEmployeeListUseCase {

        return GetEmployeeListUseCase(impl)
    }

    @RemoteGetEmployeeListUseCaseQualifier
    @Provides
    fun provideRemoteGetEmployeeListUseCase(
        @RemoteEmployeeRepositoryQualifier impl: EmployeeRepository
    ): GetEmployeeListUseCase {

        return GetEmployeeListUseCase(impl)
    }

    @DbGetDefaultRepairTypeJobsUseCaseQualifier
    @Provides
    fun provideDbGetDefaultRepairTypeJobsUseCase(
        @DbRepairTypeRepositoryQualifier impl: RepairTypeRepository
    ): GetDefaultRepairTypeJobsUseCase {

        return GetDefaultRepairTypeJobsUseCase(impl)
    }

    @DbGetRepairTypeListUseCaseQualifier
    @Provides
    fun provideDbGetRepairTypeListUseCase(
        @DbRepairTypeRepositoryQualifier impl: RepairTypeRepository
    ): GetRepairTypeListUseCase {

        return GetRepairTypeListUseCase(impl)
    }

    @RemoteGetRepairTypeListUseCaseQualifier
    @Provides
    fun provideRemoteGetRepairTypeListUseCase(
        @RemoteRepairTypeRepositoryQualifier impl: RepairTypeRepository
    ): GetRepairTypeListUseCase {

        return GetRepairTypeListUseCase(impl)
    }

    @DbGetPartnerListUseCaseQualifier
    @Provides
    fun provideDbGetPartnerListUseCase(
        @DbPartnerRepositoryQualifier impl: PartnerRepository
    ): GetPartnerListUseCase {

        return GetPartnerListUseCase(impl)
    }

    @RemoteGetPartnerListUseCaseQualifier
    @Provides
    fun provideRemoteGetPartnerListUseCase(
        @RemotePartnerRepositoryQualifier impl: PartnerRepository
    ): GetPartnerListUseCase {

        return GetPartnerListUseCase(impl)
    }

    @DbGetWorkingHourListUseCaseQualifier
    @Provides
    fun provideDbGetWorkingHourListUseCase(
        @DbWorkingHourRepositoryQualifier impl: WorkingHourRepository
    ): GetWorkingHourListUseCase {

        return GetWorkingHourListUseCase(impl)
    }

    @RemoteGetWorkingHourListUseCaseQualifier
    @Provides
    fun provideRemoteGetWorkingHourListUseCase(
        @RemoteWorkingHourRepositoryQualifier impl: WorkingHourRepository
    ): GetWorkingHourListUseCase {

        return GetWorkingHourListUseCase(impl)
    }

    @DbGetCarJobListUseCaseQualifier
    @Provides
    fun provideDbGetCarJobListUseCase(
        @DbCarJobRepositoryQualifier impl: CarJobRepository
    ): GetCarJobListUseCase {

        return GetCarJobListUseCase(impl)
    }

    @DbGetCarModelListUseCaseQualifier
    @Provides
    fun provideDbGetCarModelListUseCase(
        @DbCarModelRepositoryQualifier impl: CarModelRepository
    ): GetCarModelListUseCase {

        return GetCarModelListUseCase(impl)
    }

    @RemoteGetCarJobListUseCaseQualifier
    @Provides
    fun provideRemoteGetCarJobListUseCase(
        @RemoteCarJobRepositoryQualifier impl: CarJobRepository
    ): GetCarJobListUseCase {

        return GetCarJobListUseCase(impl)
    }

    @RemoteGetCarModelListUseCaseQualifier
    @Provides
    fun provideRemoteGetCarModelListUseCase(
        @RemoteCarModelRepositoryQualifier impl: CarModelRepository
    ): GetCarModelListUseCase {

        return GetCarModelListUseCase(impl)
    }

    @DbGetCarListUseCaseQualifier
    @Provides
    fun provideDbGetCarListUseCase(
        @DbCarRepositoryQualifier impl: CarRepository
    ): GetCarListUseCase {

        return GetCarListUseCase(impl)
    }

    @RemoteGetCarListUseCaseQualifier
    @Provides
    fun provideRemoteGetCarListUseCase(
        @RemoteCarRepositoryQualifier impl: CarRepository
    ): GetCarListUseCase {

        return GetCarListUseCase(impl)
    }
}
