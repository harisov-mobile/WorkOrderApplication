package ru.internetcloud.workorderapplication.data

import android.app.Application
import ru.internetcloud.workorderapplication.data.repository.*
import ru.internetcloud.workorderapplication.data.repository.db.*
import ru.internetcloud.workorderapplication.data.repository.remote.*

class WorkOrderApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // инициализация синглтонов-репозиториев
        AuthRepositoryImpl.initialize(this)

        RemoteRepairTypeRepositoryImpl.initialize()
        RemoteCarJobRepositoryImpl.initialize()
        RemoteDepartmentRepositoryImpl.initialize()
        RemoteEmployeeRepositoryImpl.initialize()
        RemotePartnerRepositoryImpl.initialize()
        RemoteCarRepositoryImpl.initialize()
        RemoteWorkingHourRepositoryImpl.initialize()

        DbRepairTypeRepositoryImpl.initialize(this)
        DbCarJobRepositoryImpl.initialize(this)
        DbDepartmentRepositoryImpl.initialize(this)
        DbEmployeeRepositoryImpl.initialize(this)
        DbPartnerRepositoryImpl.initialize(this)
        DbCarRepositoryImpl.initialize(this)
        DbWorkingHourRepositoryImpl.initialize(this)

        SynchroRepositoryImpl.initialize(this)

        DbWorkOrderRepositoryImpl.initialize(this)
        DbDefaultWorkOrderSettingsRepositoryImpl.initialize(this)
    }
}
