package ru.internetcloud.workorderapplication.common.domain.common

interface FirstLaunchRepository {

    fun isFirstLaunch(): Boolean

    fun setFirstLaunchToFalse()
}
