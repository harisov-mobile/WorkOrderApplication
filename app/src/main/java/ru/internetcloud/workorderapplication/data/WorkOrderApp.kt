package ru.internetcloud.workorderapplication.data

import android.app.Application
import ru.internetcloud.workorderapplication.data.repository.DatabaseWorkOrderRepositoryImpl

class WorkOrderApp: Application() {

    override fun onCreate() {
        super.onCreate()

        // инициализация синглтона-репозитория
        DatabaseWorkOrderRepositoryImpl.initialize(this)
    }
}