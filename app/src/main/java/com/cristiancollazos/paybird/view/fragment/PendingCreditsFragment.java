package com.cristiancollazos.paybird.view.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.cristiancollazos.paybird.R;
import com.cristiancollazos.paybird.misc.Constants;
import com.cristiancollazos.paybird.print.PaymentPrint;
import com.cristiancollazos.paybird.misc.Utilities;
import com.cristiancollazos.paybird.presenter.PendingCreditsPresenter;
import com.cristiancollazos.paybird.repository.dto.PendingPaymentDTO;
import com.cristiancollazos.paybird.repository.dto.RouteDTO;
import com.cristiancollazos.paybird.view.activity.BaseActivity;
import com.cristiancollazos.paybird.view.adapter.PendingPaymentAdapter;
import com.cristiancollazos.paybird.view.adapter.interfaces.OnRecyclerItemActionListener;
import com.cristiancollazos.paybird.view.dialog.FilterDialog;
import com.cristiancollazos.paybird.view.dialog.PayDialog;
import com.cristiancollazos.paybird.view.dialog.interfaces.ConfirmDialogListener;
import com.cristiancollazos.paybird.view.dialog.interfaces.OnFilterDialogConfirm;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PendingCreditsFragment extends Fragment implements PendingCreditsPresenter.View,
        OnRecyclerItemActionListener<PendingPaymentDTO>, SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener {

    private final String TAG = PendingCreditsFragment.class.getSimpleName();
    private SwipeRefreshLayout srvPendingCredits;
    private RecyclerView rvPendingCredits;
    private PendingCreditsPresenter objPresenter;
    private List<PendingPaymentDTO> lstPendingPaymentDTO;
    private ImageButton btnPendingCredits_FilterByCreditCode, btnPendingCredits_FilterByCustomer;
    private RelativeLayout rlPendingCredits_NO_DATA_FOUND;

    private void setBaseEnvironment() {
        ((BaseActivity) getActivity()).setToolbarVisible(true);
        ((BaseActivity) getActivity()).setDrawerEnabled(true);
        ((BaseActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.pendingcredits_menu_title));
        ((BaseActivity) getActivity()).setActionBarSubtitle(null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater objInflater,
                             @Nullable ViewGroup objContainer,
                             @Nullable Bundle objSavedInstanceState) {
        setBaseEnvironment();

        View objView = objInflater.inflate(R.layout.fragment_pendingcredits, null);

        srvPendingCredits = objView.findViewById(R.id.srvPendingCredits);
        rvPendingCredits = objView.findViewById(R.id.rvPendingCredits);
        btnPendingCredits_FilterByCreditCode = objView.findViewById(R.id.btnPendingCredits_FilterByCreditCode);
        btnPendingCredits_FilterByCustomer = objView.findViewById(R.id.btnPendingCredits_FilterByCustomer);
        rlPendingCredits_NO_DATA_FOUND = objView.findViewById(R.id.rlPendingCredits_NO_DATA_FOUND);

        lstPendingPaymentDTO = new ArrayList<>();
        PendingPaymentAdapter objAdapter = new PendingPaymentAdapter(lstPendingPaymentDTO, this);
        LinearLayoutManager objLayoutManager = new LinearLayoutManager(getActivity());
        objLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvPendingCredits.setLayoutManager(objLayoutManager);
        rvPendingCredits.setAdapter(objAdapter);
        srvPendingCredits.setOnRefreshListener(this);

        btnPendingCredits_FilterByCreditCode.setOnClickListener(this);
        btnPendingCredits_FilterByCustomer.setOnClickListener(this);

        initializeFragment();
        return objView;
    }

    public void initializeFragment() {
        objPresenter = new PendingCreditsPresenter(this);
        RouteDTO objRouteDTO = ((BaseActivity) getActivity()).getCurrentSelectedRoute();
        objPresenter.getPendingCreditsByFilters(objRouteDTO.getNuCode(), null, null);
    }

    public void getPendingCreditsAtRouteChange() {
        RouteDTO objRouteDTO = ((BaseActivity) getActivity()).getCurrentSelectedRoute();
        objPresenter.getPendingCreditsByFilters(objRouteDTO.getNuCode(), null, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!((BaseActivity) getActivity())
                .isFragmentAlreadyCreated(PendingCreditsFragment.class.getSimpleName())) {
            Constants.FRAGMENTS_CREATED.add(PendingCreditsFragment.class.getSimpleName());
        }
        ((BaseActivity) getActivity()).setActiveMenuItem(PendingCreditsFragment.class.getSimpleName());
    }

    @Override
    public void OnAction(final PendingPaymentDTO objItem, final Integer nuPosition) {
        final PayDialog objPayDialog = new PayDialog();
        objPayDialog.initializeDialog(objItem, new ConfirmDialogListener() {
            @Override
            public void onConfirm(AlertDialog objAlertDialog, Object... rcDialogData) {
                Integer nuRouteCode = ((BaseActivity) getActivity()).getCurrentSelectedRoute().getNuCode();
                Integer nuOrder = objItem.getNuOrder();
                Integer nuValueToPay = Integer.valueOf(((EditText) rcDialogData[0]).getText().toString());
                Date dtNextPayment = (Date) rcDialogData[1];
                objPresenter.doPayPendingPayment(nuRouteCode, nuOrder, nuValueToPay, dtNextPayment, nuPosition);
                objAlertDialog.dismiss();
            }

            @Override
            public void onCancel(AlertDialog objAlertDialog) {
                objAlertDialog.dismiss();
            }
        });
        objPayDialog.show(getFragmentManager(), "PayDialog");
    }

    @Override
    public void onClick(View objView) {
        FilterDialog objFilterDialog = new FilterDialog();

        switch (objView.getId()) {
            case R.id.btnPendingCredits_FilterByCreditCode:
                objFilterDialog.setupDialog(R.drawable.ic_local_atm_black_24dp,
                        getResources().getString(R.string.filterdialog_filterby_creditcode),
                        FilterDialog.FILTER_NUMERIC,
                        new OnFilterDialogConfirm() {
                            @Override
                            public void onFilter(String sbFilterValue) {
                                RouteDTO objRouteDTO = ((BaseActivity) getActivity()).getCurrentSelectedRoute();
                                objPresenter.getPendingCreditsByFilters(objRouteDTO.getNuCode(), null,
                                        Integer.parseInt(sbFilterValue));
                            }
                        });
                objFilterDialog.show(getFragmentManager(), "FilterDialog");
                break;

            case R.id.btnPendingCredits_FilterByCustomer:
                objFilterDialog.setupDialog(R.drawable.ic_people_black_24dp,
                        getResources().getString(R.string.filterdialog_filterby_customername),
                        FilterDialog.FILTER_CHAR,
                        new OnFilterDialogConfirm() {
                            @Override
                            public void onFilter(String sbFilterValue) {
                                RouteDTO objRouteDTO = ((BaseActivity) getActivity()).getCurrentSelectedRoute();
                                objPresenter.getPendingCreditsByFilters(objRouteDTO.getNuCode(), sbFilterValue,
                                        null);
                            }
                        });
                objFilterDialog.show(getFragmentManager(), "FilterDialog");
                break;
        }
    }

    @Override
    public void showProgress() {
        ((BaseActivity) getActivity()).showProgressBar();
    }

    @Override
    public void hideProgress() {
        ((BaseActivity) getActivity()).hideProgressBar();
        srvPendingCredits.setRefreshing(false);
    }

    @Override
    public void showErrorMessage(Integer nuCode, String sbError, String sbRecommend) {
        ((BaseActivity) getActivity()).showErrorMessage(sbError, sbRecommend);
    }

    @Override
    public void showSuccessMessage(String sbMessage) {
        Utilities.showSnackBar(getView(), sbMessage);
    }

    @Override
    public void renderPendingCredits(List<PendingPaymentDTO> lstPendingPaymentDTO) {
        this.lstPendingPaymentDTO.clear();
        this.lstPendingPaymentDTO.addAll(lstPendingPaymentDTO);
        rlPendingCredits_NO_DATA_FOUND.setVisibility(View.GONE);
        rvPendingCredits.setVisibility(View.VISIBLE);
        rvPendingCredits.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void notifyPendingNotFound() {
        this.lstPendingPaymentDTO.clear();
        rvPendingCredits.getAdapter().notifyDataSetChanged();
        rlPendingCredits_NO_DATA_FOUND.setVisibility(View.VISIBLE);
        rvPendingCredits.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        objPresenter = new PendingCreditsPresenter(this);
        RouteDTO objRouteDTO = ((BaseActivity) getActivity()).getCurrentSelectedRoute();
        objPresenter.getPendingCreditsByFilters(objRouteDTO.getNuCode(), null, null);
    }

    @Override
    public void printPayment(Integer nuPosition, Integer nuValueToPay, Date dtNextPayment) {
        PendingPaymentDTO obPendingPaymentDTO = lstPendingPaymentDTO.get(nuPosition);
        String sbDataToPrint = new PaymentPrint(obPendingPaymentDTO,
                Float.valueOf(String.valueOf(nuValueToPay)), dtNextPayment).getPrint();
        Utilities.doPrint(getActivity(), sbDataToPrint);
    }

    @Override
    public void notifyFiltersUsed(int nuFilterUsed) {
        switch (nuFilterUsed) {
            case PendingCreditsPresenter.CREDITCODE_FILTER:
                btnPendingCredits_FilterByCustomer.setImageDrawable(getResources().getDrawable(R.drawable.ic_people_gray_24dp));
                btnPendingCredits_FilterByCreditCode.setImageDrawable(getResources().getDrawable(R.drawable.ic_local_atm_white_24dp));
                break;
            case PendingCreditsPresenter.CUSTOMER_FILTER:
                btnPendingCredits_FilterByCustomer.setImageDrawable(getResources().getDrawable(R.drawable.ic_people_white_24dp));
                btnPendingCredits_FilterByCreditCode.setImageDrawable(getResources().getDrawable(R.drawable.ic_local_atm_gray_24dp));
                break;
            case PendingCreditsPresenter.NO_FILTER:
                btnPendingCredits_FilterByCustomer.setImageDrawable(getResources().getDrawable(R.drawable.ic_people_gray_24dp));
                btnPendingCredits_FilterByCreditCode.setImageDrawable(getResources().getDrawable(R.drawable.ic_local_atm_gray_24dp));
                break;
        }
    }

}