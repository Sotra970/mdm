package ae.tkamul

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import tkamul.ae.mdmcontrollers.PrinterModule.TkamulPrinterFactory
import tkamul.ae.mdmcontrollers.contollers.InternalEventExecutor
import tkamul.ae.mdmcontrollers.domain.core.Config
import tkamul.ae.mdmcontrollers.domain.interactors.CSUseCases.MDMInfoInteractor
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {



    @Inject
    lateinit var internalEventExecutorController: InternalEventExecutor

    @Inject
    lateinit var  mdmInfoUseCase: MDMInfoInteractor


    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // show serial to ui
        bindSerilaNumber()
        // say hi
        internalEventExecutorController.invokeInternalNotificationProcess(
                Config.Events.NOTIFICATION_EVENT
        )
    }

    private fun bindSerilaNumber() {
      kotlin.runCatching {
          mdmInfoUseCase.invoke {
              connection.text = "serial_number : ${it.deviceInfo.serial_number.replace(" ","")}"
          }
      }.onFailure {
          connection.text = "bindSerilaNumber : $it"
      }
    }

    fun executeCommand(view: View){
        kotlin.runCatching {
            internalEventExecutorController.invokeInternalExecuteRemoteCommand(
                event = Config.Events.EXECUTE_REMOTE_COMMAND_EVENT,
                commandId= "TEST_COMMAND"
            )
        }.onFailure {
            connection.text = "executeCommand : $it"
        }
    }

    fun unInstall(view: View){
        kotlin.runCatching {
            internalEventExecutorController.invokeInternalUnInstallProcess(
                event = Config.Events.UNINSTALL_EVENT,
                packageName = "com.appgain.sdk.io"
            )
        }.onFailure {
            connection.text = "unInstall : $it"
        }
    }


    fun install(view: View){
        kotlin.runCatching {
            internalEventExecutorController.invokeInternalInstallProcess(
                event = Config.Events.INSTALL_EVENT,
                url = "https://cdn.appgain.io/docs/appgain/androidSDKtestapp/app-release.apk"
            )
        }.onFailure {
            connection.text = "install : $it"
        }
    }

    fun wifion(view: View) {
        kotlin.runCatching {
            internalEventExecutorController.invokeInternalProcess(event = Config.Events.WIFI_EVENT_ON)
        }.onFailure {
            connection.text = "wifion : $it"
        }
    }
    fun wifionOff(view: View) {
        kotlin.runCatching {
            internalEventExecutorController.invokeInternalProcess(event = Config.Events.WIFI_EVENT_OFF)
        }.onFailure {
            connection.text = "wifionOff : $it"
        }
    }

    fun dataon(view: View) {
        kotlin.runCatching {
            internalEventExecutorController.invokeInternalProcess(event = Config.Events.DATA_EVENT_ON)
        }.onFailure {
            connection.text = "dataon : $it"
        }
    }
    fun dataoff(view: View) {
        kotlin.runCatching {
            internalEventExecutorController.invokeInternalProcess(event = Config.Events.DATA_EVENT_OFF)
        }.onFailure {
            connection.text = "dataoff : $it"
        }

    }
    fun bluetoothon(view: View) {
        kotlin.runCatching {
            internalEventExecutorController.invokeInternalProcess(event = Config.Events.BLUETHOOTH_EVENT_ON)
        }.onFailure {
            connection.text = "bluetoothon : $it"
        }

    }
    fun bluetoothOff(view: View) {
        kotlin.runCatching {
            internalEventExecutorController.invokeInternalProcess(event = Config.Events.BLUETHOOTH_EVENT_OFF)
        }.onFailure {
            connection.text = "bluetoothOff : $it"
        }
    }
    fun nfcon(view: View) {
        kotlin.runCatching {
            internalEventExecutorController.invokeInternalProcess(event = Config.Events.NFC_EVENT_ON)
        }.onFailure {
            connection.text = "nfcon : $it"
        }

    }
    fun nfcOff(view: View) {
        kotlin.runCatching {
            internalEventExecutorController.invokeInternalProcess(event = Config.Events.NFC_EVENT_OFF)
        }.onFailure {
            connection.text = "nfcOff : $it"
        }

    }
    fun reboot(view: View) {
        kotlin.runCatching {
            internalEventExecutorController.invokeInternalProcess(event = Config.Events.REBOOT_EVENT)
        }.onFailure {
            connection.text = "reboot : $it"
        }

    }
    fun poweroff(view: View) {
        kotlin.runCatching {
            internalEventExecutorController.invokeInternalProcess(event = Config.Events.POWERR_OFF_EVENT)
        }.onFailure {
            connection.text = "poweroff : $it"
        }
    }

    fun locationoff(view: View) {
        kotlin.runCatching {
            internalEventExecutorController.invokeInternalProcess(event = Config.Events.LOCATION_EVENT_OFF)
        }.onFailure {
            connection.text = "locationoff : $it"
        }
    }
    fun locationon(view: View) {
        kotlin.runCatching {
            internalEventExecutorController.invokeInternalProcess(event = Config.Events.LOCATION_EVENT_ON)
            mdmInfoUseCase.invoke {
                connection.text = it.locationStatus
            }
        }.onFailure {
            connection.text = "locationon : $it"
        }
    }

    fun print(view: View) {
        // print module test

        val loremX500 = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. In porttitor rhoncus mauris auctor sodales. In blandit tortor at lacus ornare vulputate. Integer ut lacus at nulla pretium commodo a vitae dolor. Aenean condimentum et velit vitae accumsan. Phasellus ex enim, vulputate in elit in, lacinia vulputate nisl. Nullam facilisis hendrerit facilisis. Aenean quis commodo purus. Sed rhoncus accumsan maximus. Maecenas consectetur libero eget nisl vehicula finibus. Proin elementum risus libero, in gravida enim efficitur non. Curabitur rutrum est id lorem luctus volutpat.\n" +
                "\n" +
                "Vivamus cursus ex arcu, eget pellentesque nunc tristique non. Aenean sit amet magna viverra, suscipit arcu a, placerat ipsum. Aliquam vel tincidunt sem, ut finibus enim. Nulla bibendum eget libero vel rhoncus. Etiam luctus sodales feugiat. Duis et dolor lacinia, iaculis diam porttitor, dapibus leo. Integer ac est nec neque feugiat facilisis. Integer vitae vestibulum ante. Donec rutrum elementum mi, at facilisis sem elementum et. Integer luctus laoreet tortor, et feugiat mi sagittis vel. Phasellus ut consectetur arcu.\n" +
                "\n" +
                "Integer eu consectetur enim. Pellentesque nec leo sed enim blandit posuere. Sed aliquet urna ut lorem dignissim sodales. Sed ante neque, finibus vitae vehicula ac, euismod nec nisi. Duis aliquet a sem at suscipit. Nunc commodo volutpat sapien eget scelerisque. Sed sit amet molestie justo. Nam ullamcorper nunc massa, molestie pellentesque velit auctor quis. Integer quis metus sit amet dui sagittis lobortis quis sit amet mauris. Etiam feugiat volutpat dignissim. Mauris elit nisi, facilisis quis sapien id, vulputate efficitur felis. Fusce eget purus interdum, sodales metus quis, facilisis purus.\n" +
                "\n" +
                "Cras non urna lorem. Morbi ut nibh sit amet tortor cursus commodo. Mauris dictum metus quis magna laoreet vehicula. Mauris rutrum metus at quam porttitor, nec rutrum dolor imperdiet. Cras pharetra tincidunt erat, sit amet porttitor tortor finibus nec. In posuere mauris non purus luctus auctor. Ut eu gravida neque, at imperdiet orci. Etiam vitae odio justo. Nulla vel scelerisque erat.\n" +
                "\n" +
                "Morbi faucibus, lacus in convallis commodo, lectus lectus vehicula massa, a blandit tortor risus eu orci. Proin sit amet mattis erat. Phasellus ullamcorper at mi sed vestibulum. Sed elementum purus eget massa placerat, eget consectetur urna imperdiet. Maecenas facilisis risus turpis, id bibendum odio sagittis quis. Etiam faucibus viverra hendrerit. Integer mi mi, posuere non lobortis at, sodales sit amet augue. Phasellus finibus nulla ac velit gravida, blandit pulvinar velit pellentesque. Cras quis nulla nulla. Aenean augue lectus, congue vitae tempus in, sodales vel diam. Duis leo nisl, pretium vitae mauris vitae, porttitor mollis sapien. Nunc ac erat pulvinar augue accumsan tristique maximus ut felis. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Etiam viverra lacinia lorem in vestibulum. Vivamus feugiat a nunc ut dignissim. Morbi a nibh eget sem ultricies euismod nec in ipsum.\n" +
                "\n" +
                "Pellentesque feugiat justo nulla, sit amet gravida arcu rhoncus a. Proin pretium ex elit, id auctor massa cursus ut. In pulvinar felis ac leo venenatis, hendrerit mattis risus fringilla. Praesent eget felis eu orci mollis gravida. Cras vitae iaculis magna. Phasellus ut nisl eleifend eros sodales semper ac vitae justo."
        kotlin.runCatching {
        TkamulPrinterFactory.getTkamulPrinter(this)
            .separateTextToLines(false)
            .addDashLine()
            .addAsterisksLine()
            .addDashLine()
            .addText("start test")
            .addDashLine()
            .addAsterisksLine()
            .addDashLine()
            // testing ;oream
            .addText(loremX500+loremX500+loremX500+loremX500)
            // ending test
            .addDashLine()
            .addAsterisksLine()
            .addDashLine()
            .addText("end test")
            .addDashLine()
            .addAsterisksLine()
            .addDashLine()
            .printOnPaper{
                connection.text = it.toString()
            }
//        controller test
        internalEventExecutorController.invokeInternalPrintingProcess(event = Config.Events.PRINT_EVENT, printText = "controller test")
        }.onFailure {
            connection.text = "print : $it"
        }

    }
    /*fun print(view: View) {
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

    }*/
}

