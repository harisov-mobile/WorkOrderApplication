package ru.internetcloud.workorderapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ru.internetcloud.workorderapplication.navigationimpl.NavigationActivityProvider

@HiltAndroidApp
class WorkOrderApp : Application() {

    val navigationActivityProvider = NavigationActivityProvider(this)
}

// ToDo - пагинацию сделать из Room
