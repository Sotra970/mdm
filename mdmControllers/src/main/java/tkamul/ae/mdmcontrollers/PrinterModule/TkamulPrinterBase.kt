package tkamul.ae.mdmcontrollers.PrinterModule

import android.graphics.Bitmap
import android.os.Build
import tkamul.ae.mdmcontrollers.PrinterModule.core.LineUtils
import tkamul.ae.mdmcontrollers.PrinterModule.models.config.LinePrintingStatus
import tkamul.ae.mdmcontrollers.PrinterModule.models.config.DevicePrinterStatus
import tkamul.ae.mdmcontrollers.PrinterModule.models.config.PrinterType
import tkamul.ae.mdmcontrollers.PrinterModule.models.data.TkamulPrinterImageModel
import tkamul.ae.mdmcontrollers.PrinterModule.models.data.TkamulPrinterTextModel
import tkamul.ae.mdmcontrollers.PrinterModule.models.data.TkamulPrintingData
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
    private var printQueue: Queue<TkamulPrintingData> = ArrayDeque()

    /**
     * setup child printer
     */
    protected abstract fun setup()

    /**
     * check child  printer status before printing
     */
    public abstract fun getPrinterStatus() : DevicePrinterStatus

    /**
     * get child printer mac char by line
     */
    protected abstract fun getMaxCharCountInLine():Int

    /**
     * make child print text model
     */
    protected abstract fun PrintTextOnPaper(tkamulPrinterTextModel : TkamulPrinterTextModel) : LinePrintingStatus

    /**
     * make child print image model
     */
    protected abstract fun PrintImageOnPaper(tkamulPrinterImageModel : TkamulPrinterImageModel) : LinePrintingStatus

    /**
     * make child finish printing to save voltage & temperature
     */
    protected abstract fun endingPrinterChild()


    /**
     * add text with format : align , diricion size
     * @param scale : text size default : normal
     * @param align : text align : left
     * @param diriction : text diriction [RTL , LTR]  default : LTR
     */
    fun  addText(text: String, scale: PrinterTextScale = PrinterTextScale.normal, align: PrintTextAlign = PrintTextAlign.LEFT, diriction: PrintTextDirction = PrintTextDirction.LTR):TkamulPrinterBase{
      for ( child in LineUtils.convertTextToLine(text,getMaxCharCountInLine())){
          printQueue.add(TkamulPrinterTextModel().apply {
              this.scale = scale
              this.dirction = diriction
              this.text = child
              this.align = align
          })
      }
        return this
    }

    /**
     * add bitmab to queue
     */
    fun addImage(bitmap: Bitmap): TkamulPrinterBase {
        val model = TkamulPrinterImageModel(bitmap)
        printQueue.add(model)
        return this
    }

    /**
     * add bitmab by  eternal path to queue
     */
     fun addImage(path: String):TkamulPrinterBase {
        val model = TkamulPrinterImageModel(path)
        printQueue.add(model)
        return this
    }

    /**
     * add add Asterisks Line to queue
     */
     fun addAsterisksLine():TkamulPrinterBase{
        printQueue.add(
            TkamulPrinterTextModel(
                LineUtils.getLineOfChar(lineCount = getMaxCharCountInLine() , char = LineUtils.ASTERISK)
            )
        )
        return this
    }

    /**
     * add dash line to queue
     */
    fun addDashLine():TkamulPrinterBase{
        printQueue.add(
            TkamulPrinterTextModel(
                LineUtils.getLineOfChar(lineCount = getMaxCharCountInLine() , char = LineUtils.DASH)
            )
        )
        return this
    }

    /**
     * add empty line to queue
     */
    fun addEmptyLine():TkamulPrinterBase{
        printQueue.add(TkamulPrinterTextModel(LineUtils.EMPTY_LINE))
        return this
    }


    /**
     * print queue on paper or throw runtime error
     */
    @Throws(RuntimeException::class)
    fun printOnPaper(): LinePrintingStatus {
        var printiSgtatus =  LinePrintingStatus()
        setup()
        if (getPrinterStatus().isReady){
            // builder to loog queue text
            var logLines =StringBuilder()
            // loop on queue and call responsible method to print on paper to print text or image  :  printTkamulPrintingDataOnPaper()
            for (tkamulPrintingData in printQueue){
                printiSgtatus = printTkamulPrintingDataOnPaper(tkamulPrintingData,logLines)
                if (!printiSgtatus.printed){
                    Logger.logd("PrinterLines : $logLines")
                    // clear queue
                    printQueue.clear()
                    // throw the runtime error while printing
                    throw RuntimeException(printiSgtatus.errorMessage)
                    return printiSgtatus
                }
            }
            // log printed lines
            Logger.logd("PrinterLines : $logLines")
            // clear queue
            printQueue.clear()
            // ending printer to save voltage
            endingPrinterChild()
            // return success printing
            return printiSgtatus
        }else{
            // printer have an issue(out of paper or temperature ) before printing
            // throw exception and clear our queue
            val notReadyPrinterStatus = getPrinterStatus().status
            printQueue.clear()
            throw RuntimeException()
            return printiSgtatus.apply {
                errorMessage = notReadyPrinterStatus
            }
        }
    }


    /**
     * method to print on paper to print text or image
     * @return  LinePrintingStatus obj
     */
    private fun printTkamulPrintingDataOnPaper(tkamulPrintingData: TkamulPrintingData , logLines :StringBuilder) : LinePrintingStatus{
        var linePrintingStatus = LinePrintingStatus()
        when(tkamulPrintingData){
            // printing text
            is TkamulPrinterTextModel->{
                linePrintingStatus = PrintTextOnPaper(tkamulPrintingData)
                logLines.append(tkamulPrintingData.text).also {
                    it.append("/n")
                    it.append("LinePrintingStatus $linePrintingStatus")
                    it.append("/n")
                }
                return linePrintingStatus
            }
            //printing image
            is TkamulPrinterImageModel->{
                linePrintingStatus = PrintImageOnPaper(tkamulPrintingData)
                logLines.append("bitmap").also {
                    it.append("/n")
                    it.append("PrintingStatus $linePrintingStatus")
                    it.append("/n")
                }
                return linePrintingStatus
            }
        }
        return linePrintingStatus
    }


    companion object{
        /**
         * get printer type by device model
         */
        fun getPrinterType(): PrinterType {
            Logger.logd("model" + Build.MODEL)
            val model = Build.MODEL
            return when (Build.MODEL.trim { it <= ' ' }) {
                "MobiPrint" -> PrinterType.MOBIEWIRE
                "MP3" -> PrinterType.MOBIEWIRE
                "MobiPrint 4+" -> PrinterType.CSPRINTER
                "MP4" -> PrinterType.CSPRINTER
                "MP3_Plus" -> PrinterType.CSPRINTER
                else -> PrinterType.CSPRINTER
            }
        }
    }


}