package tkamul.ae.mdmcontrollers.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import tkamul.ae.mdmcontrollers.data.gateways.socketgateway.SocketApiClientImplementer
import javax.inject.Singleton

/**
 * Created by sotra@altakamul.tr on 2/27/2021.
 */
@InstallIn(ApplicationComponent::class)
@Module
class RemoteModule {
    @Provides
    @Singleton
    fun provideSocketRepo(): SocketApiClientImplementer {
        return SocketApiClientImplementer()
    }
}