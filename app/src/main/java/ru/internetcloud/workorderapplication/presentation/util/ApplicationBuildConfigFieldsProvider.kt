package ru.internetcloud.workorderapplication.presentation.util

import javax.inject.Inject
import ru.internetcloud.workorderapplication.BuildConfig
import ru.internetcloud.workorderapplication.common.buildconfig.BuildConfigFields
import ru.internetcloud.workorderapplication.common.buildconfig.BuildConfigFieldsProvider

class ApplicationBuildConfigFieldsProvider @Inject constructor() : BuildConfigFieldsProvider {

    override fun get(): BuildConfigFields = BuildConfigFields(
        buildType = BuildConfig.BUILD_TYPE,
        versionCode = BuildConfig.VERSION_CODE,
        versionName = BuildConfig.VERSION_NAME
    )
}
