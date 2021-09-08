package com.cristiancollazos.paybird.view.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cristiancollazos.paybird.R;
import com.cristiancollazos.paybird.repository.dto.CustomerDTO;
import com.cristiancollazos.paybird.repository.dto.MovementDTO;
import com.cristiancollazos.paybird.view.adapter.CreditMovementAdapter;
import com.cristiancollazos.paybird.view.adapter.CustomerSimpleAdapter;
import com.cristiancollazos.paybird.view.adapter.MovementsAdapter;
import com.cristiancollazos.paybird.view.adapter.interfaces.OnRecyclerItemActionListener;
import com.cristiancollazos.paybird.view.dialog.interfaces.OnFilterDialogConfirm;
import com.cristiancollazos.paybird.view.dialog.interfaces.OnSearchDialogAction;

import java.util.ArrayList;
import java.util.List;

public class MovementsDialog extends DialogFragment {

    private View objView;
    private RecyclerView rvMovementsList;
    private List<MovementDTO> lstMovementDTO;
    private RelativeLayout rlMovementsDialog_NO_CUSTOMERS_FOUND;
    private Integer nuCreditCode;
    private TextView tvMovementDialog_Title;

    public void setupDialog(List<MovementDTO> lstMovementDTO, Integer nuCreditCode) {
        this.lstMovementDTO = lstMovementDTO;
        this.nuCreditCode = nuCreditCode;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater objLayoutInflater = LayoutInflater.from(getActivity());
        objView = objLayoutInflater.inflate(R.layout.dialog_movements, null);
        rvMovementsList = objView.findViewById(R.id.rvMovementsList);
        rlMovementsDialog_NO_CUSTOMERS_FOUND = objView.findViewById(R.id.rlMovementsDialog_NO_CUSTOMERS_FOUND);
        tvMovementDialog_Title = objView.findViewById(R.id.tvMovementDialog_Title);

        LinearLayoutManager objManager = new LinearLayoutManager(getActivity());
        objManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvMovementsList.setLayoutManager(objManager);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder objBuilder = new AlertDialog.Builder(getActivity());
        objBuilder.setView(objView);
        return objBuilder.create();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (lstMovementDTO != null && lstMovementDTO.size() > 0) {
            CreditMovementAdapter objAdapter = new CreditMovementAdapter(lstMovementDTO);
            rvMovementsList.setAdapter(objAdapter);
            rlMovementsDialog_NO_CUSTOMERS_FOUND.setVisibility(View.GONE);
            rvMovementsList.setVisibility(View.VISIBLE);
        } else {
            rlMovementsDialog_NO_CUSTOMERS_FOUND.setVisibility(View.VISIBLE);
            rvMovementsList.setVisibility(View.GONE);
        }

        tvMovementDialog_Title.setText(
                getActivity().getResources().getString(R.string.creditmovements_title, nuCreditCode));
    }

}
