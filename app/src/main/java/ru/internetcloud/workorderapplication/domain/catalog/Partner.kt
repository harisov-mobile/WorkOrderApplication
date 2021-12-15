package ru.internetcloud.workorderapplication.domain.catalog

// спр-к Контрагенты
data class Partner(
    var id: String = "",
    var code1C: String = "",
    var name: String = "",
    var INN: String = "",
    var KPP: String = ""
)
