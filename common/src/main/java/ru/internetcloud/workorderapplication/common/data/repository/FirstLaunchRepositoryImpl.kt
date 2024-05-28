package ru.internetcloud.workorderapplication.common.data.repository

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.data.storage.FirstLaunchSharedPreferencesStorage
import ru.internetcloud.workorderapplication.common.domain.common.FirstLaunchRepository

class FirstLaunchRepositoryImpl @Inject constructor(
    private val firstLaunchSharedPreferencesStorage: FirstLaunchSharedPreferencesStorage
) : FirstLaunchRepository {

    override fun isFirstLaunch(): Boolean {
        return firstLaunchSharedPreferencesStorage.isFirstLaunch()
    }

    override fun setFirstLaunchToFalse() {
        firstLaunchSharedPreferencesStorage.setFirstLaunchToFalse()
    }
}
