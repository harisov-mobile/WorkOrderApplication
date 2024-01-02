package ru.internetcloud.workorderapplication.navigationimpl.di

import androidx.navigation.NavController
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import ru.internetcloud.workorderapplication.login.presentation.navigation.LoginDirections
import ru.internetcloud.workorderapplication.navigationapi.NavigationApi
import ru.internetcloud.workorderapplication.navigationimpl.NavigationActivityProvider
import ru.internetcloud.workorderapplication.navigationimpl.navigationapis.LoginNavigationImpl
import ru.internetcloud.workorderapplication.navigationimpl.navigationapis.SynchroNavigationImpl
import ru.internetcloud.workorderapplication.navigationimpl.navigationapis.WorkOrderDetailNavigationImpl
import ru.internetcloud.workorderapplication.navigationimpl.navigationapis.WorkOrdersNavigationImpl
import ru.internetcloud.workorderapplication.synchro.presentation.navigation.SynchroDirections
import ru.internetcloud.workorderapplication.workorderdetail.presentation.navigation.WorkOrderDetailDirections
import ru.internetcloud.workorderapplication.workorders.presentation.navigation.WorkOrdersDirections

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {

    @Binds
    fun bindLoginNavigationApi(api: LoginNavigationImpl): NavigationApi<LoginDirections>

    @Binds
    fun bindSynchroNavigationApi(api: SynchroNavigationImpl): NavigationApi<SynchroDirections>

    @Binds
    fun bindWorkOrdersNavigationApi(api: WorkOrdersNavigationImpl): NavigationApi<WorkOrdersDirections>

    @Binds
    fun bindWorkOrderDetailNavigationApi(api: WorkOrderDetailNavigationImpl): NavigationApi<WorkOrderDetailDirections>

    companion object {

        @Singleton
        @Provides
        fun provideNavController(
            activityProvider: NavigationActivityProvider
        ): NavController = activityProvider.get()
            ?.getNavigationFragment()
            ?.navController
            ?: error("Do not make navigation calls while activity is not available")
    }
}
