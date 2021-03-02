package tkamul.ae.mdmcontrollers.service.MDMService

import android.os.Binder
import android.os.IBinder

class MDMBinderImplemnter  constructor(
) : Binder() ,
    MDMServiceInterface {
    override fun asBinder(): IBinder {
        return this
    }
}
