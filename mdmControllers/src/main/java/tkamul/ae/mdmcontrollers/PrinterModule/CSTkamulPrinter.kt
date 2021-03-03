package tkamul.ae.mdmcontrollers.PrinterModule

import android.content.Context
import com.mobiiot.androidqapi.api.CsPrinter
import com.mobiiot.androidqapi.api.Utils.PrinterServiceUtil
import tkamul.ae.mdmcontrollers.PrinterModule.models.config.PrinterStatus
import tkamul.ae.mdmcontrollers.PrinterModule.models.data.TkamulPrinterImageModel
import tkamul.ae.mdmcontrollers.PrinterModule.models.data.TkamulPrinterTextModel
import tkamul.ae.mdmcontrollers.PrinterModule.models.textFormat.PrintTextAlign
import tkamul.ae.mdmcontrollers.PrinterModule.models.textFormat.PrintTextDirction
import tkamul.ae.mdmcontrollers.PrinterModule.models.textFormat.PrinterTextScale
import tkamul.ae.mdmcontrollers.domain.core.Logger

/**
 * Created by sotra@altakamul.tr on 3/2/2021.
 */
class CSTkamulPrinter( val context: Context) : TkamulPrinterBase() {


    override fun setup() {
        PrinterServiceUtil.bindService(context)
//        ServiceUtil.bindRemoteService(context)

    }

    override fun getPrinterStatus(): PrinterStatus {
        val printerStatus = CsPrinter.getPrinterStatus()
        Logger.logd("printerStatus $printerStatus")
        if (printerStatus != 1 && printerStatus != 0 && printerStatus != 16 && printerStatus != 17) {
            return PrinterStatus("The printer status is KO, you can restart the device!" , false)
        } else if (printerStatus == 1){
            return PrinterStatus("ready" , true)
        } else {
            return PrinterStatus("ready" , true)
        }
    }


    override fun getMaxCharCountInLine(): Int {
       return MAX_CHAR_COUNT
    }


     fun getTextSize(scale: PrinterTextScale): Int {
        return when(scale){
            PrinterTextScale.large -> LARGE_TEXT
            PrinterTextScale.medium -> MED_TEXT
            PrinterTextScale.normal -> NORMAL_TEXT
        }
    }

    /**
     * no documentation yet for CsPrinter.printText_FullParam
     * TODO("make keys for text font , alignment , bold , underline)
     */
    override fun PrintTextOnPaper(tkamulPrinterTextModel: TkamulPrinterTextModel) {
        CsPrinter.printText_FullParam(
            tkamulPrinterTextModel.text ,
            getTextSize(tkamulPrinterTextModel.scale),
            getTextDiriction(tkamulPrinterTextModel.dirction),
            1, getTexAlign(tkamulPrinterTextModel.align), false, false
        )
    }

    private fun getTexAlign(align: PrintTextAlign): Int {
        return when(align){
            PrintTextAlign.LEFT -> ALIGN_LEFT
            PrintTextAlign.RIGHT -> ALIGN_RIGHT
            PrintTextAlign.CENTER -> ALIGN_CENTER
        }
    }

    private fun getTextDiriction(dirction: PrintTextDirction): Int {
        when(dirction){
            PrintTextDirction.LTR -> return LTR
            PrintTextDirction.RTL -> return RTL
        }
    }

    override fun PrintImageOnPaper(tkamulPrinterImageModel: TkamulPrinterImageModel) {
        if (tkamulPrinterImageModel.path != null)
            CsPrinter.printBitmap(tkamulPrinterImageModel.path)
        else if (tkamulPrinterImageModel.bitmap != null)
            CsPrinter.printBitmap(tkamulPrinterImageModel.bitmap)
    }

    override fun endingPrinterChild() {
        CsPrinter.printEndLine()
    }

    companion object {
        internal const val LARGE_TEXT = 3
        internal const val MED_TEXT = 2
        internal const val NORMAL_TEXT = 1
        internal const val MAX_CHAR_COUNT: Int = 21
        internal const val LTR: Int = 0
        internal const val RTL: Int = 1
        internal  const val ALIGN_LEFT = 0
        internal  const val ALIGN_RIGHT = 2
        internal  const val ALIGN_CENTER = 1
    }
}