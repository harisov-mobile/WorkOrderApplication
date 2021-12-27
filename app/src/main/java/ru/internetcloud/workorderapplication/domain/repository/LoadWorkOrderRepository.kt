package ru.internetcloud.workorderapplication.domain.repository

import ru.internetcloud.workorderapplication.domain.common.FunctionResult

interface LoadWorkOrderRepository {
    suspend fun loadWorkOrderList(): Boolean // из сервера 1С

    suspend fun uploadWorkOrderList(): FunctionResult // на сервер 1С выгрузить
}
