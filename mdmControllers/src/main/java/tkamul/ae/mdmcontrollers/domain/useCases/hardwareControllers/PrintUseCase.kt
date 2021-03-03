package tkamul.ae.mdmcontrollers.domain.useCases.hardwareControllers

import android.content.Context
import tkamul.ae.mdmcontrollers.PrinterModule.TkamulPrinterFactory
import tkamul.ae.mdmcontrollers.PrinterModule.models.config.DevicePrinterStatus
import tkamul.ae.mdmcontrollers.PrinterModule.models.config.LinePrintingStatus
import java.lang.RuntimeException
import javax.inject.Inject

/**
 * Created by sotra@altakamul.tr on 2/17/2021.
 */
class PrintUseCase @Inject constructor(
    var context: Context
){
    /**
     * print incoming text
     * return printing status
     */
    @Throws(RuntimeException::class)
     fun invoke(text : String):LinePrintingStatus{
        return TkamulPrinterFactory.getTkamulPrinter(context)
            .addEmptyLine()
            .addText(text)
            .addEmptyLine()
            .addEmptyLine()
            .printOnPaper()
    }
    fun  getPrinterStats(): DevicePrinterStatus {
        return TkamulPrinterFactory.getTkamulPrinter(context).getPrinterStatus()
    }
}