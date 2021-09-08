package com.cristiancollazos.paybird.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import com.cristiancollazos.paybird.R;
import com.cristiancollazos.paybird.view.dialog.interfaces.ConfirmDialogListener;

import java.util.Calendar;
import java.util.Date;

public class ChooseDateDialog extends DialogFragment {

    private View objView;
    private CalendarView calvChooseDateDialog;
    private Button btnChooseDate_Cancel, btnChooseDate_Confirm;
    private AlertDialog thisDialog;
    private ConfirmDialogListener objListener;
    private Calendar calCurrentDate;
    private Date dtCurrentSelectedDate, dtStartDate, dtMinDate, dtMaxDate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater objLayoutInflater = LayoutInflater.from(getActivity());
        objView = objLayoutInflater.inflate(R.layout.dialog_filterdate, null);

        calvChooseDateDialog = objView.findViewById(R.id.calvChooseDateDialog);
        btnChooseDate_Cancel = objView.findViewById(R.id.btnChooseDate_Cancel);
        btnChooseDate_Confirm = objView.findViewById(R.id.btnChooseDate_Confirm);
    }

    public void setupDialog(ConfirmDialogListener objListener) {
        this.objListener = objListener;
    }

    public void setupDialog(ConfirmDialogListener objListener,
                            Date dtStartDate) {
        this.objListener = objListener;
        this.dtStartDate = dtStartDate;
    }

    public void setupDialog(ConfirmDialogListener objListener,
                            Date dtStartDate,
                            Date dtMinDate,
                            Date dtMaxDate) {
        this.objListener = objListener;
        this.dtStartDate = dtStartDate;
        this.dtMinDate = dtMinDate;
        this.dtMaxDate = dtMaxDate;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder objBuilder = new AlertDialog.Builder(getActivity());
        objBuilder.setView(objView);
        thisDialog = objBuilder.create();
        return thisDialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (dtStartDate != null) {
            dtCurrentSelectedDate = dtStartDate;
        } else {
            dtCurrentSelectedDate = new Date();
        }

        calvChooseDateDialog.setDate(dtCurrentSelectedDate.getTime());

        if (dtMinDate != null) {
            calvChooseDateDialog.setMinDate(dtMinDate.getTime());
        }

        if (dtMaxDate != null) {
            calvChooseDateDialog.setMaxDate(dtMaxDate.getTime());
        }

        calvChooseDateDialog.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView objCalendarView,
                                            int nuYear, int nuMonth, int nuDay) {
                calCurrentDate = Calendar.getInstance();
                calCurrentDate.set(nuYear, nuMonth, nuDay);
                dtCurrentSelectedDate = calCurrentDate.getTime();
            }
        });

        btnChooseDate_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View objView) {
                objListener.onConfirm(thisDialog, dtCurrentSelectedDate);
            }
        });
        btnChooseDate_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View objView) {
                objListener.onCancel(thisDialog);
            }
        });
    }

}
