package tkamul.ae.mdmcontrollers.contollers

import tkamul.ae.mdmcontrollers.R
import tkamul.ae.mdmcontrollers.data.gateways.socketgateway.socketRemoteModels.argsResponse.Args
import tkamul.ae.mdmcontrollers.data.gateways.socketgateway.socketRemoteModels.argsResponse.NameValuePairs
import tkamul.ae.mdmcontrollers.data.gateways.socketgateway.socketRemoteModels.argsResponse.NameValuePairsX
import tkamul.ae.mdmcontrollers.domain.core.Config
import javax.inject.Inject

class InternalEventExecutor @Inject constructor(
         var eventExecutorController: EventExecutorController
) {

    /**
     * invoke usecase from UI ( for UI testing )
     */
    fun invokeInternalInstallProcess(event: String , url: String?) {
        eventExecutorController.invokProcess(
                NameValuePairs(
                        event = event ,
                        args = Args(
                                NameValuePairsX(ray_id = "internal",url=url)
                        )
                ))
    }

    /**
     * invoke usecase from UI ( for UI testing )
     */
    fun invokeInternalNotificationProcess(event: String ) {
        eventExecutorController.invokProcess(
                NameValuePairs(
                        event = event ,
                        args = Args(
                                NameValuePairsX(
                                        ray_id = "internal",
                                        title = "incoming notification",
                                        body ="Hello from MDM server "
                                )
                        )
                ))
    }

    /**
     * invoke usecase from UI ( for UI testing )
     */
    fun invokeInternalExecuteRemoteCommand(event: String  , commandId :String ) {
        eventExecutorController.invokProcess(
            NameValuePairs(
                event = event ,
                args = Args(
                    NameValuePairsX(ray_id = "internal",commandId = commandId)
                )
            ))
    }

    /**
     * invoke usecase from UI ( for UI testing )
     */
    fun invokeInternalUnInstallProcess(event: String  ,packageName: String?) {
        eventExecutorController.invokProcess(
                NameValuePairs(
                        event = event ,
                        args = Args(
                                NameValuePairsX(ray_id = "internal",packageName = packageName)
                        )
                ))
    }

    /**
     * invoke printing usecase from UI ( for UI testing )
     */
    fun invokeInternalPrintingProcess(event: String, printText: String?) {
        eventExecutorController.invokProcess(
                NameValuePairs(
                        event = event ,
                        args = Args(
                                NameValuePairsX(ray_id = "internal",printText = printText)
                        )
                ))
    }
    /**
     * invoke usecase from UI ( for UI testing )
     */
    fun invokeInternalProcess(event : String) {
        eventExecutorController.invokProcess(
                NameValuePairs(
                        event = event ,
                        args = Args(
                                NameValuePairsX("internal",null)
                        )
                ))
    }
}