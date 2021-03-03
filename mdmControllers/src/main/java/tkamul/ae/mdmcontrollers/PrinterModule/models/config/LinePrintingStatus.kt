package tkamul.ae.mdmcontrollers.PrinterModule.models.config

/**
 * LinePrintingStatus : object identify last printed line on paper status
 */
data class LinePrintingStatus(
    var printed: Boolean = false,
    var errorMessage: String? = "UnKnown Error "
)
