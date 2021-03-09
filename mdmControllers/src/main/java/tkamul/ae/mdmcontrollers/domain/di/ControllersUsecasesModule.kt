package tkamul.ae.mdmcontrollers.domain.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import tkamul.ae.mdmcontrollers.domain.useCases.CSUseCases.PrintUseCase
import tkamul.ae.mdmcontrollers.domain.useCases.CSUseCases.*
import tkamul.ae.mdmcontrollers.domain.core.KeyStoreUtils
import tkamul.ae.mdmcontrollers.domain.useCases.CSUseCases.InstallApkUsecase
import tkamul.ae.mdmcontrollers.service.MobiMediaTechServiceUtil
import javax.inject.Singleton

/**
 * Created by sotra@altakamul.tr on 2/17/2021.
 */
@InstallIn(ApplicationComponent::class)
@Module
class ControllersUsecasesModule {

    @Provides
    fun providePrintController(@ApplicationContext context:Context): PrintUseCase {
        return PrintUseCase(context)
    }

    @Provides
    fun provideUnInstallApkUseCase(
            mobiMediaTechServiceUtil: MobiMediaTechServiceUtil
    ): UnInstallApkUsecase {
        return UnInstallApkUsecase(mobiMediaTechServiceUtil)
    }

    @Provides
    fun provideInstallApkUseCase(
            @ApplicationContext appContext: Context ,
            mobiMediaTechServiceUtil: MobiMediaTechServiceUtil
    ): InstallApkUsecase {
        return InstallApkUsecase(appContext,mobiMediaTechServiceUtil)
    }

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
        mobiMediaTechServiceUtil: MobiMediaTechServiceUtil,
        printUseCase: PrintUseCase
    ): MDMInfoUseCase {
        return MDMInfoUseCase(
            mobiMediaTechServiceUtil,
            printUseCase,
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