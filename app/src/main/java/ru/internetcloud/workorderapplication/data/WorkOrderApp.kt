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
        RemoteDepartmentRepositoryImpl.initialize(this)
        RemoteEmployeeRepositoryImpl.initialize(this)
        RemotePartnerRepositoryImpl.initialize(this)
        RemoteCarRepositoryImpl.initialize(this)
        RemoteWorkingHourRepositoryImpl.initialize(this)

        DbRepairTypeRepositoryImpl.initialize(this)
        DbCarJobRepositoryImpl.initialize(this)
        DbDepartmentRepositoryImpl.initialize(this)
        DbEmployeeRepositoryImpl.initialize(this)
        DbPartnerRepositoryImpl.initialize(this)
        DbCarRepositoryImpl.initialize(this)
        DbWorkingHourRepositoryImpl.initialize(this)

        DbWorkOrderRepositoryImpl.initialize(this)
    }
}
