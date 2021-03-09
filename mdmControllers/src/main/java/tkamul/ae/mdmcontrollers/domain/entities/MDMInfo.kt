package tkamul.ae.mdmcontrollers.domain.entities

import com.mediatek.settings.service.DeviceInfo
import tkamul.ae.mdmcontrollers.PrinterModule.models.config.DevicePrinterStatus
import tkamul.ae.mdmcontrollers.PrinterModule.models.config.LinePrintingStatus

data class  MDMInfo(
        val deviceInfo: DeviceInfo,
        val wifiStatus: String?,
        val nfcStatus: String?,
        val locationStatus: String?,
        val dataStatus: String?,
        val bluetoothStatus: String?,
        var installedPackages: List<PackageInfo>,
        val printerStatus: DevicePrinterStatus
) {
     var lastLineStatus: LinePrintingStatus? = null
}