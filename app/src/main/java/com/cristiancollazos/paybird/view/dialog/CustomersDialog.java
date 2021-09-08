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

import com.cristiancollazos.paybird.R;
import com.cristiancollazos.paybird.repository.dto.CustomerDTO;
import com.cristiancollazos.paybird.repository.dto.RouteDTO;
import com.cristiancollazos.paybird.view.activity.BaseActivity;
import com.cristiancollazos.paybird.view.adapter.CustomerSimpleAdapter;
import com.cristiancollazos.paybird.view.adapter.interfaces.OnRecyclerItemActionListener;
import com.cristiancollazos.paybird.view.dialog.interfaces.OnFilterDialogConfirm;
import com.cristiancollazos.paybird.view.dialog.interfaces.OnSearchDialogAction;

import java.util.List;

public class CustomersDialog extends DialogFragment
        implements OnRecyclerItemActionListener<CustomerDTO>, View.OnClickListener {

    private View objView;
    private RecyclerView rvCustomersList;
    private List<CustomerDTO> lstCustomersDTO;
    private OnRecyclerItemActionListener<CustomerDTO> objListener;
    private OnSearchDialogAction objSearchListener;
    private ImageButton btnCustomerDialog_Filter, btnCustomerDialog_CancelFilter;
    private Boolean blMakeSeachVisible = false;
    private RelativeLayout rlCustomerDialog_NO_CUSTOMERS_FOUND;

    public void setupDialog(List<CustomerDTO> lstCustomersDTO,
                            OnRecyclerItemActionListener<CustomerDTO> objListener) {
        this.lstCustomersDTO = lstCustomersDTO;
        this.objListener = objListener;
        this.blMakeSeachVisible = false;
    }

    public void setupDialog(List<CustomerDTO> lstCustomersDTO,
                            OnRecyclerItemActionListener<CustomerDTO> objListener,
                            OnSearchDialogAction objSearchListener) {
        this.lstCustomersDTO = lstCustomersDTO;
        this.objListener = objListener;
        this.objSearchListener = objSearchListener;
        this.blMakeSeachVisible = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater objLayoutInflater = LayoutInflater.from(getActivity());
        objView = objLayoutInflater.inflate(R.layout.dialog_customers, null);
        rvCustomersList = objView.findViewById(R.id.rvCustomersList);
        btnCustomerDialog_Filter = objView.findViewById(R.id.btnCustomerDialog_Filter);
        btnCustomerDialog_CancelFilter = objView.findViewById(R.id.btnCustomerDialog_CancelFilter);
        rlCustomerDialog_NO_CUSTOMERS_FOUND = objView.findViewById(R.id.rlCustomerDialog_NO_CUSTOMERS_FOUND);

        btnCustomerDialog_Filter.setOnClickListener(this);
        btnCustomerDialog_CancelFilter.setOnClickListener(this);

        LinearLayoutManager objManager = new LinearLayoutManager(getActivity());
        objManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCustomersList.setLayoutManager(objManager);
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

        if (lstCustomersDTO != null && lstCustomersDTO.size() > 0) {
            rlCustomerDialog_NO_CUSTOMERS_FOUND.setVisibility(View.GONE);
            rvCustomersList.setVisibility(View.VISIBLE);

            CustomerSimpleAdapter objAdapter
                    = new CustomerSimpleAdapter(lstCustomersDTO, this);
            rvCustomersList.setAdapter(objAdapter);
        } else {
            rlCustomerDialog_NO_CUSTOMERS_FOUND.setVisibility(View.VISIBLE);
            rvCustomersList.setVisibility(View.GONE);
        }

        if (blMakeSeachVisible) {
            btnCustomerDialog_Filter.setVisibility(View.VISIBLE);
            btnCustomerDialog_CancelFilter.setVisibility(View.GONE);
        } else {
            btnCustomerDialog_Filter.setVisibility(View.GONE);
            btnCustomerDialog_CancelFilter.setVisibility(View.GONE);
        }
    }

    @Override
    public void OnAction(CustomerDTO objItem, Integer nuPosition) {
        objListener.OnAction(objItem, nuPosition);
        dismiss();
    }

    @Override
    public void onClick(View objView) {
        switch (objView.getId()) {
            case R.id.btnCustomerDialog_CancelFilter:
                this.btnCustomerDialog_CancelFilter.setVisibility(View.GONE);
                this.btnCustomerDialog_Filter.setVisibility(View.VISIBLE);
                objSearchListener.OnCancelSearch();
                break;
            case R.id.btnCustomerDialog_Filter:
                this.btnCustomerDialog_CancelFilter.setVisibility(View.VISIBLE);
                this.btnCustomerDialog_Filter.setVisibility(View.GONE);
                FilterDialog objFilterDialog = new FilterDialog();
                objFilterDialog.setupDialog(
                        R.drawable.ic_people_black_24dp,
                        getResources().getString(R.string.filterdialog_filterby_customername),
                        FilterDialog.FILTER_CHAR,
                        new OnFilterDialogConfirm() {
                            @Override
                            public void onFilter(String sbFilterValue) {
                                objSearchListener.OnSearch(sbFilterValue);
                            }
                        });
                objFilterDialog.show(getFragmentManager(), "FilterDialog");
                break;
        }
    }

    public void refreshCustomers(List<CustomerDTO> lstCustomersDTO) {
        rlCustomerDialog_NO_CUSTOMERS_FOUND.setVisibility(View.GONE);
        rvCustomersList.setVisibility(View.VISIBLE);

        this.lstCustomersDTO.clear();
        this.lstCustomersDTO.addAll(lstCustomersDTO);
        rvCustomersList.getAdapter().notifyDataSetChanged();
    }

    public void notifyNoDataFound() {
        rlCustomerDialog_NO_CUSTOMERS_FOUND.setVisibility(View.VISIBLE);
        rvCustomersList.setVisibility(View.GONE);
    }

}
