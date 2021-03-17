package tkamul.ae.mdmcontrollers.data.gateways.socketgateway.socketRemoteModels.sendingObject

import tkamul.ae.mdmcontrollers.domain.entities.MDMInfo

data class DeviceInfo2SocketPayload(
        val args: Args,
        val device: MDMInfo,
        val event: String
)