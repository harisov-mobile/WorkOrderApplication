package ru.internetcloud.workorderapplication.data

import android.app.Application
import ru.internetcloud.workorderapplication.data.repository.DatabaseWorkOrderRepositoryImpl
import ru.internetcloud.workorderapplication.data.repository.RemoteRepairTypeRepositoryImpl

class WorkOrderApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // инициализация синглтонов-репозиториев
        DatabaseWorkOrderRepositoryImpl.initialize(this)
        RemoteRepairTypeRepositoryImpl.initialize(this)
    }
}
