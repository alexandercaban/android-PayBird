package service;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.playtechla.bluetoothlink.R;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.UUID;

public class RunServicePrinter {
    public static final String TAG = "TAG.RunServicePrinter";

    private static final UUID UUID_DEVICE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static BluetoothSocket bluetoothSocket = null;

    private Context context;
    private String sbMac;



    public RunServicePrinter(Context context, String sbMac) {
        this.context = context;
        this.sbMac = sbMac;
    }


    public void start() throws Exception {
        try {
            if("".equals(this.sbMac)) {
                throw new Exception(this.context.getString(R.string.err_impresora_no_asociada));
            }
            BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice bd = ba.getRemoteDevice(this.sbMac);

            if(bluetoothSocket != null) {
                try {
                    bluetoothSocket.close();
                    bluetoothSocket = null;
                } catch (Exception e){

                }
            }

            bluetoothSocket = this.createBluetoothSocket(bd);
            bluetoothSocket.connect();

            Intent iService = new Intent(this.context, BluetoothService.class);
            this.context.startService(iService);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            throw e;
        }
    }

    public void stop() throws Exception {
        try {
            try {
                bluetoothSocket.close();

            }catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
            Intent iService = new Intent(this.context, BluetoothService.class);
            this.context.stopService(iService);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            throw e;
        }
    }

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        if (Build.VERSION.SDK_INT >= 10) {
            try {
                final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[]{UUID.class});
                return (BluetoothSocket) m.invoke(device, UUID_DEVICE);
            } catch (Throwable e) {
                Log.w(TAG, "Could not create Insecure RFComm Connection", e);
                return device.createRfcommSocketToServiceRecord(UUID_DEVICE);
            }
        }
        return device.createRfcommSocketToServiceRecord(UUID_DEVICE);
    }


}
