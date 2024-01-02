package ru.internetcloud.workorderapplication.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ru.internetcloud.workorderapplication.WorkOrderApp
import ru.internetcloud.workorderapplication.common.buildconfig.BuildConfigFieldsProvider
import ru.internetcloud.workorderapplication.navigationimpl.NavigationActivityProvider
import ru.internetcloud.workorderapplication.presentation.util.ApplicationBuildConfigFieldsProvider

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    companion object {

        @Singleton
        @Provides
        internal fun provideBuildConfigFieldsProvider(
            buildConfigFieldsProvider: ApplicationBuildConfigFieldsProvider
        ): BuildConfigFieldsProvider = buildConfigFieldsProvider

        @Singleton
        @Provides
        fun provideNavigationActivityProvider(
            @ApplicationContext context: Context
        ): NavigationActivityProvider {
            return (context as WorkOrderApp).navigationActivityProvider
        }

    }
}
