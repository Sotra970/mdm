package ae.tkamul

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import tkamul.ae.mdmcontrollers.PrinterModule.TkamulPrinterFactory
import tkamul.ae.mdmcontrollers.PrinterModule.models.textFormat.PrinterTextAlign
import tkamul.ae.mdmcontrollers.PrinterModule.models.textFormat.PrinterTextScale
import tkamul.ae.mdmcontrollers.contollers.MDMControllers
import tkamul.ae.mdmcontrollers.domain.core.Config
import tkamul.ae.mdmcontrollers.domain.useCases.hardwareControllers.core.KeyStoreUtils
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
//        keyStoreUtils.addKeyStore()
        mdmControllers.mdmInfoController.invoke {
            connection.text = it.deviceInfo.serial_number
        }
    }



    fun wifion(view: View) {
/*        TkamulPrinterFactory.getTkamulPrinter(baseContext)
            .addText("hi")
            .addText("hi x2" , PrinterTextScale.large)
            .addText("hi x1" , PrinterTextScale.medium)
            .addText("hi x" , PrinterTextScale.normal)
            .addDashLine()
            .addText("hi LTR" , printerTextAlign = PrinterTextAlign.left)
            .addText("hi RTL" , printerTextAlign = PrinterTextAlign.right)
            .addText("hi center" , printerTextAlign = PrinterTextAlign.center)
            .addAsterisksLine()*/
//            .printOnPaper()
        mdmControllers.invokProcess(event = Config.Events.WIFI_EVENT_ON )
    }
    fun wifionOff(view: View) {
        mdmControllers.invokProcess(event = Config.Events.WIFI_EVENT_OFF )
    }

    fun dataon(view: View) {
        mdmControllers.invokProcess(event = Config.Events.DATA_EVENT_ON )
    }
    fun dataoff(view: View) {
        mdmControllers.invokProcess(event = Config.Events.DATA_EVENT_OFF )

    }
    fun bluetoothon(view: View) {
        mdmControllers.invokProcess(event = Config.Events.BLUETHOOTH_EVENT_ON )

    }
    fun bluetoothOff(view: View) {
        mdmControllers.invokProcess(event = Config.Events.BLUETHOOTH_EVENT_OFF )
    }
    fun nfcon(view: View) {
        mdmControllers.invokProcess(event = Config.Events.NFC_EVENT_ON )

    }
    fun nfcOff(view: View) {
        mdmControllers.invokProcess(event = Config.Events.NFC_EVENT_OFF )

    }
    fun reboot(view: View) {
        mdmControllers.invokProcess(event = Config.Events.REBOOT_EVENT )

    }
    fun poweroff(view: View) {
        mdmControllers.invokProcess(event = Config.Events.POWERR_OFF_EVENT )
    }
    fun location(view: View) {
    }

    fun locationoff(view: View) {
        mdmControllers.invokProcess(event = Config.Events.LOCATION_EVENT_OFF )
    }
    fun locationon(view: View) {
        mdmControllers.invokProcess(event = Config.Events.LOCATION_EVENT_ON )

    }
}

