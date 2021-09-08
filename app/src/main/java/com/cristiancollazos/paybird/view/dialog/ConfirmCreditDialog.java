package com.cristiancollazos.paybird.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.cristiancollazos.paybird.R;
import com.cristiancollazos.paybird.misc.Constants;
import com.cristiancollazos.paybird.misc.Utilities;
import com.cristiancollazos.paybird.misc.interfaces.BluetoothPrinterNotifier;
import com.cristiancollazos.paybird.repository.dto.CreditDTO;
import com.cristiancollazos.paybird.repository.dto.CustomerDTO;
import com.cristiancollazos.paybird.repository.dto.RouteItemDTO;
import com.cristiancollazos.paybird.view.activity.BaseActivity;
import com.cristiancollazos.paybird.view.adapter.SimpleAdapter;
import com.cristiancollazos.paybird.view.dialog.interfaces.ConfirmDialogListener;
import com.cristiancollazos.paybird.view.dialog.interfaces.OnDialogDismissNotifier;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ConfirmCreditDialog extends DialogFragment implements BluetoothPrinterNotifier {

    private final String TAG = ConfirmCreditDialog.class.getSimpleName();
    private AlertDialog objThisDialog;
    private View objView;
    private CustomerDTO objCustomerDTO;
    private CreditDTO objCreditDTO;
    private Date dtDueDate;
    private TextView tvConfirmCredit_Customer, tvConfirmCredit_Created, tvConfirmCredit_Due,
            tvConfirmCredit_Length, tvConfirmCredit_Period, tvConfirmCredit_NextPayment,
            tvConfirmCredit_CreditValue, tvConfirmCredit_Interest, tvConfirmCredit_Remainder,
            tvConfirmCredit_QuantityInstallment;
    private Button btnConfirmCreditDialog_Cancel, btnConfirmCreditDialog_Confirm;
    private RelativeLayout rlConfirmCredit_PrinterConnected, rlConfirmCredit_PrinterConnecting,
            rlConfirmCredit_PrinterDisconnected;
    private ConfirmDialogListener objListener;
    private OnDialogDismissNotifier objDismissListener;
    private Spinner spConfirmCredit_Order;
    private List<RouteItemDTO> lstRouteItems;

    public void setupDialog(CustomerDTO objCustomerDTO,
                            CreditDTO objCreditDTO,
                            Date dtDueDate,
                            ConfirmDialogListener objListener,
                            List<RouteItemDTO> lstRouteItems,
                            OnDialogDismissNotifier objDismissListener) {
        this.dtDueDate = dtDueDate;
        this.objCustomerDTO = objCustomerDTO;
        this.objCreditDTO = objCreditDTO;
        this.objListener = objListener;
        this.lstRouteItems = lstRouteItems;
        this.objDismissListener = objDismissListener;
    }

    @Override
    public void onCreate(@Nullable Bundle objSavedInstanceState) {
        super.onCreate(objSavedInstanceState);

        LayoutInflater objLayoutInflater = LayoutInflater.from(getActivity());
        objView = objLayoutInflater.inflate(R.layout.dialog_confirmcredit, null);

        tvConfirmCredit_Customer = objView.findViewById(R.id.tvConfirmCredit_Customer);
        tvConfirmCredit_Created = objView.findViewById(R.id.tvConfirmCredit_Created);
        tvConfirmCredit_Due = objView.findViewById(R.id.tvConfirmCredit_Due);
        tvConfirmCredit_Length = objView.findViewById(R.id.tvConfirmCredit_Length);
        tvConfirmCredit_Period = objView.findViewById(R.id.tvConfirmCredit_Period);
        tvConfirmCredit_NextPayment = objView.findViewById(R.id.tvConfirmCredit_NextPayment);
        tvConfirmCredit_CreditValue = objView.findViewById(R.id.tvConfirmCredit_CreditValue);
        tvConfirmCredit_Interest = objView.findViewById(R.id.tvConfirmCredit_Interest);
        tvConfirmCredit_Remainder = objView.findViewById(R.id.tvConfirmCredit_Remainder);
        tvConfirmCredit_QuantityInstallment = objView.findViewById(R.id.tvConfirmCredit_QuantityInstallment);
        btnConfirmCreditDialog_Cancel = objView.findViewById(R.id.btnConfirmCreditDialog_Cancel);
        btnConfirmCreditDialog_Confirm = objView.findViewById(R.id.btnConfirmCreditDialog_Confirm);
        spConfirmCredit_Order = objView.findViewById(R.id.spConfirmCredit_Order);
        rlConfirmCredit_PrinterDisconnected = objView.findViewById(R.id.rlConfirmCredit_PrinterDisconnected);
        rlConfirmCredit_PrinterConnecting = objView.findViewById(R.id.rlConfirmCredit_PrinterConnecting);
        rlConfirmCredit_PrinterConnected = objView.findViewById(R.id.rlConfirmCredit_PrinterConnected);

        rlConfirmCredit_PrinterConnecting.setVisibility(View.GONE);
        rlConfirmCredit_PrinterConnected.setVisibility(View.GONE);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle objSavedInstanceState) {
        AlertDialog.Builder objBuilder = new AlertDialog.Builder(getActivity());
        objBuilder.setView(objView);
        objBuilder.setCancelable(false);
        objThisDialog = objBuilder.create();
        return objThisDialog;
    }

    @Override
    public void onStart() {
        super.onStart();

        SimpleDateFormat objFormatter = new SimpleDateFormat("dd/MM/yyyy");

        tvConfirmCredit_Customer.setText(objCustomerDTO.getSbName() + " "
                + objCustomerDTO.getSbLastName());
        tvConfirmCredit_Created.setText(objFormatter.format(objCreditDTO.getDtCreatedDate()));
        tvConfirmCredit_Due.setText(objFormatter.format(dtDueDate));
        tvConfirmCredit_Length.setText(String.valueOf(objCreditDTO.getNuLength()));

        String sbPeriod;
        if (objCreditDTO.getNuPeriod() == Constants.TIEMPOPAGO_DIARIO) {
            sbPeriod = "Diario";
        } else if (objCreditDTO.getNuPeriod() == Constants.TIEMPOPAGO_SEMANAL) {
            sbPeriod = "Semanal";
        } else if (objCreditDTO.getNuPeriod() == Constants.TIEMPOPAGO_QUINCENAL) {
            sbPeriod = "Quincenal";
        } else {
            sbPeriod = "Mensual";
        }
        tvConfirmCredit_Period.setText(sbPeriod);

        tvConfirmCredit_NextPayment.setText(objFormatter.format(objCreditDTO.getDtNextPayment()));
        tvConfirmCredit_CreditValue.setText(Utilities.getFormattedValue(objCreditDTO.getFlValue()));
        tvConfirmCredit_Interest.setText(String.valueOf(objCreditDTO.getFlInterest()));
        tvConfirmCredit_Remainder.setText(Utilities.getFormattedValue(objCreditDTO.getFlRemainder()));
        tvConfirmCredit_QuantityInstallment.setText(
                getActivity().getResources().getString(R.string.confirmcredit_quantityinstallment,
                        objCreditDTO.getNuInstallmentQuantity(),
                        Utilities.getFormattedValue(objCreditDTO.getFlInstallmentValue())));

        btnConfirmCreditDialog_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View objView) {
                objListener.onCancel(objThisDialog);
            }
        });

        btnConfirmCreditDialog_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View objView) {
                RouteItemDTO obRouteItemDTO = (RouteItemDTO) spConfirmCredit_Order.getSelectedItem();
                Integer nuRouteOrderPosition = obRouteItemDTO.getNuOrder();
                objListener.onConfirm(objThisDialog, nuRouteOrderPosition);
            }
        });

        SimpleAdapter<RouteItemDTO> objAdapter = new SimpleAdapter<>(getActivity(),
                lstRouteItems, true);
        spConfirmCredit_Order.setAdapter(objAdapter);

        ((BaseActivity) getActivity()).getPrinterMacAndConnect(ConfirmCreditDialog.this);
    }

    @Override
    public void onDismiss(DialogInterface objDialogInterface) {
        super.onDismiss(objDialogInterface);
        objDismissListener.OnDialogDismiss();
    }

    @Override
    public void onDisconnected() {
        Log.i(TAG, "Disconnected");
        rlConfirmCredit_PrinterDisconnected.setVisibility(View.VISIBLE);
        rlConfirmCredit_PrinterConnecting.setVisibility(View.GONE);
        rlConfirmCredit_PrinterConnected.setVisibility(View.GONE);
    }

    @Override
    public void onConnecting() {
        Log.i(TAG, "Connecting");
        rlConfirmCredit_PrinterDisconnected.setVisibility(View.GONE);
        rlConfirmCredit_PrinterConnecting.setVisibility(View.VISIBLE);
        rlConfirmCredit_PrinterConnected.setVisibility(View.GONE);
    }

    @Override
    public void onConnected() {
        Log.i(TAG, "Connected");
        rlConfirmCredit_PrinterDisconnected.setVisibility(View.GONE);
        rlConfirmCredit_PrinterConnecting.setVisibility(View.GONE);
        rlConfirmCredit_PrinterConnected.setVisibility(View.VISIBLE);
    }

}
