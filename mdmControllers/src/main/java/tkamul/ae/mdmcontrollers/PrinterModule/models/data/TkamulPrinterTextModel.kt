package tkamul.ae.mdmcontrollers.PrinterModule.models.data

import tkamul.ae.mdmcontrollers.PrinterModule.models.textFormat.PrinterTextAlign
import tkamul.ae.mdmcontrollers.PrinterModule.models.textFormat.PrinterTextScale

/**
 * Created by developers@appgain.io on 8/26/2018.
 */
class TkamulPrinterTextModel(
    var text: String = "",
    var scale: PrinterTextScale = PrinterTextScale.normal,
    var align: PrinterTextAlign = PrinterTextAlign.left
): TkamulPrintingData()