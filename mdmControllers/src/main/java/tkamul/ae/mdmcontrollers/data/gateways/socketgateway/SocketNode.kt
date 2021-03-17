package tkamul.ae.mdmcontrollers.data.gateways.socketgateway

import io.socket.client.Socket

data class SocketNode (
        val socket : Socket ,
        val callbacks : MutableList<SocketEventCallbacks>
)
