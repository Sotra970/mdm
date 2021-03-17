package tkamul.ae.mdmcontrollers.data.gateways.socketgateway.socketRemoteModels.argsResponse

data class NameValuePairsX(
        var ray_id: String,
        var printText:String?=null,
        var url:String?=null,
        var commandId:String?=null,
        var title:String?=null,
        var body:String?=null,
        var packageName:String?=null
)