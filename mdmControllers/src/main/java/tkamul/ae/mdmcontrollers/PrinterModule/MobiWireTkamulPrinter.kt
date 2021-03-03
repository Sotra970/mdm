package tkamul.ae.mdmcontrollers.PrinterModule

import com.nbbse.mobiprint3.Printer
import tkamul.ae.mdmcontrollers.PrinterModule.core.LineUtils
import tkamul.ae.mdmcontrollers.PrinterModule.models.config.PrinterStatus
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

    override fun PrintTextOnPaper(tkamulPrinterTextModel: TkamulPrinterTextModel) {
        mobiWirePrinter.printText(tkamulPrinterTextModel.text , getTextSize(tkamulPrinterTextModel.scale) ,getR2LFLAG(tkamulPrinterTextModel.dirction))
    }

    private fun getR2LFLAG(dirction: PrintTextDirction): Boolean {
        return when(dirction){
            PrintTextDirction.LTR->false
            PrintTextDirction.RTL->true
        }
    }

    override fun PrintImageOnPaper(tkamulPrinterImageModel: TkamulPrinterImageModel) {
        if (tkamulPrinterImageModel.path != null)
            mobiWirePrinter.printBitmap(tkamulPrinterImageModel.path)
        else if (tkamulPrinterImageModel.bitmap != null)
            mobiWirePrinter.printBitmap(
                tkamulPrinterImageModel.bitmap
        )
    }

    override fun endingPrinterChild() {
        mobiWirePrinter.printEndLine()
    }

    override fun getPrinterStatus(): PrinterStatus {
        return when (mobiWirePrinter.printerStatus) {
            Printer.PRINTER_STATUS_NO_PAPER -> {
                PrinterStatus("no paper ", false)
            }
            Printer.PRINTER_STATUS_OVER_HEAT -> {
                PrinterStatus("over heat ", false)
            }
            Printer.PRINTER_STATUS_GET_FAILED -> {
                PrinterStatus("print fail", false)
            }
            Printer.PRINTER_STATUS_OK -> {
                PrinterStatus("ready", true)
            }
            else ->  PrinterStatus("unKnown", false)
        }
    }




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