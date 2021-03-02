package tkamul.ae.mdmcontrollers.domain.core.extentionFunction

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import tkamul.ae.mdmcontrollers.data.gateways.socketModels.argsResponse.NameValuePairs
import tkamul.ae.mdmcontrollers.data.gateways.socketModels.argsResponse.SocketArgsResponseItem
import tkamul.ae.mdmcontrollers.domain.core.Logger
import java.lang.reflect.Type


/**
 * Created by sotra@altakamul.tr on 2/23/2021.
 */

fun <T> Array<T>.toArgsResponse(): NameValuePairs {
    Logger.logd(this)
    val argsToJson = Gson().toJson(this[0])
    val collectionType: Type = object : TypeToken<ArrayList<SocketArgsResponseItem>>() {}.type
    val argsResponseObj : ArrayList<SocketArgsResponseItem> =  GsonBuilder().create().fromJson(argsToJson , collectionType)
    val processPairs = argsResponseObj[0].nameValuePairs
    return  processPairs
}
