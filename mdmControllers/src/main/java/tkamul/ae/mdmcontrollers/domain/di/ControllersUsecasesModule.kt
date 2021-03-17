package tkamul.ae.mdmcontrollers.domain.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import tkamul.ae.mdmcontrollers.domain.interactors.printer.PrintInteractor
import tkamul.ae.mdmcontrollers.domain.interactors.CSUseCases.*
import tkamul.ae.mdmcontrollers.domain.interactors.CSUseCases.InstallApkInteractor
import tkamul.ae.mdmcontrollers.domain.interactors.broadcasting.ExecuteCommandInteractor
import tkamul.ae.mdmcontrollers.domain.interactors.notification.NotificationInteractor
import tkamul.ae.mdmcontrollers.service.MobiMediaTechServiceUtil
import javax.inject.Singleton

/**
 * Created by sotra@altakamul.tr on 2/17/2021.
 */
@InstallIn(ApplicationComponent::class)
@Module
class ControllersUsecasesModule {

    @Provides
    fun provideExecuteCommandUseCaseController(@ApplicationContext context:Context): ExecuteCommandInteractor {
        return ExecuteCommandInteractor(context)
    }

    @Provides
    fun providePrintController(@ApplicationContext context:Context): PrintInteractor {
        return PrintInteractor(context)
    }

    @Provides
    fun provideUnInstallApkUseCase(
            mobiMediaTechServiceUtil: MobiMediaTechServiceUtil
    ): UnInstallApkInteractor {
        return UnInstallApkInteractor(mobiMediaTechServiceUtil)
    }

    @Provides
    fun provideInstallApkUseCase(
            @ApplicationContext appContext: Context ,
            mobiMediaTechServiceUtil: MobiMediaTechServiceUtil
    ): InstallApkInteractor {
        return InstallApkInteractor(appContext,mobiMediaTechServiceUtil)
    }

    @Provides
    @Singleton
    fun provideMobileDataController(
        mobiMediaTechServiceUtil: MobiMediaTechServiceUtil
    ): MobileDataInteractor {
        return MobileDataInteractor(
            mobiMediaTechServiceUtil
        )
    }

    @Provides
    @Singleton
    fun providNotificationInteractor(
        @ApplicationContext appContext: Context
    ): NotificationInteractor {
        return NotificationInteractor(
            context = appContext
        )
    }


    @Provides
    @Singleton
    fun provideShutdownController(
        mobiMediaTechServiceUtil: MobiMediaTechServiceUtil
    ): ShutdownInteractor {
        return ShutdownInteractor(
            mobiMediaTechServiceUtil
        )
    }

    @Provides
    @Singleton
    fun provideRebootController(
        mobiMediaTechServiceUtil: MobiMediaTechServiceUtil
    ): RebootInteractor {
        return RebootInteractor(
            mobiMediaTechServiceUtil
        )
    }


    @Provides
    @Singleton
    fun provideNFCController(
        @ApplicationContext appContext: Context ,
        mobiMediaTechServiceUtil: MobiMediaTechServiceUtil
    ): NFCInteractor {
        return NFCInteractor(
            mobiMediaTechServiceUtil,
            appContext
        )
    }

    @Provides
    @Singleton
    fun provideMDMInfoController(
            @ApplicationContext appContext: Context,
            mobiMediaTechServiceUtil: MobiMediaTechServiceUtil,
            printInteractor: PrintInteractor
    ): MDMInfoInteractor {
        return MDMInfoInteractor(
            mobiMediaTechServiceUtil,
            printInteractor,
            appContext
        )
    }

    @Provides
    @Singleton
    fun provideLocationController(
        @ApplicationContext appContext: Context ,
        mobiMediaTechServiceUtil: MobiMediaTechServiceUtil
    ): LocationInteractor {
        return LocationInteractor(
            mobiMediaTechServiceUtil,
            appContext
        )
    }


    @Provides
    @Singleton
    fun provideBluetoothController(
        @ApplicationContext appContext: Context ,
        mobiMediaTechServiceUtil: MobiMediaTechServiceUtil
    ): BluetoothInteractor {
        return BluetoothInteractor(
            mobiMediaTechServiceUtil,
            appContext
        )
    }

    @Provides
    @Singleton
    fun provideWifiController(mobiMediaTechServiceUtil: MobiMediaTechServiceUtil): WifiInteractor {
        return WifiInteractor(
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