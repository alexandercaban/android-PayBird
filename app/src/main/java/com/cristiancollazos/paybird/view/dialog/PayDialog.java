package com.cristiancollazos.paybird.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cristiancollazos.paybird.R;
import com.cristiancollazos.paybird.misc.Constants;
import com.cristiancollazos.paybird.misc.interfaces.BluetoothPrinterNotifier;
import com.cristiancollazos.paybird.repository.dto.PendingPaymentDTO;
import com.cristiancollazos.paybird.view.activity.BaseActivity;
import com.cristiancollazos.paybird.view.dialog.interfaces.ConfirmDialogListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PayDialog extends DialogFragment implements BluetoothPrinterNotifier,
        CalendarView.OnDateChangeListener {

    private final String TAG = PayDialog.class.getSimpleName();
    private View objView;
    private EditText etPayDialog_ValueToPay;
    private CalendarView dpPayDialog_NextPayment;
    private PendingPaymentDTO objPendingPaymentDTO;
    private TextView tvPayDialog_Title;
    private ConfirmDialogListener objListener;
    private Button btnPayDialog_Confirm, btnPayDialog_Cancel;
    private AlertDialog objThisDialog;
    private RelativeLayout rlPayments_PrinterConnected, rlPayments_PrinterConnecting,
            rlPayments_PrinterDisconnected;
    private Date dtCurrentSelectedDate;

    public void initializeDialog(PendingPaymentDTO objPendingPaymentDTO,
                                 ConfirmDialogListener objListener) {
        this.objPendingPaymentDTO = objPendingPaymentDTO;
        this.objListener = objListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater objInflater = LayoutInflater.from(getActivity());
        objView = objInflater.inflate(R.layout.dialog_pay, null);

        etPayDialog_ValueToPay = objView.findViewById(R.id.etPayDialog_ValueToPay);
        dpPayDialog_NextPayment = objView.findViewById(R.id.dpPayDialog_NextPayment);
        tvPayDialog_Title = objView.findViewById(R.id.tvPayDialog_Title);
        btnPayDialog_Confirm = objView.findViewById(R.id.btnPayDialog_Confirm);
        btnPayDialog_Cancel = objView.findViewById(R.id.btnPayDialog_Cancel);
        rlPayments_PrinterConnected = objView.findViewById(R.id.rlPayments_PrinterConnected);
        rlPayments_PrinterConnecting = objView.findViewById(R.id.rlPayments_PrinterConnecting);
        rlPayments_PrinterDisconnected = objView.findViewById(R.id.rlPayments_PrinterDisconnected);

        dpPayDialog_NextPayment.setOnDateChangeListener(this);
        rlPayments_PrinterConnected.setVisibility(View.GONE);
        rlPayments_PrinterConnecting.setVisibility(View.GONE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder objBuilder = new AlertDialog.Builder(getActivity());
        objBuilder.setView(objView);
        objThisDialog = objBuilder.create();
        objThisDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        objThisDialog.setCancelable(false);
        return objThisDialog;
    }

    @Override
    public void onStart() {
        super.onStart();
        tvPayDialog_Title.setText(getResources().getString(R.string.todaypayments_dopay_title,
                objPendingPaymentDTO.getNuCode()));
        etPayDialog_ValueToPay.setText(null);

        btnPayDialog_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View objView) {
                if (!etPayDialog_ValueToPay.getText().toString().isEmpty()) {
                    Integer nuValueToPay = Integer.valueOf(etPayDialog_ValueToPay.getText().toString());
                    if (nuValueToPay > 0) {
                        if (nuValueToPay <= objPendingPaymentDTO.getFlRemainder()) {
                            objListener.onConfirm(objThisDialog, etPayDialog_ValueToPay, dtCurrentSelectedDate);
                        } else {
                            etPayDialog_ValueToPay.setError(getResources().getString(R.string.todaypayments_dopay_error2));
                        }
                    } else {
                        etPayDialog_ValueToPay.setError(getResources().getString(R.string.todaypayments_dopay_error1));
                    }
                } else {
                    etPayDialog_ValueToPay.setError(getResources().getString(R.string.todaypayments_dopay_error1));
                }
            }
        });

        btnPayDialog_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View objView) {
                objListener.onCancel(objThisDialog);
                ((BaseActivity) getActivity()).disconectFromPrinter();
            }
        });

        Calendar objCalendar = Calendar.getInstance();
        objCalendar.setTime(objPendingPaymentDTO.getDtNextPayment());

        if (objPendingPaymentDTO.getNuPeriod() == Constants.TIEMPOPAGO_DIARIO) {
            objCalendar.add(Calendar.DATE, 1);
        } else if (objPendingPaymentDTO.getNuPeriod() == Constants.TIEMPOPAGO_SEMANAL) {
            objCalendar.add(Calendar.DATE, 7);
        } else if (objPendingPaymentDTO.getNuPeriod() == Constants.TIEMPOPAGO_QUINCENAL) {
            objCalendar.add(Calendar.DATE, 14);
        } else {
            objCalendar.add(Calendar.MONTH, 1);
        }

        Date dtNextPayment = objCalendar.getTime();

        objCalendar = Calendar.getInstance();
        objCalendar.add(Calendar.DATE, 1);
        Date dtTomorrow = objCalendar.getTime();

        dtCurrentSelectedDate = dtNextPayment;
        dpPayDialog_NextPayment.setDate(dtNextPayment.getTime());
        dpPayDialog_NextPayment.setMinDate(dtTomorrow.getTime());

        ((BaseActivity) getActivity()).getPrinterMacAndConnect(PayDialog.this);
    }

    @Override
    public void onDisconnected() {
        Log.i(TAG, "Disconnected");
        rlPayments_PrinterDisconnected.setVisibility(View.VISIBLE);
        rlPayments_PrinterConnecting.setVisibility(View.GONE);
        rlPayments_PrinterConnected.setVisibility(View.GONE);
    }

    @Override
    public void onConnecting() {
        Log.i(TAG, "Connecting");
        rlPayments_PrinterDisconnected.setVisibility(View.GONE);
        rlPayments_PrinterConnecting.setVisibility(View.VISIBLE);
        rlPayments_PrinterConnected.setVisibility(View.GONE);
    }

    @Override
    public void onConnected() {
        Log.i(TAG, "Connected");
        rlPayments_PrinterDisconnected.setVisibility(View.GONE);
        rlPayments_PrinterConnecting.setVisibility(View.GONE);
        rlPayments_PrinterConnected.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView calendarView, int nuYear,
                                    int nuMonth, int nuDay) {
        Calendar calCurrentDate = Calendar.getInstance();
        calCurrentDate.set(nuYear, nuMonth, nuDay);
        dtCurrentSelectedDate = calCurrentDate.getTime();
    }

}
