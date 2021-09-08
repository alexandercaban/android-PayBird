package com.cristiancollazos.paybird.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.cristiancollazos.paybird.R;
import com.cristiancollazos.paybird.misc.Utilities;
import com.cristiancollazos.paybird.view.dialog.interfaces.OnFilterDialogConfirm;

public class FilterDialog extends DialogFragment {

    public static final int FILTER_NUMERIC = 0;
    public static final int FILTER_CHAR = 1;

    private View objView;
    private EditText etFilterDialog_FilterValueChar, etFilterDialog_FilterValueNumeric;
    private TextInputLayout tilFilterDialogChar, tilFilterDialogNumeric;
    private Button btnFilterDialog_DoFilter;
    private OnFilterDialogConfirm objListener;
    private AlertDialog objThisDialog;

    private int nuDrawable, nuInputType;
    private String sbInputLabel;

    public void setupDialog(int nuDrawable,
                            String sbInputLabel,
                            int nuInputType,
                            OnFilterDialogConfirm objListener) {
        this.nuDrawable = nuDrawable;
        this.nuInputType = nuInputType;
        this.sbInputLabel = sbInputLabel;
        this.objListener = objListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater objLayoutInflater = LayoutInflater.from(getContext());
        objView = objLayoutInflater.inflate(R.layout.dialog_filtertext, null);
        etFilterDialog_FilterValueChar = objView.findViewById(R.id.etFilterDialog_FilterValueChar);
        etFilterDialog_FilterValueNumeric = objView.findViewById(R.id.etFilterDialog_FilterValueNumeric);
        btnFilterDialog_DoFilter = objView.findViewById(R.id.btnFilterDialog_DoFilter);
        tilFilterDialogChar = objView.findViewById(R.id.tilFilterDialogChar);
        tilFilterDialogNumeric = objView.findViewById(R.id.tilFilterDialogNumeric);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder objBuilder = new AlertDialog.Builder(getContext());
        objBuilder.setView(objView);
        objThisDialog = objBuilder.create();
        objThisDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return objThisDialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        switch (nuInputType) {
            case FILTER_CHAR:
                tilFilterDialogChar.setVisibility(View.VISIBLE);
                tilFilterDialogNumeric.setVisibility(View.GONE);
                etFilterDialog_FilterValueChar.setHint(sbInputLabel);
                etFilterDialog_FilterValueChar
                        .setCompoundDrawablesWithIntrinsicBounds(nuDrawable, 0, 0, 0);
                btnFilterDialog_DoFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View objView) {
                        if (etFilterDialog_FilterValueChar.getText().toString().isEmpty()) {
                            etFilterDialog_FilterValueChar.setError(getContext().getString(R.string.gen_error_mandatory));
                        } else {
                            objListener.onFilter(etFilterDialog_FilterValueChar.getText().toString());
                            objThisDialog.dismiss();
                        }
                    }
                });
                break;

            case FILTER_NUMERIC:
                tilFilterDialogNumeric.setVisibility(View.VISIBLE);
                tilFilterDialogChar.setVisibility(View.GONE);
                etFilterDialog_FilterValueNumeric.setHint(sbInputLabel);
                etFilterDialog_FilterValueNumeric
                        .setCompoundDrawablesWithIntrinsicBounds(nuDrawable, 0, 0, 0);
                btnFilterDialog_DoFilter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View objView) {
                        if (etFilterDialog_FilterValueNumeric.getText().toString().isEmpty()) {
                            etFilterDialog_FilterValueNumeric.setError(getContext().getString(R.string.gen_error_mandatory));
                        } else {
                            objListener.onFilter(etFilterDialog_FilterValueNumeric.getText().toString());
                            objThisDialog.dismiss();
                        }
                    }
                });
                break;
        }


    }

}
