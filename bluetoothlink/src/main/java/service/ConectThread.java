package service;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;


import com.playtechla.bluetoothlink.R;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ConectThread extends Thread {
    // public static final int RECIEVE_MSG = 1;
    private static final String TAG = "TAG.ConectThread";
    public static boolean conected = false;
    private final InputStream isBuffer;
    private final OutputStream osBuffer;
    private Context context;
    private static final List<PrinterStatusListener> PRINTER_STATUS_LISTENERS = new ArrayList<>(0);

    public ConectThread(BluetoothSocket bsSocket, Context context) {

        InputStream isTmp = null;
        OutputStream osTmp = null;
        this.context = context;

        try {
            isTmp = bsSocket.getInputStream();
            osTmp = bsSocket.getOutputStream();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }

        isBuffer = isTmp;
        osBuffer = osTmp;

        conected = (isBuffer != null && osBuffer != null);
    }

    @Override
    public void interrupt() {
        super.interrupt();
        try {
            if(isBuffer != null){
                isBuffer.close();
            }
            if(osBuffer != null){
                osBuffer.close();
            }
            conected = false;
            notifyListeners();
        }catch (Exception e){
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void run() {
        byte[] buffer = new byte[256];
        int bytes;
        notifyListeners();

        while (true) {
            try {
                // Read from the InputStream
                bytes = isBuffer.read(buffer);
            } catch (Throwable e) {
                conected = false;
                Log.e(TAG, e.getMessage(), e);
                notifyListeners();
                break;
            }
        }
    }


    public void print(byte[] rcByte) throws Exception {
        try {
            if (isConected()) {
                if (!rcByte.equals("")) {
                    osBuffer.write(rcByte);
                    osBuffer.write(0x0A);
                }
            } else {
                throw new Exception(this.context.getString(R.string.err_not_connect_bluetooth_socket));
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean isConected() {
        return conected;
    }

    public void setConected(boolean conected) {
        conected = conected;
    }
    /**
     * @author carboleda@playtechla.com
     * @date 2017-02-02
     * Suscribe un listener para recibir notificaciones sobre el cambio de estado de conexion de la impresora
     * IMPORTANTE: Este metodo deberia ser invocado en el metodo onResume del Fragment o Activity
     * */
    public static void addListener(PrinterStatusListener listener) {
        if(!PRINTER_STATUS_LISTENERS.contains(listener)) {
            PRINTER_STATUS_LISTENERS.add(listener);
        }
    }

    /**
     * @author carboleda@playtechla.com
     * @date 2017-02-02
     * Remueve un listener para dejar de recibir notificaciones sobre el cambio de estado de conexion de la impresora
     * IMPORTANTE: Este metodo deberia ser invocado en el metodo onPause del Fragment o Activity
     * */
    public static void removeListener(PrinterStatusListener listener) {
        if(PRINTER_STATUS_LISTENERS.contains(listener)) {
            PRINTER_STATUS_LISTENERS.remove(listener);
        }
    }

    /**
     * @author carboleda@playtechla.com
     * @date 2017-02-02
     * Metodo encargado de notificarle a todos los listeners suscritos
     * */
    public void notifyListeners() {
        for (PrinterStatusListener listener : PRINTER_STATUS_LISTENERS) {
            listener.onChange(conected);
        }
    }

    /**
     * @author carboleda@playtechla.com
     * @date 2017-02-02
     * Interface para que otros objetos interezados en conocer el estado de la impresora se puedan suscribir
     * y recibir notificaciones
     * */
    public interface PrinterStatusListener {
        void onChange(boolean conected);
    }
}
