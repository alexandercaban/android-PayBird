package service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class BluetoothService extends Service {
    public static final String TAG = "TAG.BluetoothService";

    public static final String ACTION_PRINT = "print";
    public static final String ACTION_RESTART_CONECT_THREAD = "restartThread";
    public static final String KEY_DATA_PRINT = "printData";
    public static final String ACTION_THREAD_CLOSE = "closeThread";
    public static boolean isRunning = false;
    private ConectThread ct;
    private PrintReceiver pr;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        isRunning = true;
        this.pr = new PrintReceiver();
        IntentFilter iFilter = new IntentFilter(ACTION_PRINT);
        iFilter.addAction(ACTION_RESTART_CONECT_THREAD);
        iFilter.addAction(ACTION_THREAD_CLOSE);
        this.registerReceiver(pr, iFilter);
        if (RunServicePrinter.bluetoothSocket != null && !ConectThread.conected) {
            this.ct = new ConectThread(RunServicePrinter.bluetoothSocket, this);
            this.ct.start();
        }
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        this.unregisterReceiver(this.pr);
        this.ct.interrupt();
    }

    class PrintReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String sbAction = intent.getAction();
            if (ACTION_PRINT.equals(sbAction)) {
                byte[] rcPrint = intent.getByteArrayExtra(KEY_DATA_PRINT);
                if (ct != null && rcPrint != null) {
                    try {
                        ct.print(rcPrint);
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage(), e);
                        Toast.makeText(BluetoothService.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            } else if (ACTION_RESTART_CONECT_THREAD.equals(sbAction)) {
                if (ct == null || (RunServicePrinter.bluetoothSocket != null && !ConectThread.conected)) {
                    ct = new ConectThread(RunServicePrinter.bluetoothSocket, BluetoothService.this);
                    ct.start();
                }
            } else if (ACTION_THREAD_CLOSE.equals(sbAction)) {
                if (ct != null) {
                    isRunning = false;
                    ct.interrupt();

                }
            }
        }
    }
}
