package ru.internetcloud.workorderapplication.common.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ru.internetcloud.workorderapplication.common.data.database.AppDao
import ru.internetcloud.workorderapplication.common.data.database.AppDatabase
import ru.internetcloud.workorderapplication.common.data.repository.AuthRepositoryImpl
import ru.internetcloud.workorderapplication.common.data.repository.FirstLaunchRepositoryImpl
import ru.internetcloud.workorderapplication.common.data.repository.SynchroRepositoryImpl
import ru.internetcloud.workorderapplication.common.data.repository.common.AuthorizationPreferencesRepositoryImpl
import ru.internetcloud.workorderapplication.common.data.repository.common.CarJobRepositoryImpl
import ru.internetcloud.workorderapplication.common.data.repository.common.CarModelRepositoryImpl
import ru.internetcloud.workorderapplication.common.data.repository.common.CarRepositoryImpl
import ru.internetcloud.workorderapplication.common.data.repository.common.DefaultWorkOrderSettingsRepositoryImpl
import ru.internetcloud.workorderapplication.common.data.repository.common.DepartmentRepositoryImpl
import ru.internetcloud.workorderapplication.common.data.repository.common.EmployeeRepositoryImpl
import ru.internetcloud.workorderapplication.common.data.repository.common.PartnerRepositoryImpl
import ru.internetcloud.workorderapplication.common.data.repository.common.RepairTypeRepositoryImpl
import ru.internetcloud.workorderapplication.common.data.repository.common.WorkOrderRepositoryImpl
import ru.internetcloud.workorderapplication.common.data.repository.common.WorkingHourRepositoryImpl
import ru.internetcloud.workorderapplication.common.domain.common.AuthParameters
import ru.internetcloud.workorderapplication.common.domain.common.FirstLaunchRepository
import ru.internetcloud.workorderapplication.common.domain.repository.AuthRepository
import ru.internetcloud.workorderapplication.common.domain.repository.AuthorizationPreferencesRepository
import ru.internetcloud.workorderapplication.common.domain.repository.CarJobRepository
import ru.internetcloud.workorderapplication.common.domain.repository.CarModelRepository
import ru.internetcloud.workorderapplication.common.domain.repository.CarRepository
import ru.internetcloud.workorderapplication.common.domain.repository.DefaultWorkOrderSettingsRepository
import ru.internetcloud.workorderapplication.common.domain.repository.DepartmentRepository
import ru.internetcloud.workorderapplication.common.domain.repository.EmployeeRepository
import ru.internetcloud.workorderapplication.common.domain.repository.PartnerRepository
import ru.internetcloud.workorderapplication.common.domain.repository.RepairTypeRepository
import ru.internetcloud.workorderapplication.common.domain.repository.SynchroRepository
import ru.internetcloud.workorderapplication.common.domain.repository.WorkOrderRepository
import ru.internetcloud.workorderapplication.common.domain.repository.WorkingHourRepository

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Singleton
    @Binds
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Singleton
    @Binds
    fun bindPartnerRepository(impl: PartnerRepositoryImpl): PartnerRepository

    @Singleton
    @Binds
    fun bindRepairTypeRepository(impl: RepairTypeRepositoryImpl): RepairTypeRepository

    @Singleton
    @Binds
    fun bindCarJobRepository(impl: CarJobRepositoryImpl): CarJobRepository

    @Singleton
    @Binds
    fun bindCarModelRepository(impl: CarModelRepositoryImpl): CarModelRepository

    @Singleton
    @Binds
    fun bindDepartmentRepository(impl: DepartmentRepositoryImpl): DepartmentRepository

    @Singleton
    @Binds
    fun bindEmployeeRepository(impl: EmployeeRepositoryImpl): EmployeeRepository

    @Singleton
    @Binds
    fun bindCarRepository(impl: CarRepositoryImpl): CarRepository

    @Singleton
    @Binds
    fun bindWorkingHourRepository(impl: WorkingHourRepositoryImpl): WorkingHourRepository

    @Singleton
    @Binds
    fun bindDefaultWorkOrderSettingsRepository(impl: DefaultWorkOrderSettingsRepositoryImpl): DefaultWorkOrderSettingsRepository

    @Singleton
    @Binds
    fun bindSynchroRepository(impl: SynchroRepositoryImpl): SynchroRepository

    @Singleton
    @Binds
    fun bindWorkOrderRepository(impl: WorkOrderRepositoryImpl): WorkOrderRepository

    @Singleton
    @Binds
    fun bindAuthorizationPreferencesRepository(impl: AuthorizationPreferencesRepositoryImpl): AuthorizationPreferencesRepository

    @Singleton
    @Binds
    fun bindFirstLaunchRepository(impl: FirstLaunchRepositoryImpl): FirstLaunchRepository

    companion object {

        @Singleton
        @Provides
        fun provideAppDao(
            @ApplicationContext context: Context
        ): AppDao {
            return AppDatabase.getInstance(context).appDao()
        }

        @Singleton
        @Provides
        fun provideAuthParameters(): AuthParameters {
            return AuthParameters()
        }
    }
}
