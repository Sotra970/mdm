package tkamul.ae.mdmcontrollers.data.gateways.socketgateway

/**
 * Created by sotra@altakamul.tr on 2/22/2021.
 */

/**
 * Main interface to listen to server events.
 *
 */
interface SocketEventListener {
    fun onConnect( args: Any)
    fun onDisconnect(vararg args: Any)
    fun onNewMessage(vararg args: Any)
}