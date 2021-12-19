package ru.internetcloud.workorderapplication.domain.repository

interface LoadWorkOrderRepository {
    suspend fun loadWorkOrderList(): Boolean
}