package tkamul.ae.mdmremoteservice

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder

/**
 * Created by sotra@altakamul.tr on 3/13/2021.
 */
class MDMRemoteCommandServiceImplementer(
    val context: Context ,
    val onServiceConnected :(MDMRemoteInterface)->Unit
){
    init {
        bind()
    }
    fun bind(){
        val service = createExplicitFromImplicitIntent()
        context.bindService(service, connection, Context.BIND_AUTO_CREATE)
    }
    private val connection =  object :ServiceConnection {
            override fun onServiceDisconnected(componentName: ComponentName) {
            }

            override fun onServiceConnected(componentName: ComponentName, service: IBinder) {
                onServiceConnected(MDMRemoteInterface.Stub.asInterface(service))
            }
        }

    private val mdmServiceIntent = Intent("tkamul.ae.mdmcontrollers.service.MDMService.MDMService")

    // create explicit intent for [MDMService}
    private fun createExplicitFromImplicitIntent(): Intent {
        // Retrieve all services that can match the given intent
        val resolveInfo = context.packageManager.queryIntentServices(mdmServiceIntent, 0)
        resolveInfo.ifEmpty{null}?.run {
            // Get component info and create ComponentName
            val serviceInfo = resolveInfo[0]
            val packageName = serviceInfo.serviceInfo.packageName
            val className = serviceInfo.serviceInfo.name
            val component = ComponentName(packageName, className)
            // Set the component to be explicit
            return mdmServiceIntent.apply {
                this.component = component
            }
        }
        throw Exception( "cant find service match [${mdmServiceIntent}]")
    }

}