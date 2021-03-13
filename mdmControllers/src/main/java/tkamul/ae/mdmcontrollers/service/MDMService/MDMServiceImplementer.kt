package tkamul.ae.mdmcontrollers.service.MDMService

import tkamul.ae.mdmremoteservice.MDMRemoteInterface

/**
 * Created by sotra@altakamul.tr on 3/13/2021.
 */

class MDMServiceImplementer(
    val mdmServiceEventInterface :MDMServiceEventInterface
) : MDMRemoteInterface.Stub() {
    override fun completeEvent(eventId: String?) {
        mdmServiceEventInterface.completeEvent(eventId)
    }
}