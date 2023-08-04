package ru.internetcloud.workorderapplication.domain.common

interface FirstLaunchRepository {

    fun isFirstLaunch(): Boolean

    fun setFirstLaunchToFalse()
}
