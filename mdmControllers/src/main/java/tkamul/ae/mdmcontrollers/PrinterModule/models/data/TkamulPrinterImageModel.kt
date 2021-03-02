package tkamul.ae.mdmcontrollers.PrinterModule.models.data

import android.graphics.Bitmap

/**
 * Created by developers@appgain.io on 8/26/2018.
 */
class TkamulPrinterImageModel : TkamulPrintingData {
    var bitmap: Bitmap?
    var path: String? = null

    constructor(bitmap: Bitmap) {
        this.bitmap = bitmap
        path = null
    }

    constructor(bitmapPath: String) {
        path = bitmapPath
        bitmap = null
    }

}