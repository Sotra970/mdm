package tkamul.ae.printer_factory

import android.content.Context
import tkamul.ae.printer_factory.models.config.PrinterType

/**
 * Created by sotra@altakamul.tr on 3/2/2021.
 */
/**
 * usage
 * TkamulPrinterFactory.getTkamulPrinter(context)
 *.addEmptyLine()
 * .addText(text)
 * .addEmptyLine()
 * .addEmptyLine()
 * .printOnPaper()  >> return LinePrintingStatus
 */
object TkamulPrinterFactory {

    @JvmStatic
     fun getTkamulPrinter(context: Context): TkamulPrinterBase {
        return when(TkamulPrinterBase.getPrinterType()){
            PrinterType.MOBIEWIRE ->{
                   MobiWireTkamulPrinter() // mp3
            }
            PrinterType.CSPRINTER ->{
                CSTkamulPrinter(context)// mp3+ , mp4+ , mp4
            }
        }
    }
}