package tkamul.ae.mdmcontrollers.data.gateways.socketgateway

import com.google.gson.Gson
import io.socket.client.Ack
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import tkamul.ae.mdmcontrollers.domain.core.Logger
import java.net.URISyntaxException


/**
 * Created by sotra@altakamul.tr on 2/19/2021.
 */

class SocketApiClientImplementer : SocketApi {
    private val socketListeners: MutableMap<String , SocketNode> = mutableMapOf()

    val tag = "socket-call"



    /**
     * Connect to the server.
     * @throws URISyntaxException
     */
    @Throws(URISyntaxException::class)
    override fun observe (url :String, eventName : String, query : String, socketEventCallbacks: SocketEventCallbacks) {
        // Register the incoming events and their listeners
        // on the socket.
        val listenerKey = getListenerKey(url,query,eventName)
        if (socketListeners.containsKey(listenerKey)){
            socketListeners[listenerKey]?.callbacks?.add(socketEventCallbacks)
        }   else {
            val options = IO.Options.builder()
            options.setQuery(query)
            var newSocket = IO.socket(url, options.build())
            val node = SocketNode(
                    newSocket,
                    mutableListOf(socketEventCallbacks)
            )
            socketListeners[listenerKey] = node
            newSocket.on(Socket.EVENT_CONNECT, onConnect(node))
            newSocket.on(Socket.EVENT_DISCONNECT, onDisconnect(node))
            newSocket.on(eventName, onNewMessage(node))
            Logger.logd("start connection")
            newSocket.connect()
        }
    }

    private fun getListenerKey(url: String, query: String, eventName: String): String {
        return url+query+eventName
    }

    /**
     * Disconnect from the server.
     *
     */
    override fun disconnect(url: String, query: String, eventName: String) {
        socketListeners[getListenerKey(url,query,eventName)]?.socket?.disconnect()
    }


    /**
     * Send chat message to the server.
     *
     * @param chatMessage
     * @return
     */
    override fun <P>sendMessage(url: String, query: String,eventName: String  , payload: P,  ack : Ack ) {
        Logger.logd("$tag sending [${payload.toString()}]")
        socketListeners[getListenerKey(url,query,eventName)]?.socket?.emit(eventName, payload ,ack)
    }




    // On connect listener
    private fun onConnect(node: SocketNode) : Emitter.Listener = Emitter.Listener { args ->
        Logger.logd("$tag onConnect , args : "+ Gson().toJson(args) )
        for (child in node.callbacks){
            child.onConnect(args)
        }
    }

    // On disconnect listener
    private fun onDisconnect(node: SocketNode) : Emitter.Listener = Emitter.Listener { args ->
       Logger.logd("$tag onDisconnect , args :" + Gson().toJson(args) )
        for (child in node.callbacks){
            child.onDisconnect(args)
        }
    }

    // On new message listener
    private fun onNewMessage(node: SocketNode)  : Emitter.Listener = Emitter.Listener { args ->
        Logger.logd("$tag onNewMessage , args : "+ Gson().toJson(args) )
        for (child in node.callbacks){
            child.onNewMessage(args)
        }
    }

}