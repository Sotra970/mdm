package tkamul.ae.mdmcontrollers.contollers

import tkamul.ae.mdmcontrollers.PrinterModule.models.config.LinePrintingStatus
import tkamul.ae.mdmcontrollers.data.gateways.socketModels.argsResponse.NameValuePairs
import tkamul.ae.mdmcontrollers.data.gateways.socketModels.sendingObject.Args
import tkamul.ae.mdmcontrollers.data.gateways.socketModels.sendingObject.DeviceInfo2SocketPayload
import tkamul.ae.mdmcontrollers.data.gateways.socketModels.sendingObject.InstallInfo2SocketPayload
import tkamul.ae.mdmcontrollers.data.gateways.socketModels.sendingObject.UnInstallInfo2SocketPayload
import tkamul.ae.mdmcontrollers.domain.core.Config
import tkamul.ae.mdmcontrollers.domain.core.DownloadUtils
import tkamul.ae.mdmcontrollers.domain.useCases.CSUseCases.MDMInfoUseCase
import tkamul.ae.mdmcontrollers.domain.useCases.remote.MDMSocketChannelUseCase
import javax.inject.Inject
import kotlin.concurrent.thread


class SendInfoController @Inject constructor(
         var mdmSocketChannelController : MDMSocketChannelUseCase,
         var mdmInfoController  : MDMInfoUseCase
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
                mdmSocketChannelController.send(DeviceInfo2SocketPayload(
                        args = Args(pairs.args.nameValuePairs.ray_id),
                        device = it,
                        event = Config.Events.ON_CONNECT
                ))
            }
        }
    }

    /**
     * function to send  controls status + device info + anyObject  to responsible Socket channel
     */
    fun sendInstallStatusToSocket(pairs: NameValuePairs, installStatus: DownloadUtils.DownloadStatus?, installed:Boolean ) {
            mdmInfoController.invoke {
                it.installedPackages = listOf()
                mdmSocketChannelController.send(InstallInfo2SocketPayload(
                        event = Config.Events.ON_CONNECT ,
                        device = it,
                        downloadStatus=installStatus,
                        installed=installed,
                        args = Args(pairs.args.nameValuePairs.ray_id)
                ))
            }
    }

    /**
     * function to send  controls status + device info + anyObject  to responsible Socket channel
     */
    fun sendUnInstallStatusToSocket(pairs: NameValuePairs, unInstalled:Boolean ) {
            mdmInfoController.invoke {
                it.installedPackages = listOf()
                mdmSocketChannelController.send(UnInstallInfo2SocketPayload(
                        event = Config.Events.ON_CONNECT ,
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
                mdmSocketChannelController.send(DeviceInfo2SocketPayload(
                        args = Args(pairs.args.nameValuePairs.ray_id),
                        device = it,
                        event = Config.Events.ON_CONNECT
                ))
            }
    }

}