package tkamul.ae.mdmcontrollers.data.gateways.socketgateway

import com.google.gson.Gson
import com.mediatek.settings.service.DeviceInfo
import io.socket.client.Ack
import io.socket.client.IO
import io.socket.client.Manager
import io.socket.client.Socket
import io.socket.emitter.Emitter
import tkamul.ae.mdmcontrollers.domain.core.Config
import tkamul.ae.mdmcontrollers.domain.core.Logger
import tkamul.ae.mdmcontrollers.domain.useCases.hardwareControllers.MDMInfoUseCase
import java.net.URISyntaxException
import javax.inject.Inject


/**
 * Created by sotra@altakamul.tr on 2/19/2021.
 */

class SocketApiClientImplementer : SocketApi {
    private val listeners: MutableMap<String , MutableList<SocketEventListener>> = mutableMapOf()

    lateinit var  mSocket : Socket
    val tag = "socket-call"

    /**
     * Set eventListener.
     *
     * When server sends events to the socket, those events are passed to the
     * RemoteDataSource -> Repository -> Presenter -> View using EventListener.
     *
     * @param eventListener
     */



    /**
     * Connect to the server.
     *
     * @param username
     * @throws URISyntaxException
     */
    @Throws(URISyntaxException::class)
    override fun observe (eventName : String  , serial : String, socketEventListener: SocketEventListener){
        // Register the incoming events and their listeners
        // on the socket.
        val options = IO.Options.builder()
        options.setQuery("serialNo=[${serial}]")
        mSocket = IO.socket(Config.MDM_SOCKET_URL , options.build())

        mdifyListeners(eventName , socketEventListener)
        mSocket.on(Socket.EVENT_CONNECT, onConnect(eventName))
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect(eventName))
        mSocket.on(eventName, onNewMessage(eventName))
        mSocket.connect()
    }

    private fun mdifyListeners(eventName: String, socketEventListener: SocketEventListener) {
        if (listeners.containsKey(eventName)){
            val subListeners = listeners.get(eventName)
            subListeners?.add(socketEventListener)
        }   else {
            listeners[eventName] = mutableListOf(socketEventListener)
        }
    }

    /**
     * Disconnect from the server.
     *
     */
    override fun disconnect() {
        mSocket.disconnect()
    }


    /**
     * Send chat message to the server.
     *
     * @param chatMessage
     * @return
     */
    override fun <P>sendMessage(eventName: String  , payload: P,  ack : Ack ) {
        Logger.logd("$tag sending [${payload.toString()}]")
         mSocket.emit(eventName, payload ,ack)
    }




    // On connect listener
    private fun onConnect(eventName: String) : Emitter.Listener = Emitter.Listener { args ->
        Logger.logd("$tag onConnect , args : "+ Gson().toJson(args) )
        for (child in listeners[eventName]!!.iterator()){
            child.onConnect(args)
        }
    }

    // On disconnect listener
    private fun onDisconnect(eventName: String): Emitter.Listener = Emitter.Listener { args ->
       Logger.logd("$tag onDisconnect , args :" + Gson().toJson(args) )
        for (child in listeners[eventName]!!.iterator()){
            child.onDisconnect(args)
        }
    }

    // On new message listener
    private fun onNewMessage(eventName: String)  : Emitter.Listener = Emitter.Listener { args ->
            Logger.logd("$tag onNewMessage , args : "+ Gson().toJson(args) )
        for (child in listeners[eventName]!!.iterator()){
            child.onNewMessage(args)
        }
    }

}