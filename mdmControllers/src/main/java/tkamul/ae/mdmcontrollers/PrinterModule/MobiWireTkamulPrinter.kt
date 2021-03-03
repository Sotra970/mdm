package tkamul.ae.mdmcontrollers.PrinterModule

import com.mobiiot.androidqapi.api.Utils.Line
import com.nbbse.mobiprint3.Printer
import tkamul.ae.mdmcontrollers.PrinterModule.models.config.DevicePrinterStatus
import tkamul.ae.mdmcontrollers.PrinterModule.models.config.LinePrintingStatus
import tkamul.ae.mdmcontrollers.PrinterModule.models.data.TkamulPrinterImageModel
import tkamul.ae.mdmcontrollers.PrinterModule.models.data.TkamulPrinterTextModel
import tkamul.ae.mdmcontrollers.PrinterModule.models.textFormat.PrintTextDirction
import tkamul.ae.mdmcontrollers.PrinterModule.models.textFormat.PrinterTextScale

/**
 * Created by sotra@altakamul.tr on 3/1/2021.
 */
class MobiWireTkamulPrinter : TkamulPrinterBase() {

    private lateinit var mobiWirePrinter : Printer

    override fun setup() {
        mobiWirePrinter = Printer.getInstance()
    }
     fun getTextSize(scale : PrinterTextScale): Int {
       return when(scale){
            PrinterTextScale.large->  LARGE_TEXT
            PrinterTextScale.medium ->  MED_TEXT
            PrinterTextScale.normal -> NORMAL_TEXT
       }
    }

    override fun PrintTextOnPaper(tkamulPrinterTextModel: TkamulPrinterTextModel) : LinePrintingStatus{
        val isPrinted  = mobiWirePrinter.printText(tkamulPrinterTextModel.text , getTextSize(tkamulPrinterTextModel.scale) ,getR2LFLAG(tkamulPrinterTextModel.dirction))
        return  getLastLinePrintingStatus()
    }

    /**
     * unfotuntly we cant get last printed line status like CSPrinter
     * but we can save time and parent class logic with checking with device printer status instadeof returning always true and the printer have an issue
     */
    private fun getLastLinePrintingStatus(): LinePrintingStatus {
        val status = getPrinterStatus()
        if (status.isReady){
            return LinePrintingStatus(true , null)
        }else {
            return LinePrintingStatus(false , status.status)
        }
    }

    /**
     * get desired printing text direction
     */
    private fun getR2LFLAG(dirction: PrintTextDirction): Boolean {
        return when(dirction){
            PrintTextDirction.LTR->false
            PrintTextDirction.RTL->true
        }
    }


    /**
     *  {@inheritDoc}
     */
    override fun PrintImageOnPaper(tkamulPrinterImageModel: TkamulPrinterImageModel) : LinePrintingStatus{
        if (tkamulPrinterImageModel.path != null)
            mobiWirePrinter.printBitmap(tkamulPrinterImageModel.path)
        else if (tkamulPrinterImageModel.bitmap != null)
            mobiWirePrinter.printBitmap(
                tkamulPrinterImageModel.bitmap
        )
        return getLastLinePrintingStatus()
    }

    /**
     *  {@inheritDoc}
     */
    override fun endingPrinterChild() {
        mobiWirePrinter.printEndLine()
    }


    /**
     *  {@inheritDoc}
     * get mobiewire printer status for mp3 device type
     */
    override fun getPrinterStatus(): DevicePrinterStatus {
        return when (mobiWirePrinter.printerStatus) {
            Printer.PRINTER_STATUS_NO_PAPER -> {
                DevicePrinterStatus("no paper ", false)
            }
            Printer.PRINTER_STATUS_OVER_HEAT -> {
                DevicePrinterStatus("over heat ", false)
            }
            Printer.PRINTER_STATUS_GET_FAILED -> {
                DevicePrinterStatus("print fail", false)
            }
            Printer.PRINTER_STATUS_OK -> {
                DevicePrinterStatus("ready", true)
            }
            else ->  DevicePrinterStatus("unKnown", false)
        }
    }




    /**
     * @inheritDoc
     */
    override fun getMaxCharCountInLine(): Int {
        return  MAX_CHAR_COUNT
    }



    companion object {
        internal const val LARGE_TEXT = 2
        internal const val MED_TEXT = 1
        internal const val NORMAL_TEXT = 1
        internal const val MAX_CHAR_COUNT: Int = 21
        internal const val EMPTY_LINE: String = "\n"
    }
}