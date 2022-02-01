package ru.internetcloud.workorderapplication.di

import dagger.Module
import dagger.Provides
import ru.internetcloud.workorderapplication.data.repository.db.DbRepairTypeRepositoryImpl
import ru.internetcloud.workorderapplication.domain.repository.RepairTypeRepository

@Module
class DataModule {

//    @Provides
//    fun provideRepairTypeRepository(impl: DbRepairTypeRepositoryImpl): RepairTypeRepository {
//        return impl;
//    }
//
//    @Provides
//    fun provideDbRepairTypeRepositoryImpl(): DbRepairTypeRepositoryImpl {
//        return DbRepairTypeRepositoryImpl.get();
//    }

    @Provides
    fun provideRepairTypeRepository(): RepairTypeRepository {
        return DbRepairTypeRepositoryImpl.get();
    }
}