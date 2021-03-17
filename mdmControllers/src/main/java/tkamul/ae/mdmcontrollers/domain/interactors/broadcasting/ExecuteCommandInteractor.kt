package tkamul.ae.mdmcontrollers.domain.interactors.broadcasting

import android.content.Context
import android.content.Intent
import tkamul.ae.mdmcontrollers.domain.core.Config
import javax.inject.Inject

/**
 * Created by sotra@altakamul.tr on 2/17/2021.
 */
class ExecuteCommandInteractor @Inject constructor(
        val context: Context
){
    fun invoke(commandId :String , rayId:String){
        context.sendBroadcast(Intent().apply {
            action = Config.Events.REMOTE_COMMAND_RECEIVER
            putExtra(Config.Events.COMMAND_ID_KEY,commandId)
            putExtra(Config.Events.COMMAND_RAY_ID_KEY,rayId)
        })
    }
}