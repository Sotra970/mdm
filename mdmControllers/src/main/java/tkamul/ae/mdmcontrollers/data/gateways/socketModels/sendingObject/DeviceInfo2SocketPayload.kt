package tkamul.ae.mdmcontrollers.data.gateways.socketModels.sendingObject

import tkamul.ae.mdmcontrollers.domain.useCases.hardwareControllers.MDMInfo

data class DeviceInfo2SocketPayload(
    val args: Args,
    val device: MDMInfo,
    val event: String
)