package tkamul.ae.mdmcontrollers.domain.useCases.remote

import com.google.gson.Gson
import io.socket.client.Ack
import org.json.JSONObject
import tkamul.ae.mdmcontrollers.data.gateways.socketgateway.SocketEventListener
import tkamul.ae.mdmcontrollers.data.gateways.socketgateway.SocketApiClientImplementer
import tkamul.ae.mdmcontrollers.domain.core.Config
import tkamul.ae.mdmcontrollers.domain.core.Logger
import javax.inject.Inject

/**
 * Created by sotra@altakamul.tr on 2/17/2021.
 */
class MDMSocketChannelUseCase @Inject constructor(
    var  socket: SocketApiClientImplementer
){
    @Throws(Exception::class)
     fun  observe(serial : String , listener : SocketEventListener){
        socket.observe(Config.MDM_SOCKET_CHANNEL , serial.replace(" " ,"")     , listener )
    }
    fun <P> send (payload : P , ack : Ack = Ack{}){
        val json = Gson().toJson(payload)
        socket.sendMessage( Config.SET_DEVICE_INFO_SOCKET_CHANNEL ,json , ack)
    }
}