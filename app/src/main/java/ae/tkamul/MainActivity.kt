package ae.tkamul

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.mobiiot.androidqapi.api.Utils.PrinterServiceUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import tkamul.ae.mdmcontrollers.PrinterModule.TkamulPrinterFactory
import tkamul.ae.mdmcontrollers.PrinterModule.models.textFormat.PrintTextAlign
import tkamul.ae.mdmcontrollers.PrinterModule.models.textFormat.PrintTextDirction
import tkamul.ae.mdmcontrollers.PrinterModule.models.textFormat.PrinterTextScale
import tkamul.ae.mdmcontrollers.contollers.MDMControllers
import tkamul.ae.mdmcontrollers.domain.core.Config
import tkamul.ae.mdmcontrollers.domain.core.KeyStoreUtils
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    @Inject
    lateinit var mdmControllers: MDMControllers

    @Inject
    lateinit var keyStoreUtils: KeyStoreUtils


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindSerilaNumber()

    }

    private fun bindSerilaNumber() {
        mdmControllers.mdmInfoController.invoke {
            connection.text = it.deviceInfo.serial_number
        }
    }



    fun install(view:View){
        mdmControllers.invokeInternalInstallProcess(
                event = Config.Events.INSTALL_EVENT ,
                url = "https://cdn.appgain.io/docs/appgain/androidSDKtestapp/app-release.apk",
                packageName = "com.appgain.sdk.io"
        )
    }


    fun wifion(view: View) {
        mdmControllers.invokeInternalProcess(event = Config.Events.WIFI_EVENT_ON)
    }
    fun wifionOff(view: View) {
        mdmControllers.invokeInternalProcess(event = Config.Events.WIFI_EVENT_OFF)
    }

    fun dataon(view: View) {
        mdmControllers.invokeInternalProcess(event = Config.Events.DATA_EVENT_ON)
    }
    fun dataoff(view: View) {
        mdmControllers.invokeInternalProcess(event = Config.Events.DATA_EVENT_OFF)

    }
    fun bluetoothon(view: View) {
        mdmControllers.invokeInternalProcess(event = Config.Events.BLUETHOOTH_EVENT_ON)

    }
    fun bluetoothOff(view: View) {
        mdmControllers.invokeInternalProcess(event = Config.Events.BLUETHOOTH_EVENT_OFF)
    }
    fun nfcon(view: View) {
        mdmControllers.invokeInternalProcess(event = Config.Events.NFC_EVENT_ON)

    }
    fun nfcOff(view: View) {
        mdmControllers.invokeInternalProcess(event = Config.Events.NFC_EVENT_OFF)

    }
    fun reboot(view: View) {
        mdmControllers.invokeInternalProcess(event = Config.Events.REBOOT_EVENT)

    }
    fun poweroff(view: View) {
        mdmControllers.invokeInternalProcess(event = Config.Events.POWERR_OFF_EVENT)
    }
    fun location(view: View) {
    }

    fun locationoff(view: View) {
        mdmControllers.invokeInternalProcess(event = Config.Events.LOCATION_EVENT_OFF)
    }
    fun locationon(view: View) {
        mdmControllers.invokeInternalProcess(event = Config.Events.LOCATION_EVENT_ON)

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
        mdmControllers.invokeInternalPrintingProcess(event = Config.Events.PRINT_EVENT, printText = "controller test")

    }
}

