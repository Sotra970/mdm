package tkamul.ae.mdmcontrollers.contollers

import tkamul.ae.mdmcontrollers.PrinterModule.models.config.LinePrintingStatus
import tkamul.ae.mdmcontrollers.data.gateways.socketgateway.socketRemoteModels.argsResponse.NameValuePairs
import tkamul.ae.mdmcontrollers.data.gateways.socketgateway.socketRemoteModels.sendingObject.Args
import tkamul.ae.mdmcontrollers.data.gateways.socketgateway.socketRemoteModels.sendingObject.DeviceInfo2SocketPayload
import tkamul.ae.mdmcontrollers.data.gateways.socketgateway.socketRemoteModels.sendingObject.UnInstallInfo2SocketPayload
import tkamul.ae.mdmcontrollers.domain.core.Config
import tkamul.ae.mdmcontrollers.domain.core.DownloadUtils
import tkamul.ae.mdmcontrollers.domain.interactors.CSUseCases.MDMInfoInteractor
import tkamul.ae.mdmcontrollers.domain.interactors.remote.SocketRepo
import javax.inject.Inject
import kotlin.concurrent.thread


class SendInfoController @Inject constructor(
        var socketRepoController : SocketRepo,
        var mdmInfoController  : MDMInfoInteractor
){


    /**
     * function to send all controls status + device info
     */
    fun sendStatusToSocket(pairs: NameValuePairs) {
        thread {
            // sleep 5 sec to wait controllers status to be ready
            // ex : wait wifi until have a status of (enable/disable) not (enabling/disabling )
            //      then send mobile info
            Thread.sleep(5*1000)
            mdmInfoController.invoke {
                socketRepoController.send(DeviceInfo2SocketPayload(
                        args = Args(pairs.args.nameValuePairs.ray_id),
                        device = it,
                        event = Config.Events.SET_DEVICE_INFO_EVENT
                ))
            }
        }
    }

    /**
     * function to send  controls status + device info + anyObject  to responsible Socket channel
     */
    fun sendInstallStatusToSocket(pairs: NameValuePairs, installStatus: DownloadUtils.DownloadStatus?, installed:Boolean ) {
            mdmInfoController.invoke {
                it.apply {
                    this.installStatus = installStatus
                    this.installed = installed
                }
                socketRepoController.send(DeviceInfo2SocketPayload(
                        event = Config.Events.SET_DEVICE_INFO_EVENT ,
                        device = it,
                        args = Args(pairs.args.nameValuePairs.ray_id)
                ))
            }
    }

    /**
     * function to send  controls status + device info + anyObject  to responsible Socket channel
     */
    fun sendUnInstallStatusToSocket(pairs: NameValuePairs, unInstalled:Boolean ) {
            mdmInfoController.invoke {
                it.apply {
                    this.unInstalled = unInstalled
                }
                socketRepoController.send(UnInstallInfo2SocketPayload(
                        event = Config.Events.SET_DEVICE_INFO_EVENT ,
                        device = it,
                        unInstalled=unInstalled,
                        args = Args(pairs.args.nameValuePairs.ray_id)
                ))
            }
    }
    /**
     * function to send  controls status + device info + print status  to responsible Socket channel
     */
    fun setPrintStatusToSocket(pairs: NameValuePairs, lastLineStatus: LinePrintingStatus) {
            mdmInfoController.invoke {
                it.apply {
                    this.lastLineStatus = lastLineStatus
                }
                socketRepoController.send(DeviceInfo2SocketPayload(
                        args = Args(pairs.args.nameValuePairs.ray_id),
                        device = it,
                        event = Config.Events.SET_DEVICE_INFO_EVENT
                ))
            }
    }

}