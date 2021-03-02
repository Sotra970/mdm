package tkamul.ae.mdmcontrollers.domain.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import tkamul.ae.mdmcontrollers.domain.useCases.hardwareControllers.*
import tkamul.ae.mdmcontrollers.domain.useCases.hardwareControllers.core.KeyStoreUtils
import tkamul.ae.mdmcontrollers.service.MobiMediaTechServiceUtil
import javax.inject.Singleton

/**
 * Created by sotra@altakamul.tr on 2/17/2021.
 */
@InstallIn(ApplicationComponent::class)
@Module
class ControllersModule {


    @Provides
    @Singleton
    fun provideMobileDataController(
        mobiMediaTechServiceUtil: MobiMediaTechServiceUtil
    ): MobileDataUseCase {
        return MobileDataUseCase(
            mobiMediaTechServiceUtil
        )
    }

    @Provides
    @Singleton
    fun provideKeyStoreUtils(
        @ApplicationContext appContext: Context ,
        mobiMediaTechServiceUtil: MobiMediaTechServiceUtil
    ): KeyStoreUtils {
        return KeyStoreUtils(
            mobiMediaTechServiceUtil= mobiMediaTechServiceUtil,
            context = appContext
        )
    }


    @Provides
    @Singleton
    fun provideShutdownController(
        mobiMediaTechServiceUtil: MobiMediaTechServiceUtil
    ): ShutdownUseCase {
        return ShutdownUseCase(
            mobiMediaTechServiceUtil
        )
    }

    @Provides
    @Singleton
    fun provideRebootController(
        mobiMediaTechServiceUtil: MobiMediaTechServiceUtil
    ): RebootUseCase {
        return RebootUseCase(
            mobiMediaTechServiceUtil
        )
    }


    @Provides
    @Singleton
    fun provideNFCController(
        @ApplicationContext appContext: Context ,
        mobiMediaTechServiceUtil: MobiMediaTechServiceUtil
    ): NFCUseCase {
        return NFCUseCase(
            mobiMediaTechServiceUtil,
            appContext
        )
    }

    @Provides
    @Singleton
    fun provideMDMInfoController(
        @ApplicationContext appContext: Context ,
        mobiMediaTechServiceUtil: MobiMediaTechServiceUtil
    ): MDMInfoUseCase {
        return MDMInfoUseCase(
            mobiMediaTechServiceUtil,
            appContext
        )
    }

    @Provides
    @Singleton
    fun provideLocationController(
        @ApplicationContext appContext: Context ,
        mobiMediaTechServiceUtil: MobiMediaTechServiceUtil
    ): LocationUseCase {
        return LocationUseCase(
            mobiMediaTechServiceUtil,
            appContext
        )
    }


    @Provides
    @Singleton
    fun provideBluetoothController(
        @ApplicationContext appContext: Context ,
        mobiMediaTechServiceUtil: MobiMediaTechServiceUtil
    ): BluetoothUseCase {
        return BluetoothUseCase(
            mobiMediaTechServiceUtil,
            appContext
        )
    }

    @Provides
    @Singleton
    fun provideWifiController(mobiMediaTechServiceUtil: MobiMediaTechServiceUtil): WifiUseCase {
        return WifiUseCase(
            mobiMediaTechServiceUtil
        )
    }


    // provide mobie media service which bind automatically on init
    @Provides
    @Singleton
    fun provideMobiInterface(@ApplicationContext appContext: Context) : MobiMediaTechServiceUtil {
        return MobiMediaTechServiceUtil(
            appContext
        )
    }
}