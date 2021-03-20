package tkamul.ae.mdmcontrollers.PrinterModule

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.UiThread
import kotlinx.coroutines.*
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
import tkamul.ae.mdmcontrollers.domain.core.Config
import tkamul.ae.mdmcontrollers.domain.core.Logger
import java.lang.RuntimeException
import java.lang.StringBuilder
import java.util.*

/**
 * Created by sotra@altakamul.tr on 3/1/2021.
 */
abstract class TkamulPrinterBase {

    private val parentJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + parentJob)

    private var printQueue: Queue<TkamulPrintingData> = ArrayDeque()
    private  var  isSeparateTextToLinesEnabled = true
    private  var  combineLineCount = 100
    /**
     * setup child printer
     */
    protected abstract fun setup(onReady:()->Unit , onError:()->Unit)

    /**
     * get printer child status before printing
     */
    public abstract fun getPrinterStatus() : DevicePrinterStatus

    /**
     * get printer child  max char by line
     */
    protected abstract fun getMaxCharCountInLine():Int

    /**
     * make printer child  print text model
     */
    @UiThread
    protected abstract fun PrintTextOnPaper(tkamulPrinterTextModel : TkamulPrinterTextModel) : LinePrintingStatus

    /**
     * make printer child  print image model
     */
    @UiThread
    protected abstract fun PrintImageOnPaper(tkamulPrinterImageModel : TkamulPrinterImageModel) : LinePrintingStatus

    /**
     * make printer child  finish printing to save voltage & temperature
     */
    protected abstract fun endingPrinterChild()

    /**
     *  enable text separation to lines and set combineLineCount value
     */
    fun setSeparateTextToLines(enable : Boolean , combineLineCount:Int = 100) : TkamulPrinterBase {
        isSeparateTextToLinesEnabled = enable
        this.combineLineCount = combineLineCount
        return this
    }
    fun enableSeparateTextToLines(combineLineCount : Int) : TkamulPrinterBase {
        isSeparateTextToLinesEnabled = true
        this.combineLineCount = combineLineCount
        return this
    }
    fun disableSeparateTextToLines() : TkamulPrinterBase {
        isSeparateTextToLinesEnabled = false
        this.combineLineCount = 1
        return this
    }
    /**
     * add text with format : align , diricion size
     * @param scale : text size default : normal
     * @param align : text align : left
     * @param diriction : text diriction [RTL , LTR]  default : LTR
     */
    fun  addText(text: String, scale: PrinterTextScale = PrinterTextScale.normal, align: PrintTextAlign = PrintTextAlign.LEFT, diriction: PrintTextDirction = PrintTextDirction.LTR):TkamulPrinterBase{
        if (isSeparateTextToLinesEnabled){
            val lineList = LineUtils.convertTextToLine(text,getMaxCharCountInLine()*combineLineCount)
            for ( child in lineList ){
                printQueue.add(TkamulPrinterTextModel().apply {
                    this.scale = scale
                    this.dirction = diriction
                    this.text = child
                    this.align = align
                })
            }
        }else{
            printQueue.add(TkamulPrinterTextModel().apply {
                this.scale = scale
                this.dirction = diriction
                this.text = text
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
     * @return last printed line status
     */
    private  var retryCount = 0
    @Throws(RuntimeException::class)
     fun printOnPaper(result : (LinePrintingStatus)->Unit){
        var disconnectShouldWait = false
        // printing service have leaks on mp4+ so you have to wail binding service and dismiss you outgoing job when service disconnected
        var printingJob : Job? = null
        setup({
                printingJob = coroutineScope.async(Dispatchers.Main){
                    // printing service is ready now
                    val printerStatus = getPrinterStatus()
                    if (printerStatus.isReady){
                        // print and return last printed line status
                         result(processQueue().await())
                    }else{
                       if (printerStatus.status!=null && printerStatus.status!!.contains("unknown",true) && retryCount < 4 ){
                           disconnectShouldWait = true
                           retryCount+=1
                           //bnhet el tab3a
                           endingPrinterChild()
                           delay(11*1000)
                           // recall this function and return
                           printOnPaper(result)
                       }else{
                           // printer not ready and ether out of voltage or out of paper or both of them
                           // clear our queue
                           printQueue.clear()
                           retryCount=0
                           // retun error to function consumer
                           result(LinePrintingStatus().apply {
                               errorMessage = getPrinterStatus().status
                           })
                       }
                    }
                }
              },{
               if (!disconnectShouldWait){
                   // dismiss outgoing printing job
                printingJob?.cancel()
                   //clear our queue
                   result(LinePrintingStatus().apply {
                       errorMessage = "service is not ready , contact support "
                   })
               }
        })
    }
    // loop on queue
    // call responsible method to print on paper to print text or image  :  printTkamulPrintingDataOnPaper()
    // log printed lines
    // clear queue
    // finish printer
    private suspend fun processQueue(): Deferred<LinePrintingStatus> = coroutineScope.async(Dispatchers.Default){
        // builder to loog queue text
        var logLines =StringBuilder()
        // get last printed line status
        var lastPrintedLineStatus =  LinePrintingStatus()
        for (tkamulPrintingData in printQueue){
            lastPrintedLineStatus = printTkamulPrintingDataOnPaper(tkamulPrintingData,logLines).await()
            if (!lastPrintedLineStatus.printed){
                Logger.logd("PrinterLines : $logLines")
                // clear queue
                printQueue.clear()
                return@async lastPrintedLineStatus
            }
        }
        // log printed lines
        Logger.logd("PrinterLines : $logLines")
        // clear queue
        clearAndFinish()
        // return last printed line status
        return@async lastPrintedLineStatus
    }

    private fun clearAndFinish() = coroutineScope.launch(Dispatchers.Main){
        // clear queue
        printQueue.clear()
        // ending printer to save voltage
        endingPrinterChild()
    }


    /**
     * method to print on paper to print text or image
     * @return  LinePrintingStatus obj
     */
    @UiThread
    private suspend fun printTkamulPrintingDataOnPaper(tkamulPrintingData: TkamulPrintingData, logLines :StringBuilder) : Deferred<LinePrintingStatus> = coroutineScope.async(Dispatchers.Main){
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
                return@async linePrintingStatus
            }
            //printing image
            is TkamulPrinterImageModel->{
                linePrintingStatus = PrintImageOnPaper(tkamulPrintingData)
                logLines.append("bitmap").also {
                    it.append("/n")
                    it.append("PrintingStatus $linePrintingStatus")
                    it.append("/n")
                }
                return@async linePrintingStatus
            }
        }
        return@async linePrintingStatus
    }


    companion object{
        /**
         * get printer type by device model
         */
        fun getPrinterType(): PrinterType {
            Logger.logd("model" + Build.MODEL)
            val model = Build.MODEL
            return when (Build.MODEL.trim { it <= ' ' }) {
                Config.MP3_MODEL_NAME -> PrinterType.MOBIEWIRE
                Config.MP4P_MODEL_NAME -> PrinterType.CSPRINTER
                Config.MP4_MODEL_NAME -> PrinterType.CSPRINTER
                Config.MP3P_MODEL_NAME -> PrinterType.CSPRINTER
                else -> PrinterType.CSPRINTER
            }
        }
    }


}