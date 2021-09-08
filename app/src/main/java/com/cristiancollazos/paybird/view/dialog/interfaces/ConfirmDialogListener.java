package com.cristiancollazos.paybird.view.dialog.interfaces;

import android.app.AlertDialog;

public interface ConfirmDialogListener {

    void onConfirm(AlertDialog objAlertDialog, Object... rcDialogData);

    void onCancel(AlertDialog objAlertDialog);

}
