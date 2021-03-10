package tkamul.ae.mdmcontrollers.contollers

import tkamul.ae.mdmcontrollers.data.gateways.socketModels.argsResponse.Args
import tkamul.ae.mdmcontrollers.data.gateways.socketModels.argsResponse.NameValuePairs
import tkamul.ae.mdmcontrollers.data.gateways.socketModels.argsResponse.NameValuePairsX
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