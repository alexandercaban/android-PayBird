package print;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;

public class BluetoothImagePrinter {
	private static int[][] Floyd16x16 = { { 0, 128, 32, 160, 8, 136, 40, 168, 2, 130, 34, 162, 10, 138, 42, 170 },
			{ 192, 64, 224, 96, 200, 72, 232, 104, 194, 66, 226, 98, 202, 74, 234, 106 },
			{ 48, 176, 16, 144, 56, 184, 24, 152, 50, 178, 18, 146, 58, 186, 26, 154 },
			{ 240, 112, 208, 80, 248, 120, 216, 88, 242, 114, 210, 82, 250, 122, 218, 90 },
			{ 12, 140, 44, 172, 4, 132, 36, 164, 14, 142, 46, 174, 6, 134, 38, 166 },
			{ 204, 76, 236, 108, 196, 68, 228, 100, 206, 78, 238, 110, 198, 70, 230, 102 },
			{ 60, 188, 28, 156, 52, 180, 20, 148, 62, 190, 30, 158, 54, 182, 22, 150 },
			{ 252, 124, 220, 92, 244, 116, 212, 84, 254, 126, 222, 94, 246, 118, 214, 86 },
			{ 3, 131, 35, 163, 11, 139, 43, 171, 1, 129, 33, 161, 9, 137, 41, 169 },
			{ 195, 67, 227, 99, 203, 75, 235, 107, 193, 65, 225, 97, 201, 73, 233, 105 },
			{ 51, 179, 19, 147, 59, 187, 27, 155, 49, 177, 17, 145, 57, 185, 25, 153 },
			{ 243, 115, 211, 83, 251, 123, 219, 91, 241, 113, 209, 81, 249, 121, 217, 89 },
			{ 15, 143, 47, 175, 7, 135, 39, 167, 13, 141, 45, 173, 5, 133, 37, 165 },
			{ 207, 79, 239, 111, 199, 71, 231, 103, 205, 77, 237, 109, 197, 69, 229, 101 },
			{ 63, 191, 31, 159, 55, 183, 23, 151, 61, 189, 29, 157, 53, 181, 21, 149 },
			{ 254, 127, 223, 95, 247, 119, 215, 87, 253, 125, 221, 93, 245, 117, 213, 85 } };
	private static int[] p0 = { 0, 0x80 };
	private static int[] p1 = { 0, 0x40 };
	private static int[] p2 = { 0, 0x20 };
	private static int[] p3 = { 0, 0x10 };
	private static int[] p4 = { 0, 0x08 };
	private static int[] p5 = { 0, 0x04 };
	private static int[] p6 = { 0, 0x02 };
	
	public static byte[] getImage(String sbBase64, int nuWidth){		
		byte[] imageAsBytes = android.util.Base64.decode(sbBase64, android.util.Base64.DEFAULT);
		
		int nMode = 0;
		Bitmap mBitmap = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
		int width = ((nuWidth + 7) / 8) * 8;
		int height = mBitmap.getHeight() * width / mBitmap.getWidth();
		height = ((height + 7) / 8) * 8;
		Bitmap rszBitmap = resizeImage(mBitmap, width, height);
		Bitmap grayBitmap = toGrayscale(rszBitmap);
		byte[] src = bitmapToBWPix(grayBitmap);
		byte[] data = pixToCmd(src, width, nMode);
		
		return data;
	}
	
	private static byte[] pixToCmd(byte[] src, int nWidth, int nMode) {
		// nWidth = 384; nHeight = 582;
		int nHeight = src.length / nWidth;
		byte[] data = new byte[8 + (src.length / 8)];
		data[0] = 0x1d;
		data[1] = 0x76;
		data[2] = 0x30;
		data[3] = (byte) (nMode & 0x01);
		data[4] = (byte) ((nWidth / 8) % 0x100);// (xl+xh*256)*8 = nWidth
		data[5] = (byte) ((nWidth / 8) / 0x100);
		data[6] = (byte) ((nHeight) % 0x100);// (yl+yh*256) = nHeight
		data[7] = (byte) ((nHeight) / 0x100);
		int k = 0;
		for (int i = 8; i < data.length; i++) {
			data[i] = (byte) (p0[src[k]] + p1[src[k + 1]] + p2[src[k + 2]]
					+ p3[src[k + 3]] + p4[src[k + 4]] + p5[src[k + 5]]
					+ p6[src[k + 6]] + src[k + 7]);
			k = k + 8;
		}
		return data;

	}
	
	public static byte[] bitmapToBWPix(Bitmap mBitmap) {
		int[] pixels = new int[mBitmap.getWidth() * mBitmap.getHeight()];
		byte[] data = new byte[mBitmap.getWidth() * mBitmap.getHeight()];

		mBitmap.getPixels(pixels, 0, mBitmap.getWidth(), 0, 0, mBitmap.getWidth(), mBitmap.getHeight());

		format_K_dither16x16(pixels, mBitmap.getWidth(), mBitmap.getHeight(), data);

		return data;
	}
	
	private static void format_K_dither16x16(int[] orgpixels, int xsize, int ysize, byte[] despixels) {
		int k = 0;
		for (int y = 0; y < ysize; y++) {
			for (int x = 0; x < xsize; x++) {
				if ((orgpixels[k] & 0xff) > Floyd16x16[x & 15][y & 15])
					despixels[k] = 0;
				else
					despixels[k] = 1;

				k++;
			}
		}
	}
	
	public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
	    Bitmap BitmapOrg = bitmap;
	    int width = BitmapOrg.getWidth();
	    int height = BitmapOrg.getHeight();
	    int newWidth = w;
	    int newHeight = h;

	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    Matrix matrix = new Matrix();
	    matrix.postScale(scaleWidth, scaleHeight);
	    Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0,0, width, height, matrix, true);
	    return resizedBitmap;
	}
	
	public static Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();
		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}
}
