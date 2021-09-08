package com.cristiancollazos.paybird.view.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cristiancollazos.paybird.R;
import com.cristiancollazos.paybird.misc.Constants;
import com.cristiancollazos.paybird.misc.ResizeAnimator;
import com.cristiancollazos.paybird.print.PaymentPrint;
import com.cristiancollazos.paybird.misc.Utilities;
import com.cristiancollazos.paybird.presenter.TodayPaymentsPresenter;
import com.cristiancollazos.paybird.repository.dto.MovementDTO;
import com.cristiancollazos.paybird.repository.dto.PendingPaymentDTO;
import com.cristiancollazos.paybird.repository.dto.RouteDTO;
import com.cristiancollazos.paybird.view.activity.BaseActivity;
import com.cristiancollazos.paybird.view.adapter.CardViewDispatcher;
import com.cristiancollazos.paybird.view.adapter.MovementsAdapter;
import com.cristiancollazos.paybird.view.adapter.PendingPaymentAdapter;
import com.cristiancollazos.paybird.view.adapter.interfaces.OnRecyclerItemActionListener;
import com.cristiancollazos.paybird.view.adapter.interfaces.OnRecyclerItemDispatchedListener;
import com.cristiancollazos.paybird.view.dialog.ChooseDateDialog;
import com.cristiancollazos.paybird.view.dialog.FilterDialog;
import com.cristiancollazos.paybird.view.dialog.PayDialog;
import com.cristiancollazos.paybird.view.dialog.interfaces.ConfirmDialogListener;
import com.cristiancollazos.paybird.view.dialog.interfaces.OnFilterDialogConfirm;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TodayPaymentsFragment extends Fragment
        implements TodayPaymentsPresenter.View, SwipeRefreshLayout.OnRefreshListener,
        OnRecyclerItemActionListener<PendingPaymentDTO>, View.OnClickListener,
        OnRecyclerItemDispatchedListener {

    private final String TAG = TodayPaymentsFragment.class.getSimpleName();
    private TodayPaymentsPresenter objPresenter;
    private SwipeRefreshLayout srvTodayPayments;
    private List<PendingPaymentDTO> lstPendingPaymentDTO;
    private RelativeLayout rlTodayPayments_NO_DATA_FOUND, rlTodayPayments_Subheader;
    private RecyclerView rvTodayPayments;
    private List<MovementDTO> lstMovementDTO;
    private RelativeLayout rlMovements_NO_DATA_FOUND;
    private RecyclerView rvMovements;
    private Date dtCurrentSelectedDate;
    private TextView tvPayment_PaymentType, tvPayments_Date;
    private FloatingActionButton btnPayments_GoNullPayment, btnPayments_GoDoPayment;
    private MenuItem miTodayPayment_ChooseDate;
    private ImageButton btnTodayPayments_FilterByCreditCode, btnTodayPayments_FilterByCustomer;
    private Boolean blFilterBarDisplayed = true;

    private int nuFilterLayoutHeight;

    public TodayPaymentsFragment() {
    }

    private void setBaseEnvironment() {
        ((BaseActivity) getActivity()).setToolbarVisible(true);
        ((BaseActivity) getActivity()).setDrawerEnabled(true);
        ((BaseActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.todaypayments_menu_title));
        ((BaseActivity) getActivity()).setActionBarSubtitle(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setBaseEnvironment();

        View objView = inflater.inflate(R.layout.fragment_todaypayments, container, false);

        srvTodayPayments = objView.findViewById(R.id.srvTodayPayments);
        rvTodayPayments = objView.findViewById(R.id.rvTodayPayments);
        rlTodayPayments_NO_DATA_FOUND = objView.findViewById(R.id.rlTodayPayments_NO_DATA_FOUND);
        rvMovements = objView.findViewById(R.id.rvMovements);
        rlMovements_NO_DATA_FOUND = objView.findViewById(R.id.rlMovements_NO_DATA_FOUND);
        tvPayment_PaymentType = objView.findViewById(R.id.tvPayment_PaymentType);
        tvPayments_Date = objView.findViewById(R.id.tvPayments_Date);
        btnPayments_GoNullPayment = objView.findViewById(R.id.btnPayments_GoNullPayment);
        btnPayments_GoDoPayment = objView.findViewById(R.id.btnPayments_GoDoPayment);
        rlTodayPayments_Subheader = objView.findViewById(R.id.rlTodayPayments_Subheader);
        btnTodayPayments_FilterByCreditCode = objView.findViewById(R.id.btnTodayPayments_FilterByCreditCode);
        btnTodayPayments_FilterByCustomer = objView.findViewById(R.id.btnTodayPayments_FilterByCustomer);

        LinearLayoutManager objLayoutManagerPending = new LinearLayoutManager(getContext());
        objLayoutManagerPending.setOrientation(LinearLayoutManager.VERTICAL);
        rvTodayPayments.setLayoutManager(objLayoutManagerPending);

        LinearLayoutManager objLayoutManagerDone = new LinearLayoutManager(getContext());
        objLayoutManagerDone.setOrientation(LinearLayoutManager.VERTICAL);
        rvMovements.setLayoutManager(objLayoutManagerDone);

        lstPendingPaymentDTO = new ArrayList<>();
        PendingPaymentAdapter objAdapter = new PendingPaymentAdapter(lstPendingPaymentDTO,
                this);
        rvTodayPayments.setAdapter(objAdapter);

        lstMovementDTO = new ArrayList<>();
        MovementsAdapter objMovementAdapter = new MovementsAdapter(lstMovementDTO);
        rvMovements.setAdapter(objMovementAdapter);
        new ItemTouchHelper(new CardViewDispatcher(getContext(), this,
                R.drawable.ic_clear_white_png_24dp, R.color.indigoblue_accent_red))
                .attachToRecyclerView(rvMovements);

        srvTodayPayments.setOnRefreshListener(this);
        btnPayments_GoDoPayment.setOnClickListener(this);
        btnPayments_GoNullPayment.setOnClickListener(this);
        btnTodayPayments_FilterByCreditCode.setOnClickListener(this);
        btnTodayPayments_FilterByCustomer.setOnClickListener(this);

        setHasOptionsMenu(true);

        initializeFragment();
        return objView;
    }

    private void initializeFragment() {
        objPresenter = new TodayPaymentsPresenter(this);
        RouteDTO objCurrentSelectedRoute = ((BaseActivity) getActivity()).getCurrentSelectedRoute();

        dtCurrentSelectedDate = new Date();
        displayUiAsPending();
        btnPayments_GoDoPayment.hide();

        objPresenter.getPendingPaymentsByDate(objCurrentSelectedRoute.getNuCode(),
                dtCurrentSelectedDate, null, null);
    }


    @Override
    public void onCreateOptionsMenu(Menu objMenu, MenuInflater objInflater) {
        objInflater.inflate(R.menu.menu_todaypayment, objMenu);
        miTodayPayment_ChooseDate = objMenu.getItem(0);
        super.onCreateOptionsMenu(objMenu, objInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem objItem) {
        switch (objItem.getItemId()) {
            case R.id.abAction_TodayPayment_ChooseDate:
                ChooseDateDialog objChooseDateDialog = new ChooseDateDialog();
                objChooseDateDialog.setupDialog(new ConfirmDialogListener() {
                    @Override
                    public void onConfirm(AlertDialog objAlertDialog, Object... rcDialogData) {
                        dtCurrentSelectedDate = (Date) rcDialogData[0];

                        RouteDTO objCurrentSelectedRoute = ((BaseActivity) getActivity())
                                .getCurrentSelectedRoute();
                        objPresenter.getPendingPaymentsByDate(objCurrentSelectedRoute.getNuCode(),
                                dtCurrentSelectedDate, null, null);

                        objAlertDialog.dismiss();
                    }

                    @Override
                    public void onCancel(AlertDialog objAlertDialog) {
                        objAlertDialog.dismiss();
                    }
                });
                objChooseDateDialog.show(getFragmentManager(), "ChooseDateDialog");
                break;
        }
        return true;
    }

    public void getPendingPayments() {
        RouteDTO objCurrentSelectedRoute = ((BaseActivity) getActivity()).getCurrentSelectedRoute();
        dtCurrentSelectedDate = new Date();
        objPresenter.getPendingPaymentsByDate(objCurrentSelectedRoute.getNuCode(),
                dtCurrentSelectedDate, null, null);
    }

    public void getPendingPaymentsWoCurrentDate() {
        RouteDTO objCurrentSelectedRoute = ((BaseActivity) getActivity()).getCurrentSelectedRoute();
        objPresenter.getPendingPaymentsByDate(objCurrentSelectedRoute.getNuCode(),
                dtCurrentSelectedDate, null, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!((BaseActivity) getActivity())
                .isFragmentAlreadyCreated(TodayPaymentsFragment.class.getSimpleName())) {
            Constants.FRAGMENTS_CREATED.add(TodayPaymentsFragment.class.getSimpleName());
        }

        ((BaseActivity) getActivity()).setActiveMenuItem(TodayPaymentsFragment.class.getSimpleName());

        rlTodayPayments_Subheader.measure(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        nuFilterLayoutHeight = rlTodayPayments_Subheader.getMeasuredHeight();
    }

    @Override
    public void showProgress() {
        ((BaseActivity) getActivity()).showProgressBar();
    }

    @Override
    public void hideProgress() {
        ((BaseActivity) getActivity()).hideProgressBar();
        srvTodayPayments.setRefreshing(false);
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
    public void renderPendingPayments(List<PendingPaymentDTO> lstPendingPaymentDTO) {
        this.lstPendingPaymentDTO.clear();
        this.lstPendingPaymentDTO.addAll(lstPendingPaymentDTO);
        rlTodayPayments_NO_DATA_FOUND.setVisibility(View.GONE);
        rvTodayPayments.setVisibility(View.VISIBLE);
        rvTodayPayments.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void renderMovements(List<MovementDTO> lstMovementDTO) {
        this.lstMovementDTO.clear();
        this.lstMovementDTO.addAll(lstMovementDTO);
        rlMovements_NO_DATA_FOUND.setVisibility(View.GONE);
        rvMovements.setVisibility(View.VISIBLE);
        rvMovements.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        if (btnPayments_GoNullPayment.isShown()) {
            onClick(btnPayments_GoDoPayment);
        } else {
            onClick(btnPayments_GoNullPayment);
        }
    }

    @Override
    public void notifyPendingNotFound() {
        this.lstPendingPaymentDTO.clear();
        rvTodayPayments.getAdapter().notifyDataSetChanged();
        rlTodayPayments_NO_DATA_FOUND.setVisibility(View.VISIBLE);
        rvTodayPayments.setVisibility(View.GONE);
    }

    @Override
    public void notifyDoneNotFound() {
        this.lstMovementDTO.clear();
        rvMovements.getAdapter().notifyDataSetChanged();
        rlMovements_NO_DATA_FOUND.setVisibility(View.VISIBLE);
        rvMovements.setVisibility(View.GONE);
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
    public void removePendingItemAndNotify(Integer nuPosition) {
        lstPendingPaymentDTO.remove(lstPendingPaymentDTO.get(nuPosition));
        rvTodayPayments.getAdapter().notifyItemRemoved(nuPosition);
        rvTodayPayments.getAdapter().notifyItemRangeChanged(nuPosition, lstPendingPaymentDTO.size());

        if (lstPendingPaymentDTO.size() == 0) {
            notifyPendingNotFound();
        }
    }

    @Override
    public void displayUiAsPending() {
        tvPayment_PaymentType.setText(getResources().getString(R.string.todaypayments_menu_title_pending));
        SimpleDateFormat objFormatter = new SimpleDateFormat("dd/MM/yyyy");
        tvPayments_Date.setText(objFormatter.format(dtCurrentSelectedDate));
        if (miTodayPayment_ChooseDate != null) {
            miTodayPayment_ChooseDate.setVisible(true);
        }

        rvMovements.setVisibility(View.GONE);
        rlMovements_NO_DATA_FOUND.setVisibility(View.GONE);

        btnPayments_GoDoPayment.hide(new FloatingActionButton.OnVisibilityChangedListener() {
            @Override
            public void onHidden(FloatingActionButton objFab) {
                btnPayments_GoNullPayment.show();
            }
        });

        if (!blFilterBarDisplayed) {
            ResizeAnimator objAnimator = new ResizeAnimator(rlTodayPayments_Subheader,
                    nuFilterLayoutHeight, 0);
            objAnimator.setDuration(300);
            rlTodayPayments_Subheader.startAnimation(objAnimator);
        }

        blFilterBarDisplayed = true;
    }

    @Override
    public void displayUiAsDone() {
        tvPayment_PaymentType.setText(getResources().getString(R.string.todaypayments_menu_title_done));
        SimpleDateFormat objFormatter = new SimpleDateFormat("dd/MM/yyyy");
        tvPayments_Date.setText(objFormatter.format(dtCurrentSelectedDate));
        if (miTodayPayment_ChooseDate != null) {
            miTodayPayment_ChooseDate.setVisible(false);
        }

        rvTodayPayments.setVisibility(View.GONE);
        rlTodayPayments_NO_DATA_FOUND.setVisibility(View.GONE);

        btnPayments_GoNullPayment.hide(new FloatingActionButton.OnVisibilityChangedListener() {
            @Override
            public void onHidden(FloatingActionButton fab) {
                btnPayments_GoDoPayment.show();
            }
        });

        if (blFilterBarDisplayed) {
            ResizeAnimator objAnimator = new ResizeAnimator(rlTodayPayments_Subheader,
                    (nuFilterLayoutHeight * -1), nuFilterLayoutHeight);
            objAnimator.setDuration(300);
            rlTodayPayments_Subheader.startAnimation(objAnimator);
        }

        blFilterBarDisplayed = false;
    }

    @Override
    public void onClick(View objView) {
        FilterDialog objFilterDialog = new FilterDialog();

        switch (objView.getId()) {
            case R.id.btnPayments_GoDoPayment:
                getPendingPaymentsWoCurrentDate();
                break;
            case R.id.btnPayments_GoNullPayment:
                dtCurrentSelectedDate = new Date();
                RouteDTO objCurrentSelectedRoute = ((BaseActivity) getActivity()).getCurrentSelectedRoute();
                objPresenter.getMovementsByRoute(objCurrentSelectedRoute.getNuCode());
                break;
            case R.id.btnTodayPayments_FilterByCreditCode:
                objFilterDialog.setupDialog(R.drawable.ic_local_atm_black_24dp,
                        getResources().getString(R.string.filterdialog_filterby_creditcode),
                        FilterDialog.FILTER_NUMERIC,
                        new OnFilterDialogConfirm() {
                            @Override
                            public void onFilter(String sbFilterValue) {
                                RouteDTO objRouteDTO = ((BaseActivity) getActivity()).getCurrentSelectedRoute();
                                objPresenter.getPendingPaymentsByDate(objRouteDTO.getNuCode(),
                                        dtCurrentSelectedDate,
                                        null,
                                        Integer.parseInt(sbFilterValue));
                            }
                        });
                objFilterDialog.show(getFragmentManager(), "FilterDialog");
                break;
            case R.id.btnTodayPayments_FilterByCustomer:
                objFilterDialog.setupDialog(R.drawable.ic_people_black_24dp,
                        getResources().getString(R.string.filterdialog_filterby_customername),
                        FilterDialog.FILTER_CHAR,
                        new OnFilterDialogConfirm() {
                            @Override
                            public void onFilter(String sbFilterValue) {
                                RouteDTO objRouteDTO = ((BaseActivity) getActivity()).getCurrentSelectedRoute();
                                objPresenter.getPendingPaymentsByDate(objRouteDTO.getNuCode(),
                                        dtCurrentSelectedDate,
                                        sbFilterValue,
                                        null);
                            }
                        });
                objFilterDialog.show(getFragmentManager(), "FilterDialog");
                break;
        }
    }

    @Override
    public void OnItemDispatched(final int nuPosition) {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
        format.setCurrency(Currency.getInstance("COP"));
        String sbValue = format.format(lstMovementDTO.get(nuPosition).getFlValue());
        sbValue = sbValue.substring(3);

        Utilities.showConfirmationDialog(getActivity(),
                getResources().getString(R.string.payments_confirm_nullmovement,
                        lstMovementDTO.get(nuPosition).getNuCode(), sbValue),
                new ConfirmDialogListener() {
                    @Override
                    public void onConfirm(AlertDialog objAlertDialog, Object... rcDialogData) {
                        objAlertDialog.dismiss();
                        objPresenter.doNullMovement(lstMovementDTO.get(nuPosition).getNuCode(), nuPosition);
                    }

                    @Override
                    public void onCancel(AlertDialog objAlertDialog) {
                        rvMovements.getAdapter().notifyItemChanged(nuPosition);
                        objAlertDialog.dismiss();
                    }
                }, R.drawable.ic_clear_black_24dp);
    }

    @Override
    public void revertMovementItem(Integer nuPosition) {
        rvMovements.getAdapter().notifyItemChanged(nuPosition);
    }

    @Override
    public void removeMovementItem(Integer nuPosition) {
        lstMovementDTO.remove(lstMovementDTO.get(nuPosition));
        rvMovements.getAdapter().notifyItemRemoved(nuPosition);
        rvMovements.getAdapter().notifyItemRangeChanged(nuPosition, lstMovementDTO.size());

        if (lstMovementDTO.size() == 0) {
            notifyDoneNotFound();
        }
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
            case TodayPaymentsPresenter.CREDITCODE_FILTER:
                btnTodayPayments_FilterByCustomer.setImageDrawable
                        (getResources().getDrawable(R.drawable.ic_people_gray_24dp));
                btnTodayPayments_FilterByCreditCode.setImageDrawable
                        (getResources().getDrawable(R.drawable.ic_local_atm_white_24dp));
                break;
            case TodayPaymentsPresenter.CUSTOMER_FILTER:
                btnTodayPayments_FilterByCustomer.setImageDrawable
                        (getResources().getDrawable(R.drawable.ic_people_white_24dp));
                btnTodayPayments_FilterByCreditCode.setImageDrawable
                        (getResources().getDrawable(R.drawable.ic_local_atm_gray_24dp));
                break;
            case TodayPaymentsPresenter.NO_FILTER:
                btnTodayPayments_FilterByCustomer.setImageDrawable
                        (getResources().getDrawable(R.drawable.ic_people_gray_24dp));
                btnTodayPayments_FilterByCreditCode.setImageDrawable(getResources().getDrawable(R.drawable.ic_local_atm_gray_24dp));
                break;
        }
    }

}
