package tkamul.ae.mdmcontrollers.data.gateways.socketModels.sendingObject

import tkamul.ae.mdmcontrollers.domain.core.DownloadUtils
import tkamul.ae.mdmcontrollers.domain.entities.MDMInfo

data class InstallInfo2SocketPayload(
        val args: Args,
        val device: MDMInfo,
        val event: String,
        val downloadStatus: DownloadUtils.DownloadStatus?,
        val installed: Boolean
)