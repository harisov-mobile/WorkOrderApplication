package ru.internetcloud.workorderapplication.data

import android.app.Application
import ru.internetcloud.workorderapplication.data.repository.*

class WorkOrderApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // инициализация синглтонов-репозиториев
        AuthRepositoryImpl.initialize(this)

        RemoteRepairTypeRepositoryImpl.initialize(this)
        RemoteCarJobRepositoryImpl.initialize(this)

        DbRepairTypeRepositoryImpl.initialize(this)
        DbCarJobRepositoryImpl.initialize(this)

        DbWorkOrderRepositoryImpl.initialize(this)
    }
}
