package tkamul.ae.mdmcontrollers.domain.interactors.remote

import com.google.gson.Gson
import io.socket.client.Ack
import tkamul.ae.mdmcontrollers.data.gateways.socketgateway.SocketEventCallbacks
import tkamul.ae.mdmcontrollers.data.gateways.socketgateway.SocketApiClientImplementer
import tkamul.ae.mdmcontrollers.domain.core.Config
import javax.inject.Inject

/**
 * Created by sotra@altakamul.tr on 2/17/2021.
 */
class SocketRepo @Inject constructor(
    var  socket: SocketApiClientImplementer
){
    /**
     * invoke from [MDMService]
     */
    val url = Config.MDM_SOCKET_URL
    lateinit var  query  : String
    val eventname = Config.MDM_SOCKET_CHANNEL
    @Throws(Exception::class)
     fun  observe(serial : String, callbacks : SocketEventCallbacks){
        query= "serialNo=[${serial.replace(" " ,"")}]"
        socket.observe(url ,   query, eventname, socketEventCallbacks = callbacks)
    }
    /**
     * invoke from [MDMService]
     */
    @Throws(Exception::class)
    fun  disconnect(){
        socket.disconnect(url,query,eventname)
    }

    /**
     * invoke  from [SendInfoController]
     */
    fun <P> send (payload : P , ack : Ack = Ack{}){
        val json = Gson().toJson(payload)
        socket.sendMessage( url , query , eventname ,json , ack)
    }

}