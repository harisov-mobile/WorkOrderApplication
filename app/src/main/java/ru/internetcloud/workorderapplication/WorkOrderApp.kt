package ru.internetcloud.workorderapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ru.internetcloud.workorderapplication.navigationimpl.NavigationActivityProvider

@HiltAndroidApp
class WorkOrderApp : Application() {

    var navigationActivityProvider = NavigationActivityProvider(this)
}

