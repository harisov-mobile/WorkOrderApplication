package ru.internetcloud.workorderapplication.common.domain.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class OperationMode : Parcelable {
    NOTHING, // ""
    GET_CAR_JOB_LIST, // "Получение справочника Автоработы из 1С"
    GET_CAR_MODEL_LIST, // "Получение справочника Модели из 1С"
    PREPARE_CAR_JOB_LIST, // "Обработка справочника Автоработы"
    PREPARE_CAR_MODEL_LIST, // "Обработка справочника Модели"
    GET_DEPARTMENT_LIST, // "Получение справочника Цеха из 1С"
    PREPARE_DEPARTMENT_LIST, // "Обработка справочника Цеха"
    GET_EMPLOYEE_LIST, // "Получение справочника Сотрудники из 1С"
    PREPARE_EMPLOYEE_LIST, // "Обработка справочника Сотрудники"
    GET_PARTNER_LIST, // "Получение справочника Контрагенты из 1С"
    PREPARE_PARTNER_LIST, // "Обработка справочника Контрагенты"
    GET_CAR_LIST, // "Получение справочника СХТ из 1С"
    PREPARE_CAR_LIST, // "Обработка справочника СХТ"
    GET_WORKING_HOUR_LIST, // "Получение справочника Нормочасы из 1С"
    PREPARE_WORKING_HOUR_LIST, // Обработка справочника Нормочасы"
    GET_DEFAULT_WORK_ORDER_SETTINGS, // "Получение настроек заполнения из 1С"
    LOAD_REPAIR_TYPES, // "Получение справочника Виды ремонта из 1С"
    LOAD_WORK_ORDERS, // "Получение документов Заказ-наряд из 1С"
    UPLOAD_WORK_ORDERS // "Отправка документов Заказ-наряд в 1С"
}
