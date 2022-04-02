package ru.internetcloud.workorderapplication

import android.app.Application
import ru.internetcloud.workorderapplication.di.DaggerApplicationComponent

class WorkOrderApp : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}
