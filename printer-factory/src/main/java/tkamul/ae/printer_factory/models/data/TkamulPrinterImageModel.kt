package tkamul.ae.printer_factory.models.data

import android.graphics.Bitmap

/**
 * Created by developers@appgain.io on 8/26/2018.
 */
class TkamulPrinterImageModel : TkamulPrintingData {


    var bitmap:ByteArray?
    var path: String? = null

    constructor(bitmap: ByteArray) {
        this.bitmap = bitmap
        path = null
    }

    constructor(bitmapPath: String) {
        path = bitmapPath
        bitmap = null
    }

}