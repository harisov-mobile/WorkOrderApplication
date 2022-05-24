package ru.internetcloud.workorderapplication.di

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.internetcloud.workorderapplication.data.database.AppDao
import ru.internetcloud.workorderapplication.data.database.AppDatabase
import ru.internetcloud.workorderapplication.data.repository.AuthRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.SynchroRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.db.DbCarJobRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.db.DbCarModelRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.db.DbCarRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.db.DbDefaultWorkOrderSettingsRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.db.DbDepartmentRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.db.DbEmployeeRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.db.DbPartnerRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.db.DbRepairTypeRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.db.DbWorkOrderRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.db.DbWorkingHourRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.remote.RemoteCarJobRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.remote.RemoteCarModelRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.remote.RemoteCarRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.remote.RemoteDepartmentRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.remote.RemoteEmployeeRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.remote.RemotePartnerRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.remote.RemoteWorkingHourRepositoryImpl
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
import ru.internetcloud.workorderapplication.di.qualifiers.repository.RemoteWorkingHourRepositoryQualifier
import ru.internetcloud.workorderapplication.domain.common.AuthParameters
import ru.internetcloud.workorderapplication.domain.repository.AuthRepository
import ru.internetcloud.workorderapplication.domain.repository.CarJobRepository
import ru.internetcloud.workorderapplication.domain.repository.CarModelRepository
import ru.internetcloud.workorderapplication.domain.repository.CarRepository
import ru.internetcloud.workorderapplication.domain.repository.DefaultWorkOrderSettingsRepository
import ru.internetcloud.workorderapplication.domain.repository.DepartmentRepository
import ru.internetcloud.workorderapplication.domain.repository.EmployeeRepository
import ru.internetcloud.workorderapplication.domain.repository.PartnerRepository
import ru.internetcloud.workorderapplication.domain.repository.RepairTypeRepository
import ru.internetcloud.workorderapplication.domain.repository.SynchroRepository
import ru.internetcloud.workorderapplication.domain.repository.WorkOrderRepository
import ru.internetcloud.workorderapplication.domain.repository.WorkingHourRepository

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    // квалификатор делать
    @DbPartnerRepositoryQualifier
    @ApplicationScope
    @Binds
    fun bindDbPartnerRepository(impl: DbPartnerRepositoryImpl): PartnerRepository

    @RemotePartnerRepositoryQualifier
    @ApplicationScope
    @Binds
    fun bindRemotePartnerRepository(impl: RemotePartnerRepositoryImpl): PartnerRepository

    @DbRepairTypeRepositoryQualifier
    @ApplicationScope
    @Binds
    fun bindDbRepairTypeRepository(impl: DbRepairTypeRepositoryImpl): RepairTypeRepository

    @DbCarJobRepositoryQualifier
    @ApplicationScope
    @Binds
    fun bindDbCarJobRepository(impl: DbCarJobRepositoryImpl): CarJobRepository

    @DbCarModelRepositoryQualifier
    @ApplicationScope
    @Binds
    fun bindDbCarModelRepository(impl: DbCarModelRepositoryImpl): CarModelRepository

    @RemoteCarJobRepositoryQualifier
    @ApplicationScope
    @Binds
    fun bindRemoteCarJobRepository(impl: RemoteCarJobRepositoryImpl): CarJobRepository

    @RemoteCarModelRepositoryQualifier
    @ApplicationScope
    @Binds
    fun bindRemoteCarModelRepository(impl: RemoteCarModelRepositoryImpl): CarModelRepository

    @DbDepartmentRepositoryQualifier
    @ApplicationScope
    @Binds
    fun bindDbDepartmentRepository(impl: DbDepartmentRepositoryImpl): DepartmentRepository

    @RemoteDepartmentRepositoryQualifier
    @ApplicationScope
    @Binds
    fun bindRemoteDepartmentRepository(impl: RemoteDepartmentRepositoryImpl): DepartmentRepository

    @DbEmployeeRepositoryQualifier
    @ApplicationScope
    @Binds
    fun bindDbEmployeeRepository(impl: DbEmployeeRepositoryImpl): EmployeeRepository

    @RemoteEmployeeRepositoryQualifier
    @ApplicationScope
    @Binds
    fun bindRemoteEmployeeRepository(impl: RemoteEmployeeRepositoryImpl): EmployeeRepository

    @DbCarRepositoryQualifier
    @ApplicationScope
    @Binds
    fun bindDbCarRepository(impl: DbCarRepositoryImpl): CarRepository

    @RemoteCarRepositoryQualifier
    @ApplicationScope
    @Binds
    fun bindRemoteCarRepository(impl: RemoteCarRepositoryImpl): CarRepository

    @DbWorkingHourRepositoryQualifier
    @ApplicationScope
    @Binds
    fun bindDbWorkingHourRepository(impl: DbWorkingHourRepositoryImpl): WorkingHourRepository

    @RemoteWorkingHourRepositoryQualifier
    @ApplicationScope
    @Binds
    fun bindRemoteWorkingHourRepository(impl: RemoteWorkingHourRepositoryImpl): WorkingHourRepository

    @ApplicationScope
    @Binds
    fun bindDbDefaultWorkOrderSettingsRepository(impl: DbDefaultWorkOrderSettingsRepositoryImpl): DefaultWorkOrderSettingsRepository

    @ApplicationScope
    @Binds
    fun bindSynchroRepository(impl: SynchroRepositoryImpl): SynchroRepository

    @ApplicationScope
    @Binds
    fun bindWorkOrderRepository(impl: DbWorkOrderRepositoryImpl): WorkOrderRepository

    companion object {

        @ApplicationScope
        @Provides
        fun provideAppDao(
            application: Application
        ): AppDao {
            return AppDatabase.getInstance(application).appDao()
        }

        @ApplicationScope
        @Provides
        fun provideAuthParameters(): AuthParameters {
            return AuthParameters()
        }
    }
}
