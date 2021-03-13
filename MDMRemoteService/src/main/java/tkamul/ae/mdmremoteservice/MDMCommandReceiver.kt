package tkamul.ae.mdmremoteservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Created by sotra@altakamul.tr on 3/13/2021.
 */
class MDMCommandReceiver : BroadcastReceiver() {

     val COMMAND_ID_KEY =  "COMMAND_ID_KEY"
     val COMMAND_RAY_ID_KEY =  "COMMAND_RAY_ID"


    override fun onReceive(context: Context, intent: Intent) {
        val rayId = intent.getStringExtra(COMMAND_RAY_ID_KEY)
        val command = intent.getStringExtra(COMMAND_ID_KEY)
        for (child in subscribers)
            child.executeCommand(rayId!!,command!!)
    }

    interface MDMServiceEventInterface {
        fun executeCommand(rayId :String , eventId: String)
    }
    companion object{
        private val subscribers = mutableListOf<MDMServiceEventInterface>()
        fun addSubscriber(mdmServiceEventInterface: MDMServiceEventInterface){
            subscribers.add(mdmServiceEventInterface)
        }
    }
}