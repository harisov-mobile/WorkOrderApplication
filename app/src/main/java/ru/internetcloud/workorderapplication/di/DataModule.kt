package ru.internetcloud.workorderapplication.di

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.internetcloud.workorderapplication.data.database.AppDao
import ru.internetcloud.workorderapplication.data.database.AppDatabase
import ru.internetcloud.workorderapplication.data.repository.AuthRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.SynchroRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.common.CarJobRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.common.CarModelRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.common.CarRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.common.DefaultWorkOrderSettingsRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.common.DepartmentRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.common.EmployeeRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.common.PartnerRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.common.RepairTypeRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.common.WorkOrderRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.common.WorkingHourRepositoryImpl
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

    @ApplicationScope
    @Binds
    fun bindPartnerRepository(impl: PartnerRepositoryImpl): PartnerRepository

    @ApplicationScope
    @Binds
    fun bindRepairTypeRepository(impl: RepairTypeRepositoryImpl): RepairTypeRepository

    @ApplicationScope
    @Binds
    fun bindCarJobRepository(impl: CarJobRepositoryImpl): CarJobRepository

    @ApplicationScope
    @Binds
    fun bindCarModelRepository(impl: CarModelRepositoryImpl): CarModelRepository

    @ApplicationScope
    @Binds
    fun bindDepartmentRepository(impl: DepartmentRepositoryImpl): DepartmentRepository

    @ApplicationScope
    @Binds
    fun bindEmployeeRepository(impl: EmployeeRepositoryImpl): EmployeeRepository

    @ApplicationScope
    @Binds
    fun bindCarRepository(impl: CarRepositoryImpl): CarRepository

    @ApplicationScope
    @Binds
    fun bindWorkingHourRepository(impl: WorkingHourRepositoryImpl): WorkingHourRepository

    @ApplicationScope
    @Binds
    fun bindDefaultWorkOrderSettingsRepository(impl: DefaultWorkOrderSettingsRepositoryImpl): DefaultWorkOrderSettingsRepository

    @ApplicationScope
    @Binds
    fun bindSynchroRepository(impl: SynchroRepositoryImpl): SynchroRepository

    @ApplicationScope
    @Binds
    fun bindWorkOrderRepository(impl: WorkOrderRepositoryImpl): WorkOrderRepository

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
