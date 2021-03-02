package tkamul.ae.mdmcontrollers.domain.useCases.hardwareControllers.core

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import tkamul.ae.mdmcontrollers.domain.core.Config

/**
 * Created by sotra@altakamul.tr on 2/26/2021.
 */
object BluetoothUtil {
    var bluetoothManager : BluetoothManager?= null
    internal fun getAdapter(context : Context): BluetoothAdapter? {
        bluetoothManager = bluetoothManager ?.let { it }?:context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        return bluetoothManager?.adapter
    }
    internal fun getBluetoothAdapterState(context : Context): String {
        return getAdapter(context)?.let {
            return when(it.state ){
                BluetoothAdapter.STATE_OFF->{
                    Config.Bluetooth.OFF}
                BluetoothAdapter.STATE_TURNING_ON->{
                    Config.Bluetooth.TURNING_ON }
                BluetoothAdapter.STATE_ON->{
                    Config.Bluetooth.ON}
                BluetoothAdapter.STATE_TURNING_OFF->{
                    Config.Bluetooth.TURNING_OFF}
                Config.Bluetooth.BLE_TURNING_ON_VALUE->{
                    Config.Bluetooth.BLE_TURNING_ON} //  BluetoothAdapter.STATE_BLE_TURNING_ON}
                Config.Bluetooth.BLE_ON_VALUE->{
                    Config.Bluetooth.BLE_ON}  //  BluetoothAdapter.STATE_BLE_ON
                Config.Bluetooth.BLE_TURNING_OFF_VALUE->{
                    Config.Bluetooth.BLE_TURNING_OFF} //  BluetoothAdapter.STATE_BLE_TURNING_OFF
                else->{
                    Config.Bluetooth.NA+this}
            }
        }?: Config.Bluetooth.NA
    }
}