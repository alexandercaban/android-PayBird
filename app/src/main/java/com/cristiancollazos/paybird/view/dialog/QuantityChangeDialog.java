package com.cristiancollazos.paybird.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.cristiancollazos.paybird.R;
import com.cristiancollazos.paybird.view.dialog.interfaces.ConfirmDialogListener;

public class QuantityChangeDialog extends DialogFragment {

    private View objView;
    private EditText etQuantityDialog_Quantity;
    private int nuWidth;
    private Integer nuQuantity;
    private ConfirmDialogListener objListener;
    private Button btnQuantityDialog_Confirm;
    private AlertDialog objThisDialog;

    public void setupDialog(ConfirmDialogListener objListener, Integer nuQuantity) {
        this.nuQuantity = nuQuantity;
        this.objListener = objListener;
    }

    @Override
    public void onCreate(@Nullable Bundle objSavedInstanceState) {
        super.onCreate(objSavedInstanceState);

        LayoutInflater objLayoutInflater = LayoutInflater.from(getActivity());
        objView = objLayoutInflater.inflate(R.layout.dialog_quantitychange, null);
        objView.measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        nuWidth = objView.getMeasuredWidth();
        etQuantityDialog_Quantity = objView.findViewById(R.id.etQuantityDialog_Quantity);
        btnQuantityDialog_Confirm = objView.findViewById(R.id.btnQuantityDialog_Confirm);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle objSavedInstanceState) {
        final AlertDialog.Builder objBuilder = new AlertDialog.Builder(getActivity());
        objBuilder.setView(objView);
        objThisDialog = objBuilder.create();
        return objThisDialog;
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = nuWidth;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        etQuantityDialog_Quantity.setText(String.valueOf(nuQuantity));
        etQuantityDialog_Quantity.requestFocus();
        objThisDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        btnQuantityDialog_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etQuantityDialog_Quantity.getText().toString().isEmpty()) {
                    etQuantityDialog_Quantity.setError(
                            getActivity().getResources().getString(R.string.gen_error_mandatory));
                } else {
                    nuQuantity = Integer.valueOf(etQuantityDialog_Quantity.getText().toString());

                    if (nuQuantity != null && nuQuantity > 0) {
                        objListener.onConfirm(objThisDialog, nuQuantity);
                    } else {
                        etQuantityDialog_Quantity.setError(
                                getActivity().getResources().getString(R.string.createcredit_wrongquantity));
                    }
                }
            }
        });

        super.onResume();
    }
}
