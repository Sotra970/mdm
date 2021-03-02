package tkamul.ae.mdmcontrollers.domain.core

import android.util.Log
import timber.log.Timber
import timber.log.Timber.DebugTree
import tkamul.ae.mdmcontrollers.BuildConfig
import javax.inject.Singleton

/**
 * Created by sotra@altakamul.tr on 2/17/2021.
 */
object Logger {
    init {
        if (BuildConfig.DEBUG && Timber.treeCount()==0)
            Timber.plant(object : DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return   element.className + ": " +  element.methodName + ": " + element.lineNumber
                }
            })
    }
    @JvmStatic
     inline fun logd(message : Any?){
        Timber.d(message?.toString()?:"null")
    }
    @JvmStatic
    inline fun loge(e: Throwable?) {
        Timber.e(e)
    }
    @JvmStatic
    fun loge(message: Any?) {
        Timber.e(message?.toString()?:"null")
    }


}