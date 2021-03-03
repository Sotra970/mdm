package tkamul.ae.mdmcontrollers.PrinterModule

import android.graphics.Bitmap
import android.os.Build
import tkamul.ae.mdmcontrollers.PrinterModule.core.LineUtils
import tkamul.ae.mdmcontrollers.PrinterModule.models.config.PrinterStatus
import tkamul.ae.mdmcontrollers.PrinterModule.models.config.PrinterType
import tkamul.ae.mdmcontrollers.PrinterModule.models.data.TkamulPrinterImageModel
import tkamul.ae.mdmcontrollers.PrinterModule.models.data.TkamulPrinterTextModel
import tkamul.ae.mdmcontrollers.PrinterModule.models.textFormat.PrintTextAlign
import tkamul.ae.mdmcontrollers.PrinterModule.models.textFormat.PrintTextDirction
import tkamul.ae.mdmcontrollers.PrinterModule.models.textFormat.PrinterTextScale
import tkamul.ae.mdmcontrollers.domain.core.Logger
import java.lang.RuntimeException
import java.lang.StringBuilder
import java.util.*

/**
 * Created by sotra@altakamul.tr on 3/1/2021.
 */
abstract class TkamulPrinterBase {
    private var printQueue: Queue<Any> = ArrayDeque()

    protected abstract fun setup()
    protected abstract fun getPrinterStatus() : PrinterStatus
    protected abstract fun getMaxCharCountInLine():Int
    protected abstract fun PrintTextOnPaper(tkamulPrinterTextModel : TkamulPrinterTextModel)
    protected abstract fun PrintImageOnPaper(tkamulPrinterImageModel : TkamulPrinterImageModel)
    protected abstract fun endingPrinterChild()



    fun  addText(text: String, scale: PrinterTextScale = PrinterTextScale.normal, printerTextAlign: PrintTextAlign = PrintTextAlign.LEFT ,printerTextDiriction: PrintTextDirction = PrintTextDirction.LTR):TkamulPrinterBase{
      for ( child in LineUtils.convertTextToLine(text,getMaxCharCountInLine())){
          printQueue.add(TkamulPrinterTextModel().apply {
              this.scale = scale
              this.dirction = printerTextDiriction
              this.text = child
              this.align = printerTextAlign
          })
      }
        return this
    }

    fun addImage(bitmap: Bitmap): TkamulPrinterBase {
        val model = TkamulPrinterImageModel(bitmap)
        printQueue.add(model)
        return this
    }

     fun addImage(path: String):TkamulPrinterBase {
        val model = TkamulPrinterImageModel(path)
        printQueue.add(model)
        return this
    }

     fun addAsterisksLine():TkamulPrinterBase{
        printQueue.add(
            TkamulPrinterTextModel(
                LineUtils.getLineOfChar(lineCount = getMaxCharCountInLine() , char = LineUtils.ASTERISK)
            )
        )
        return this
    }
    fun addDashLine():TkamulPrinterBase{
        printQueue.add(
            TkamulPrinterTextModel(
                LineUtils.getLineOfChar(lineCount = getMaxCharCountInLine() , char = LineUtils.DASH)
            )
        )
        return this
    }
    fun addEmptyLine():TkamulPrinterBase{
        printQueue.add(TkamulPrinterTextModel(LineUtils.EMPTY_LINE))
        return this
    }



    @Throws(RuntimeException::class)
    fun printOnPaper(){
        setup()
        if (getPrinterStatus().isReady){
            // builder to loog queue text
            var logLines =StringBuilder()
            // loop on queue and call responsible method to print on paper
            for (tkamulPrintingData in printQueue){
                when(tkamulPrintingData){
                    // printing text
                    is TkamulPrinterTextModel->{
                        PrintTextOnPaper(tkamulPrintingData)
                        logLines.append(tkamulPrintingData.text).also {
                            it.append("/n")
                        }
                    }
                    //printing image
                    is TkamulPrinterImageModel->{
                        PrintImageOnPaper(tkamulPrintingData)
                        logLines.append("bitmap").also {
                            it.append("/n")
                        }
                    }
                }
            }
            Logger.logd("PrinterLines : $logLines")
            // clear queue
            printQueue.clear()
            // ending printer child ex: mobieprint
            endingPrinterChild()
        }else{
            // printer have an issue
            // throw exception and clear our queue
            printQueue.clear()
            throw RuntimeException(getPrinterStatus().status)
        }

    }


    companion object{
        fun getPrinterType(): PrinterType {
            Logger.logd("model" + Build.MODEL)
            val model = Build.MODEL
            return when (Build.MODEL.trim { it <= ' ' }) {
                "MobiPrint" -> PrinterType.MOBIEWIRE
                "MP3" -> PrinterType.MOBIEWIRE
                "MP4_Plus" -> PrinterType.CSPRINTER
                "MP4" -> PrinterType.CSPRINTER
                "MP3_Plus" -> PrinterType.CSPRINTER
                else -> PrinterType.CSPRINTER
            }
        }
    }


}