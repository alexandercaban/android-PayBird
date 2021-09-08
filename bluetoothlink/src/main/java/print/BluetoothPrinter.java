package print;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

public class BluetoothPrinter {
    public static final String TAG = "TAG.BluetoothPrinter";

    private byte[] bPrint;

    public static String bold(String sbStr) {
        return "" + (char) 27 + (char) 71 + 1 + sbStr + (char) 27 + (char) 71 + 0;
    }

    public static String left(String sbStr) {
        return "" + (char) 27 + (char) 97 + (char) 48 + sbStr;
    }

    public static String right(String sbStr) {

        return "" + (char) 27 + (char) 97 + (char) 50 + sbStr;
    }

    public static String center(String sbStr) {

        return "" + (char) 27 + (char) 97 + (char) 49 + sbStr;
    }

    public static String fontLarge(String sbStr) {
        return "" + (char) 29 + (char) 33 + (char) 16 + sbStr;
    }

    public static String fontNormal(String sbStr) {
        return "" + (char) 29 + (char) 33 + (char) 0 + sbStr;
    }

    public static String fontSmall(String sbStr) {
        return "" + (char) 27 + (char) 33 + (char) 1 + sbStr + (char) 27 + (char) 33 + (char) 0;
    }

    public static String reverse(String sbStr) {
        return "" + (char) 29 + (char) 66 + (char) 1 + sbStr + (char) 29 + (char) 66 + (char) 0;
    }

    public static String alignValor(String sbCadena1, String sbCadena2, int nuSizeCharPage) {
        sbCadena1 = sbCadena1 + ":";
        sbCadena2 = "$" + sbCadena2;
        int nuSizeCadena1 = sbCadena1.length();
        int nuSizeCadena2 = sbCadena2.length();
        int nuTotalCadena = nuSizeCadena1 + nuSizeCadena2;
        int nuDiferencia = nuSizeCharPage - nuTotalCadena;
        int nuEspacios = Math.abs(nuDiferencia + nuSizeCadena2);
        sbCadena2 =  String.format("%"+nuEspacios+"s",sbCadena2);
        return sbCadena1 + sbCadena2;
    }

    public static byte[] print(Context ctx, int nuDrawable, int nuWidth, String sbPrint) throws Exception {
        try {
            byte[] rcPrint = null;
            byte[] rcImage = null;
            byte[] rcText = sbPrint.getBytes();
            if (nuDrawable != 0) {
                Bitmap bitmapOrg = BitmapFactory.decodeResource(ctx.getResources(), nuDrawable);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                bitmapOrg.compress(Bitmap.CompressFormat.JPEG, 100, bao);
                rcImage = bao.toByteArray();
                rcImage = BluetoothImagePrinter.getImage(Base64.encodeToString(rcImage, Base64.DEFAULT), nuWidth);

                rcPrint = new byte[rcImage.length + rcText.length];
                System.arraycopy(rcImage, 0, rcPrint, 0, rcImage.length);
                System.arraycopy(rcText, 0, rcPrint, rcImage.length, rcText.length);

                return rcPrint;
            }

            return rcText;
        } catch (Exception e) {
            Log.w(TAG, e.getMessage(), e);
            return new byte[0];
        }
    }

    public byte[] getbPrint() {
        return bPrint;
    }

    public void setbPrint(byte[] bPrint) {
        this.bPrint = bPrint;
    }
}