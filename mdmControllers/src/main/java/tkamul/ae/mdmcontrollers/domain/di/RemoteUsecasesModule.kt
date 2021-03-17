package tkamul.ae.mdmcontrollers.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import tkamul.ae.mdmcontrollers.data.gateways.socketgateway.SocketApiClientImplementer
import tkamul.ae.mdmcontrollers.domain.interactors.remote.SocketRepo
import javax.inject.Singleton

/**
 * Created by sotra@altakamul.tr on 2/22/2021.
 */
@InstallIn(ApplicationComponent::class)
@Module
class RemoteUsecasesModule {

    @Provides
    @Singleton
    fun provideSocketController(socketApiClientImplementer: SocketApiClientImplementer): SocketRepo {
        return SocketRepo(
            socketApiClientImplementer
        )
    }

}