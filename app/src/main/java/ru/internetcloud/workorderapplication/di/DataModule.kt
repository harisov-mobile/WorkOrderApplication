package ru.internetcloud.workorderapplication.di

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.internetcloud.workorderapplication.data.database.AppDao
import ru.internetcloud.workorderapplication.data.database.AppDatabase
import ru.internetcloud.workorderapplication.data.repository.AuthRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.SynchroRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.db.*
import ru.internetcloud.workorderapplication.data.repository.remote.*
import ru.internetcloud.workorderapplication.di.qualifiers.repository.*
import ru.internetcloud.workorderapplication.domain.common.AuthParameters
import ru.internetcloud.workorderapplication.domain.repository.*

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
