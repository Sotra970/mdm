package tkamul.ae.mdmcontrollers.domain.useCases

import android.content.Context
import tkamul.ae.mdmcontrollers.PrinterModule.TkamulPrinterFactory
import tkamul.ae.mdmcontrollers.PrinterModule.models.textFormat.PrinterTextScale
import tkamul.ae.mdmcontrollers.domain.core.Logger
import tkamul.ae.mdmcontrollers.service.MobiMediaTechServiceUtil
import java.lang.RuntimeException
import javax.inject.Inject

/**
 * Created by sotra@altakamul.tr on 2/17/2021.
 */
class PrintUseCase @Inject constructor(
    val context: Context
){
    @Throws(RuntimeException::class)
     fun invoke(text : String){
        TkamulPrinterFactory.getTkamulPrinter(context)
            .addEmptyLine()
            .addText(text)
            .addEmptyLine()
            .addEmptyLine()
            .printOnPaper()
    }
}