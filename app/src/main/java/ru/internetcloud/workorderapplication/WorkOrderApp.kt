package ru.internetcloud.workorderapplication

import android.app.Application
import ru.internetcloud.workorderapplication.data.repository.*
import ru.internetcloud.workorderapplication.data.repository.db.*
import ru.internetcloud.workorderapplication.data.repository.remote.*
import ru.internetcloud.workorderapplication.di.DaggerApplicationComponent

class WorkOrderApp : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()

//        // инициализация синглтонов-репозиториев
//        AuthRepositoryImpl.initialize(this)
//
    }
}
