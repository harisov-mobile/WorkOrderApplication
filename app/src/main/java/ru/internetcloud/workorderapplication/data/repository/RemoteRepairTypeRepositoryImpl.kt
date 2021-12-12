package ru.internetcloud.workorderapplication.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import ru.internetcloud.workorderapplication.domain.catalog.RepairType
import ru.internetcloud.workorderapplication.domain.repository.RepairTypeRepository

// это класс-синглтон, единственный экземпляр этого репозитория создается при запуске приложения
class RemoteRepairTypeRepositoryImpl private constructor(application: Application) : RepairTypeRepository {

    companion object {
        private var instance: RemoteRepairTypeRepositoryImpl? = null

        fun initialize(application: Application) {
            if (instance == null) {
                instance = RemoteRepairTypeRepositoryImpl(application)
            }
        }

        fun get(): RemoteRepairTypeRepositoryImpl {
            return instance ?: throw RuntimeException("RemoteRepairTypeRepositoryImpl must be initialized.")
        }
    }


    override fun getRepairTypeList(): LiveData<List<RepairType>> {
        TODO("Not yet implemented")
    }

    override fun getRepairType(id: Int): RepairType? {
        TODO("Not yet implemented")
    }

    override fun getRepairTypeById1C(id1C: String): RepairType? {
        TODO("Not yet implemented")
    }
}