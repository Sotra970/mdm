package tkamul.ae.mdmcontrollers.domain.core

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager

/**
 * Created by sotra@altakamul.tr on 2/28/2021.
 */
object LocationUtils {
    @SuppressLint("MissingPermission")
    internal fun getLastKnownLocation(context: Context): String? {
        val mLocationManager = context
            .getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val providers: List<String> = mLocationManager.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            val l: Location = mLocationManager.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l
            }
        }
        val latitude = bestLocation?.latitude?:0.0
        val longitude = bestLocation?.longitude?:0.0
        return "$latitude,$longitude"
    }
}