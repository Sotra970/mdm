package tkamul.ae.mdmcontrollers.PrinterModule

import android.content.Context
import tkamul.ae.mdmcontrollers.PrinterModule.models.config.PrinterType

/**
 * Created by sotra@altakamul.tr on 3/2/2021.
 */
object TkamulPrinterFactory {

     fun getTkamulPrinter(context: Context):TkamulPrinterBase{
        return when(TkamulPrinterBase.getPrinterType()){
            PrinterType.MOBIEWIRE ->{
                   MobiWireTkamulPrinter()
            }
            PrinterType.CSPRINTER ->{
                CSTkamulPrinter(context)
            }
        }
    }
}