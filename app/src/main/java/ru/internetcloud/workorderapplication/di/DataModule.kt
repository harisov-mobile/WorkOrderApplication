package ru.internetcloud.workorderapplication.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.internetcloud.workorderapplication.data.repository.AuthRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.db.DbPartnerRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.db.DbRepairTypeRepositoryImpl
import ru.internetcloud.workorderapplication.domain.repository.AuthRepository
import ru.internetcloud.workorderapplication.domain.repository.PartnerRepository
import ru.internetcloud.workorderapplication.domain.repository.RepairTypeRepository

@Module
interface DataModule {

//    @Provides
//    fun provideRepairTypeRepository(impl: DbRepairTypeRepositoryImpl): RepairTypeRepository {
//        return impl;
//    }
//
//    @Provides
//    fun provideDbRepairTypeRepositoryImpl(): DbRepairTypeRepositoryImpl {
//        return DbRepairTypeRepositoryImpl.get();
//    }

    @Binds
    fun bindRepairTypeRepository(impl: DbRepairTypeRepositoryImpl): RepairTypeRepository

    @Binds
    fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    // квалификатор делать
    @Binds
    fun bindPartnerRepository(impl: DbPartnerRepositoryImpl): PartnerRepository
}
