package tkamul.ae.mdmcontrollers.domain.entities

import com.mediatek.settings.service.DeviceInfo
import tkamul.ae.printer_factory.models.config.DevicePrinterStatus
import tkamul.ae.printer_factory.models.config.LinePrintingStatus
import tkamul.ae.mdmcontrollers.domain.core.DownloadUtils

data class  MDMInfo(
        val deviceInfo: DeviceInfo,
        val wifiStatus: String?,
        val nfcStatus: String?,
        val locationStatus: String?,
        val dataStatus: String?,
        val bluetoothStatus: String?,
        var installedPackages: List<PackageInfo>,
        val printerStatus: tkamul.ae.printer_factory.models.config.DevicePrinterStatus
) {
    var executedEvent: String?=null
    var unInstalled: Boolean? = null
    var installed: Boolean? = null
     var installStatus: DownloadUtils.DownloadStatus?=null
     var lastLineStatus: tkamul.ae.printer_factory.models.config.LinePrintingStatus? = null
}