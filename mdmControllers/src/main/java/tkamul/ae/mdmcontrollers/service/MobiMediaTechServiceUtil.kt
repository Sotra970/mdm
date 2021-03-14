package tkamul.ae.mdmcontrollers.service

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.mediatek.settings.service.CSAndoridGo
import com.mediatek.settings.service.CsApiAndroidQ
import java.lang.RuntimeException

/**
 * Created by sotra@altakamul.tr on 2/17/2021.
 */


/**
 * class to get Media Tech interface for device operations like wifi , blutoothe , etc
 */
 class
MobiMediaTechServiceUtil(val context: Context) {

    private val mediatekRemoteServiceIntent = Intent("com.mediatek.settings.MyService.action")

    @Throws(RuntimeException::class)
    fun getGoInterface(onServiceConnected :(CSAndoridGo)->Unit ){
       bindRemoteService {
           onServiceConnected(CSAndoridGo.Stub.asInterface(it))
       }
    }
    @Throws(RuntimeException::class)
    fun getQInterface(onServiceConnected :(CsApiAndroidQ)->Unit ){
        bindRemoteService {
            onServiceConnected(CsApiAndroidQ.Stub.asInterface(it))
        }
    }

    // function to get androidQ or androidgo interface
    @Throws(RuntimeException::class)
     fun bindRemoteService(onServiceConnected :(IBinder)->Unit ){
        val intent = Intent()
        intent.action = "com.mediatek.settings.MyService.action" //若修改了清单文件，一定要重启手机！
        val service = createExplicitFromImplicitIntent(context)
        val connection = geMediaTechConnectionListener { serviceInterface: IBinder ->
            onServiceConnected(serviceInterface)
        }
        context.bindService(service, connection, Context.BIND_AUTO_CREATE)
    }



    // create explicit intent for [mediatekRemoteServiceIntent}
    fun createExplicitFromImplicitIntent(
        context: Context
    ): Intent {
        // Retrieve all services that can match the given intent
        val packageManager = context.packageManager
        val resolveInfo = packageManager.queryIntentServices(mediatekRemoteServiceIntent, 0)
        resolveInfo.ifEmpty{null}?.run {
            // Get component info and create ComponentName
            val serviceInfo = resolveInfo[0]
            val packageName = serviceInfo.serviceInfo.packageName
            val className = serviceInfo.serviceInfo.name
            val component = ComponentName(packageName, className)
            // Set the component to be explicit
            return mediatekRemoteServiceIntent.apply {
                this.component = component
            }
        }
        throw Exception( "cant find service match [${mediatekRemoteServiceIntent.action}]")
    }

    @Throws(RuntimeException::class)
    private fun geMediaTechConnectionListener(onServiceConnected :  ( serviceInterface: IBinder)->Unit = {} ): ServiceConnection {
        return object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, serviceInterface: IBinder) {
                onServiceConnected(serviceInterface)
            }
            override fun onServiceDisconnected(componentName: ComponentName) {
                throw RuntimeException("Service Disconnected")
            }
        }
    }

}