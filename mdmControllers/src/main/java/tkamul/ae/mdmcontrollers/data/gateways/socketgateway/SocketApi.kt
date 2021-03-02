package tkamul.ae.mdmcontrollers.data.gateways.socketgateway
import io.socket.client.Ack
import io.socket.emitter.Emitter
import java.net.URISyntaxException

/**
 * Created by sotra@altakamul.tr on 2/19/2021.
 */
interface SocketApi {

    @Throws(URISyntaxException::class)
    fun observe(eventName : String ,  serial : String , socketEventListener: SocketEventListener)

    fun disconnect()

    fun <P>sendMessage( eventName: String , payload: P , ack  : Ack = Ack{})

}