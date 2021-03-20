package tkamul.ae.printer_factory.models.data

import tkamul.ae.printer_factory.models.textFormat.PrintTextAlign
import tkamul.ae.printer_factory.models.textFormat.PrintTextDirction
import tkamul.ae.printer_factory.models.textFormat.PrinterTextScale

/**
 * Created by developers@appgain.io on 8/26/2018.
 */
class TkamulPrinterTextModel(
    var text: String = "",
    var scale: PrinterTextScale = PrinterTextScale.normal,
    var align: PrintTextAlign = PrintTextAlign.LEFT,
    var dirction: PrintTextDirction = PrintTextDirction.LTR
): TkamulPrintingData()