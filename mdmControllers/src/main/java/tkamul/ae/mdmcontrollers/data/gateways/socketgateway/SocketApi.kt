package tkamul.ae.mdmcontrollers.data.gateways.socketgateway
import io.socket.client.Ack
import io.socket.client.Socket
import java.net.URISyntaxException

/**
 * Created by sotra@altakamul.tr on 2/19/2021.
 */
interface SocketApi {

    @Throws(URISyntaxException::class)
    fun observe(url : String, eventName : String, serial : String, socketEventCallbacks: SocketEventCallbacks)

    fun disconnect(url: String, query: String,eventName: String)

    fun <P>sendMessage( url: String, query: String,eventName: String , payload: P , ack  : Ack = Ack{})

}