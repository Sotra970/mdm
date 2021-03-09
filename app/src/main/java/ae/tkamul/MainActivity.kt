package ae.tkamul

import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import tkamul.ae.mdmcontrollers.PrinterModule.TkamulPrinterFactory
import tkamul.ae.mdmcontrollers.PrinterModule.models.textFormat.PrintTextAlign
import tkamul.ae.mdmcontrollers.PrinterModule.models.textFormat.PrintTextDirction
import tkamul.ae.mdmcontrollers.PrinterModule.models.textFormat.PrinterTextScale
import tkamul.ae.mdmcontrollers.contollers.InternalEventExecutor
import tkamul.ae.mdmcontrollers.domain.core.Config
import tkamul.ae.mdmcontrollers.domain.core.Logger
import tkamul.ae.mdmcontrollers.domain.useCases.CSUseCases.MDMInfoUseCase
import javax.inject.Inject
import kotlin.concurrent.thread


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {



    @Inject
    lateinit var internalEventExecutorController: InternalEventExecutor

    @Inject
    lateinit var  mdmInfoUseCase: MDMInfoUseCase


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindSerilaNumber()
    }

    private fun bindSerilaNumber() {
        mdmInfoUseCase.invoke {
            connection.text = it.deviceInfo.serial_number
        }
    }

    fun unInstall(view: View){
        internalEventExecutorController.invokeInternalUnInstallProcess(
                event = Config.Events.UNINSTALL_EVENT,
                packageName = "com.appgain.sdk.io"
        )
    }


    fun install(view: View){
        internalEventExecutorController.invokeInternalInstallProcess(
                event = Config.Events.INSTALL_EVENT,
                url = "https://cdn.appgain.io/docs/appgain/androidSDKtestapp/app-release.apk",
                packageName = "com.appgain.sdk.io"
        )
    }

    fun wifion(view: View) {

        internalEventExecutorController.invokeInternalProcess(event = Config.Events.WIFI_EVENT_ON)
    }
    fun wifionOff(view: View) {
        internalEventExecutorController.invokeInternalProcess(event = Config.Events.WIFI_EVENT_OFF)
    }

    fun dataon(view: View) {
        internalEventExecutorController.invokeInternalProcess(event = Config.Events.DATA_EVENT_ON)
    }
    fun dataoff(view: View) {
        internalEventExecutorController.invokeInternalProcess(event = Config.Events.DATA_EVENT_OFF)

    }
    fun bluetoothon(view: View) {
        internalEventExecutorController.invokeInternalProcess(event = Config.Events.BLUETHOOTH_EVENT_ON)

    }
    fun bluetoothOff(view: View) {
        internalEventExecutorController.invokeInternalProcess(event = Config.Events.BLUETHOOTH_EVENT_OFF)
    }
    fun nfcon(view: View) {
        internalEventExecutorController.invokeInternalProcess(event = Config.Events.NFC_EVENT_ON)

    }
    fun nfcOff(view: View) {
        internalEventExecutorController.invokeInternalProcess(event = Config.Events.NFC_EVENT_OFF)

    }
    fun reboot(view: View) {
        internalEventExecutorController.invokeInternalProcess(event = Config.Events.REBOOT_EVENT)

    }
    fun poweroff(view: View) {
        internalEventExecutorController.invokeInternalProcess(event = Config.Events.POWERR_OFF_EVENT)
    }
    fun location(view: View) {
    }

    fun locationoff(view: View) {
        internalEventExecutorController.invokeInternalProcess(event = Config.Events.LOCATION_EVENT_OFF)
    }
    fun locationon(view: View) {
        internalEventExecutorController.invokeInternalProcess(event = Config.Events.LOCATION_EVENT_ON)
        mdmInfoUseCase.invoke {
            connection.text = it.locationStatus
        }

    }

    fun print(view: View) {
        // print module test
        TkamulPrinterFactory.getTkamulPrinter(this)
            .addDashLine()
            .addAsterisksLine()
            .addDashLine()
            .addText("start test")
            .addDashLine()
            .addAsterisksLine()
            .addDashLine()
             // testing sizes
            .addText("hi x2", PrinterTextScale.large)
            .addText("hi x1", PrinterTextScale.medium)
            .addText("hi x", PrinterTextScale.normal)
            .addDashLine()
            .addAsterisksLine()
            .addDashLine()
            //testing LTR
            .addText("hi LTR", diriction = PrintTextDirction.LTR)
            //testing RTL
            .addText("RTL مرحباً", diriction = PrintTextDirction.RTL)
            .addDashLine()
            .addAsterisksLine()
            .addDashLine()
             //Testing LTR align
            .addText("hi LTR LEFT")
            .addText("hi LTR RIGHT", align = PrintTextAlign.RIGHT)
            .addText("hi LTR CENTER", align = PrintTextAlign.CENTER)
            .addDashLine()
            .addAsterisksLine()
            .addDashLine()
            //testing RTL align
            .addText("RTL مرحباجهة اليسار", align = PrintTextAlign.LEFT, diriction = PrintTextDirction.RTL)
            .addText("RTL مرحباًجهة اليمين", align = PrintTextAlign.RIGHT, diriction = PrintTextDirction.RTL)
            .addText("RTL مرحباًبالمتصف", align = PrintTextAlign.CENTER, diriction = PrintTextDirction.RTL)
            .addDashLine()
            .addAsterisksLine()
            .addDashLine()
            // testing 42 char in 1 string which should be printed  on 2 lines
            .addText("123456789T" + "123456789T" + "123456789T" + "123456789T" + "12")
            .addDashLine()
            .addAsterisksLine()
            .addDashLine()
            .addText("end test")
            .addDashLine()
            .addAsterisksLine()
            .addDashLine()
            .printOnPaper()
//        controller test
        internalEventExecutorController.invokeInternalPrintingProcess(event = Config.Events.PRINT_EVENT, printText = "controller test")

    }
}

