package com.cristiancollazos.paybird.misc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.Spanned;
import android.util.Log;
import android.view.View;

import com.cristiancollazos.paybird.R;
import com.cristiancollazos.paybird.misc.enums.ErrorConstants;
import com.cristiancollazos.paybird.view.activity.BaseActivity;
import com.cristiancollazos.paybird.view.dialog.interfaces.ConfirmDialogListener;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

import print.BluetoothPrinter;
import service.BluetoothService;

public class Utilities {

    private static final String TAG = Utilities.class.getSimpleName();
    private static AlertDialog objAlertDialog;
    private static Snackbar objSnackBar;

    public static void showMessageDialog(Context objContext,
                                         String sbMessage) {
        AlertDialog.Builder objAlertDialogBuilder = new AlertDialog.Builder(objContext)
                .setMessage(sbMessage)
                .setPositiveButton(R.string.gen_dialog_ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        objAlertDialog.dismiss();
                    }
                });

        objAlertDialog = objAlertDialogBuilder.create();
        objAlertDialog.show();
    }

    public static void showErrorMessageDialog(Context objContext,
                                              String sbMessage) {
        AlertDialog.Builder objAlertDialogBuilder = new AlertDialog.Builder(objContext)
                .setTitle(objContext.getString(R.string.gen_error_title))
                .setMessage(sbMessage)
                .setIcon(R.drawable.ic_error_outline_black_24dp)
                .setPositiveButton(R.string.gen_dialog_ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        objAlertDialog.dismiss();
                    }
                });

        objAlertDialog = objAlertDialogBuilder.create();
        objAlertDialog.show();
    }

    public static void showErrorMessageDialog(Context objContext,
                                              Spanned sbMessage) {
        AlertDialog.Builder objAlertDialogBuilder = new AlertDialog.Builder(objContext)
                .setTitle(objContext.getString(R.string.gen_error_title))
                .setMessage(sbMessage)
                .setIcon(R.drawable.ic_error_outline_black_24dp)
                .setPositiveButton(R.string.gen_dialog_ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        objAlertDialog.dismiss();
                    }
                });

        objAlertDialog = objAlertDialogBuilder.create();
        try {
            objAlertDialog.show();
        } catch (Exception objException) {
            Log.e(TAG, objException.getMessage());
        }
    }

    public static void showTitleMessageDialog(Context objContext,
                                              String sbTitle,
                                              String sbMessage) {
        AlertDialog.Builder objAlertDialogBuilder = new AlertDialog.Builder(objContext)
                .setTitle(sbTitle)
                .setMessage(sbMessage)
                .setPositiveButton(R.string.gen_dialog_ok_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        objAlertDialog.dismiss();
                    }
                });

        objAlertDialog = objAlertDialogBuilder.create();
        objAlertDialog.show();
    }

    public static void showConfirmationDialog(Context objContext,
                                              String sbMessage,
                                              final ConfirmDialogListener objListener,
                                              int nuDrawableId) {

        AlertDialog.Builder objAlertDialogBuilder = new AlertDialog.Builder(objContext)
                .setTitle(R.string.gen_app_name)
                .setIcon(nuDrawableId)
                .setMessage(sbMessage)
                .setPositiveButton(R.string.gen_dialog_confirm_button,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                objListener.onConfirm(objAlertDialog);
                            }
                        })
                .setNegativeButton(R.string.gen_dialog_cancel_button,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                objListener.onCancel(objAlertDialog);
                            }
                        });

        objAlertDialog = objAlertDialogBuilder.create();
        objAlertDialog.setCancelable(false);
        objAlertDialog.show();
    }

    public static void showSnackBar(View objView,
                                    String sbMessage) {
        objSnackBar = Snackbar.make(objView, sbMessage, Snackbar.LENGTH_LONG);
        objSnackBar.show();
    }

    public static void showIndefinitedSnackBar(View objView,
                                               String sbMessage) {
        objSnackBar = Snackbar.make(objView, sbMessage, Snackbar.LENGTH_INDEFINITE);
        objSnackBar.setAction(R.string.gen_dialog_ok_button, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                objSnackBar.dismiss();
            }
        });
        objSnackBar.show();
    }

    public static void showSnackBarWithFun(View objView,
                                           String sbMessage,
                                           String sbButtonText,
                                           View.OnClickListener objListener) {
        objSnackBar = Snackbar.make(objView, sbMessage, Snackbar.LENGTH_LONG);
        objSnackBar.setAction(sbButtonText, objListener);
        objSnackBar.show();
    }

    public static void doPrint(Context objContext, String sbDataToPrint) {
        Log.i(TAG, sbDataToPrint);
        new PrintTask().execute(objContext, sbDataToPrint);
    }

    private static class PrintTask extends AsyncTask<Object, Void, Context> {

        @Override
        protected Context doInBackground(Object... rcObjects) {
            try {
                Context objContext = (Context) rcObjects[0];
                String sbDataToPrint = (String) rcObjects[1];

                byte[] rcPrint = BluetoothPrinter.print(objContext, 0,
                        Constants.WIDTH_IMG_HEADER_PRINT, sbDataToPrint);
                objContext.sendBroadcast(new Intent().putExtra(BluetoothService.KEY_DATA_PRINT, rcPrint)
                        .setAction(BluetoothService.ACTION_PRINT));

                return objContext;
            } catch (Exception e) {
                Context objContext = (Context) rcObjects[0];
                showErrorMessageDialog(objContext, ErrorConstants.E0010.def() + ". " + e.getMessage());
                return objContext;
            }
        }

        @Override
        protected void onPostExecute(Context objContext) {
            super.onPostExecute(objContext);
            ((BaseActivity) objContext).disconectFromPrinter();
        }

    }

    public static String getFormattedValue(@NonNull Float flValue) {
        int nuValue = Math.round(flValue);

        if (nuValue > 0 && nuValue <= 999) {
            return "$ " + nuValue;
        } else if (nuValue > 999 && nuValue <= 9999) {
            return "$ " + String.valueOf(nuValue).substring(0, 1) + "."
                    + String.valueOf(nuValue).substring(1);
        } else if (nuValue > 9999 && nuValue <= 99999) {
            return "$ " + String.valueOf(nuValue).substring(0, 2) + "."
                    + String.valueOf(nuValue).substring(2);
        } else if (nuValue > 99999 && nuValue <= 999999) {
            return "$ " + String.valueOf(nuValue).substring(0, 3) + "."
                    + String.valueOf(nuValue).substring(3);
        } else if (nuValue > 999999 && nuValue <= 9999999) {
            return "$ " + String.valueOf(nuValue).substring(0, 1) + "."
                    + String.valueOf(nuValue).substring(1, 4) + "."
                    + String.valueOf(nuValue).substring(4);
        } else if (nuValue > 9999999 && nuValue <= 99999999) {
            return "$ " + String.valueOf(nuValue).substring(0, 2) + "."
                    + String.valueOf(nuValue).substring(2, 5) + "."
                    + String.valueOf(nuValue).substring(5);
        } else if (nuValue > 99999999 && nuValue <= 999999999) {
            return "$ " + String.valueOf(nuValue).substring(0, 3) + "."
                    + String.valueOf(nuValue).substring(3, 6) + "."
                    + String.valueOf(nuValue).substring(6);
        } else {
            return "$ " + nuValue;
        }
    }

    public static String getFormattedValueWoSign(@NonNull Float flValue) {
        int nuValue = Math.round(flValue);

        if (nuValue > 0 && nuValue <= 999) {
            return "" + nuValue;
        } else if (nuValue > 999 && nuValue <= 9999) {
            return "" + String.valueOf(nuValue).substring(0, 1) + "."
                    + String.valueOf(nuValue).substring(1);
        } else if (nuValue > 9999 && nuValue <= 99999) {
            return "" + String.valueOf(nuValue).substring(0, 2) + "."
                    + String.valueOf(nuValue).substring(2);
        } else if (nuValue > 99999 && nuValue <= 999999) {
            return "" + String.valueOf(nuValue).substring(0, 3) + "."
                    + String.valueOf(nuValue).substring(3);
        } else if (nuValue > 999999 && nuValue <= 9999999) {
            return "" + String.valueOf(nuValue).substring(0, 1) + "."
                    + String.valueOf(nuValue).substring(1, 4) + "."
                    + String.valueOf(nuValue).substring(4);
        } else if (nuValue > 9999999 && nuValue <= 99999999) {
            return "" + String.valueOf(nuValue).substring(0, 2) + "."
                    + String.valueOf(nuValue).substring(2, 5) + "."
                    + String.valueOf(nuValue).substring(5);
        } else if (nuValue > 99999999 && nuValue <= 999999999) {
            return "" + String.valueOf(nuValue).substring(0, 3) + "."
                    + String.valueOf(nuValue).substring(3, 6) + "."
                    + String.valueOf(nuValue).substring(6);
        } else {
            return "" + nuValue;
        }
    }

    public static Date getIncrementedDateWithDays(Date dtCreateDate, Integer nuIncrement) {
        Calendar objCalendar = Calendar.getInstance();
        objCalendar.setTime(dtCreateDate);
        objCalendar.add(Calendar.DATE, nuIncrement);
        return objCalendar.getTime();
    }

    public static Date getIncrementedDateWithMonths(Date dtCreateDate, Integer nuIncrement) {
        Calendar objCalendar = Calendar.getInstance();
        objCalendar.setTime(dtCreateDate);
        objCalendar.add(Calendar.MONTH, nuIncrement);
        return objCalendar.getTime();
    }

}