package tkamul.ae.mdmcontrollers.domain.interactors.printer

import android.content.Context
import android.os.Build
import tkamul.ae.printer_factory.TkamulPrinterFactory
import tkamul.ae.printer_factory.models.config.DevicePrinterStatus
import tkamul.ae.printer_factory.models.config.LinePrintingStatus
import tkamul.ae.mdmcontrollers.domain.core.Config
import java.lang.RuntimeException
import javax.inject.Inject

/**
 * Created by sotra@altakamul.tr on 2/17/2021.
 */
class PrintInteractor @Inject constructor(
    var context: Context
){
    /**
     * print incoming text
     * return printing status
     */
    @Throws(RuntimeException::class)
     fun invoke(text : String , resultCallback : (tkamul.ae.printer_factory.models.config.LinePrintingStatus)->Unit){
        tkamul.ae.printer_factory.TkamulPrinterFactory.getTkamulPrinter(context)
            .setSeparateTextToLines( Build.MODEL== Config.MP4P_MODEL_NAME , 100)
            .addEmptyLine()
            .addEmptyLine()
            .addEmptyLine()
            .addText(text)
            .addEmptyLine()
            .addEmptyLine()
            .addEmptyLine()
            .printOnPaper(resultCallback)
    }
    @Throws(RuntimeException::class)
    fun  getPrinterStats(): tkamul.ae.printer_factory.models.config.DevicePrinterStatus {
        return tkamul.ae.printer_factory.TkamulPrinterFactory.getTkamulPrinter(context).getPrinterStatus()
    }
}