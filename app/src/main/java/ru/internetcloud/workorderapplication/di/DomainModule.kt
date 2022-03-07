package ru.internetcloud.workorderapplication.di

import dagger.Module
import dagger.Provides
import ru.internetcloud.workorderapplication.di.qualifiers.repository.*
import ru.internetcloud.workorderapplication.di.qualifiers.usecase.*
import ru.internetcloud.workorderapplication.domain.repository.*
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.car.GetCarListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.carjob.GetCarJobListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.department.GetDepartmentListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.employee.GetEmployeeListUseCase
import ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.partner.GetPartnerListUseCase
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

    @RemoteGetCarJobListUseCaseQualifier
    @Provides
    fun provideRemoteGetCarJobListUseCase(
        @RemoteCarJobRepositoryQualifier impl: CarJobRepository
    ): GetCarJobListUseCase {

        return GetCarJobListUseCase(impl)
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
