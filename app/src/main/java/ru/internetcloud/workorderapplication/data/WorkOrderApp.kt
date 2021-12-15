package ru.internetcloud.workorderapplication.data

import android.app.Application
import ru.internetcloud.workorderapplication.data.repository.DbWorkOrderRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.AuthRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.DbRepairTypeRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.RemoteRepairTypeRepositoryImpl

class WorkOrderApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // инициализация синглтонов-репозиториев
        DbWorkOrderRepositoryImpl.initialize(this)
        RemoteRepairTypeRepositoryImpl.initialize(this)
        DbRepairTypeRepositoryImpl.initialize(this)
        AuthRepositoryImpl.initialize(this)
    }
}
