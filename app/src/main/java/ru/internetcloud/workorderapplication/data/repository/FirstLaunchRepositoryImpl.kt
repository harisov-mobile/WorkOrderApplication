package ru.internetcloud.workorderapplication.data.repository

import javax.inject.Inject
import ru.internetcloud.workorderapplication.data.storage.FirstLaunchSharedPreferencesStorage
import ru.internetcloud.workorderapplication.domain.common.FirstLaunchRepository

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
