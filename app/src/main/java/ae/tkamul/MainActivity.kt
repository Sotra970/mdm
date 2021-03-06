package ae.tkamul

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mobiiot.androidqapi.api.Utils.PrinterServiceUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import tkamul.ae.mdmcontrollers.PrinterModule.TkamulPrinterFactory
import tkamul.ae.mdmcontrollers.PrinterModule.models.textFormat.PrintTextAlign
import tkamul.ae.mdmcontrollers.PrinterModule.models.textFormat.PrintTextDirction
import tkamul.ae.mdmcontrollers.PrinterModule.models.textFormat.PrinterTextScale
import tkamul.ae.mdmcontrollers.contollers.MDMControllers
import tkamul.ae.mdmcontrollers.domain.core.Config
import tkamul.ae.mdmcontrollers.domain.core.Logger
import tkamul.ae.mdmcontrollers.domain.useCases.hardwareControllers.core.KeyStoreUtils
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
        PrinterServiceUtil.bindService(applicationContext)
        mdmControllers.mdmInfoController.invoke {
            connection.text = it.deviceInfo.serial_number
        }
        install()
    }

    /**
     * csAndroid.installApp(String path , String packageName);
     * csAndroid.installApp("/sdcard/Document/mobiiot.apk" , "com.mobiiot.test");
     */
    fun install(){
        mdmControllers.mdmInfoController.mobiMediaTechServiceUtil.getQInterface {
            val path: String = getExternalFilesDir(null).toString()
            Log.d("Files", "Path: $path")
            val directory = File("/storage/emulated/0/altkamul/apk/bagis.apk")
            Log.d("Files", "Path: ${directory.exists()}")

            it.removeApp("com.teknohub.bagis")
//            it.installApp("/storage/emulated/0/altkamul/apk/bagis.apk", "com.teknohub.bagis")
        }
    }

     /*fun downloadPdf(context:Context , url:String,  sem:String,  title:String,  branch:String): Long {
        val Download_Uri = Uri.parse(url);
        val request =  DownloadManager.Request(Download_Uri);

        //Restrict the types of networks over which this download may proceed.
         request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
         //Set whether this download may proceed over a roaming connection.
        request.setAllowedOverRoaming(false);
        //Set the title of this download, to be displayed in notifications (if enabled).
        request.setTitle("Downloading");
        //Set a description of this download, to be displayed in notifications (if enabled)
        request.setDescription("Downloading File");
        //Set the local destination for the downloaded file to a path within the application's external files directory
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title + "_" + branch + "_" + sem + "Year" + System.currentTimeMillis() + ".pdf");

        request. setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        //Enqueue a new download and same the referenceId
         val downloadManager = ContextCompat.getSystemService(context , Context.DOWNLOAD_SERVICE::class.java) as DownloadManager
         return downloadManager.enqueue(request);

    }*/


    // catches the onKeyDown button event
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Log.e("key down", "===$keyCode")
        return super.onKeyDown(keyCode, event)
    }

    // catches the onKeyUp button event
    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        Log.e("key up", "===$keyCode")
        return super.onKeyUp(keyCode, event)
    }


    fun wifion(view: View) {
        mdmControllers.invokProcess(event = Config.Events.WIFI_EVENT_ON)
    }
    fun wifionOff(view: View) {
        mdmControllers.invokProcess(event = Config.Events.WIFI_EVENT_OFF)
    }

    fun dataon(view: View) {
        mdmControllers.invokProcess(event = Config.Events.DATA_EVENT_ON)
    }
    fun dataoff(view: View) {
        mdmControllers.invokProcess(event = Config.Events.DATA_EVENT_OFF)

    }
    fun bluetoothon(view: View) {
        mdmControllers.invokProcess(event = Config.Events.BLUETHOOTH_EVENT_ON)

    }
    fun bluetoothOff(view: View) {
        mdmControllers.invokProcess(event = Config.Events.BLUETHOOTH_EVENT_OFF)
    }
    fun nfcon(view: View) {
        mdmControllers.invokProcess(event = Config.Events.NFC_EVENT_ON)

    }
    fun nfcOff(view: View) {
        mdmControllers.invokProcess(event = Config.Events.NFC_EVENT_OFF)

    }
    fun reboot(view: View) {
        mdmControllers.invokProcess(event = Config.Events.REBOOT_EVENT)

    }
    fun poweroff(view: View) {
        mdmControllers.invokProcess(event = Config.Events.POWERR_OFF_EVENT)
    }
    fun location(view: View) {
    }

    fun locationoff(view: View) {
        mdmControllers.invokProcess(event = Config.Events.LOCATION_EVENT_OFF)
    }
    fun locationon(view: View) {
        mdmControllers.invokProcess(event = Config.Events.LOCATION_EVENT_ON)

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
        mdmControllers.invokProcess(event = Config.Events.PRINT_EVENT, printText = "controller test")

    }
}

