package com.cristiancollazos.paybird.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cristiancollazos.paybird.R;
import com.cristiancollazos.paybird.misc.Constants;
import com.cristiancollazos.paybird.misc.Utilities;
import com.cristiancollazos.paybird.presenter.CreditsManagementPresenter;
import com.cristiancollazos.paybird.repository.dto.CreditDTO;
import com.cristiancollazos.paybird.repository.dto.CustomerDTO;
import com.cristiancollazos.paybird.repository.dto.MovementDTO;
import com.cristiancollazos.paybird.repository.dto.RouteDTO;
import com.cristiancollazos.paybird.view.activity.BaseActivity;
import com.cristiancollazos.paybird.view.adapter.CreditAdapter;
import com.cristiancollazos.paybird.view.adapter.interfaces.OnRecyclerItemActionListener;
import com.cristiancollazos.paybird.view.dialog.CustomerInfoDialog;
import com.cristiancollazos.paybird.view.dialog.CustomersDialog;
import com.cristiancollazos.paybird.view.dialog.MovementsDialog;
import com.cristiancollazos.paybird.view.dialog.interfaces.OnSearchDialogAction;

import java.util.ArrayList;
import java.util.List;

public class CreditManagementFragment extends Fragment
        implements CreditsManagementPresenter.View, View.OnClickListener {

    private Button btnCreditMng_PickACustomer;
    private RelativeLayout rlCreditMng_CustomerSelectedPanel, rlCreditMng_NoCustomerSelected,
            rlCreditMng_NO_CREDITS_FOUND;
    private TextView tvCreditMng_Customer;
    private ImageButton btnCreditMng_CustomerInfo, btnCreditMng_CreateCredit;
    private RecyclerView rvCreditMng_CustomerCredits;
    private CreditsManagementPresenter objPresenter;
    private CustomerDTO objSelectedCustomerDTO;
    private List<CreditDTO> lstCreditDTO;

    private void setBaseEnvironment() {
        ((BaseActivity) getActivity()).setToolbarVisible(true);
        ((BaseActivity) getActivity()).setDrawerEnabled(true);
        ((BaseActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.credits_menu_title2));
        ((BaseActivity) getActivity()).setActionBarSubtitle(getResources().getString(R.string.credits_menu_subtitle));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater objInflater,
                             @Nullable ViewGroup objContainer,
                             @Nullable Bundle objSavedInstanceState) {
        setBaseEnvironment();
        setHasOptionsMenu(true);

        View objView = objInflater.inflate(R.layout.fragment_creditmanagement, null);

        rlCreditMng_CustomerSelectedPanel = objView.findViewById(R.id.rlCreditMng_CustomerSelectedPanel);
        rlCreditMng_NoCustomerSelected = objView.findViewById(R.id.rlCreditMng_NoCustomerSelected);
        rlCreditMng_NO_CREDITS_FOUND = objView.findViewById(R.id.rlCreditMng_NO_CREDITS_FOUND);
        tvCreditMng_Customer = objView.findViewById(R.id.tvCreditMng_Customer);
        btnCreditMng_PickACustomer = objView.findViewById(R.id.btnCreditMng_PickACustomer);
        btnCreditMng_CustomerInfo = objView.findViewById(R.id.btnCreditMng_CustomerInfo);
        btnCreditMng_CreateCredit = objView.findViewById(R.id.btnCreditMng_CreateCredit);
        rvCreditMng_CustomerCredits = objView.findViewById(R.id.rvCreditMng_CustomerCredits);

        LinearLayoutManager objLayoutManager = new LinearLayoutManager(getActivity());
        objLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvCreditMng_CustomerCredits.setLayoutManager(objLayoutManager);

        lstCreditDTO = new ArrayList<>();
        CreditAdapter objAdapter = new CreditAdapter(getActivity(), lstCreditDTO,
                new OnRecyclerItemActionListener<CreditDTO>() {
                    @Override
                    public void OnAction(CreditDTO objItem, Integer nuPosition) {
                        objPresenter.getMovementsByCredit(objItem.getNuCode());
                    }
                });
        rvCreditMng_CustomerCredits.setAdapter(objAdapter);

        btnCreditMng_PickACustomer.setOnClickListener(this);
        btnCreditMng_CustomerInfo.setOnClickListener(this);
        btnCreditMng_CreateCredit.setOnClickListener(this);

        initializeFragment();
        return objView;
    }

    private void initializeFragment() {
        objPresenter = new CreditsManagementPresenter(this);

        rlCreditMng_CustomerSelectedPanel.setVisibility(View.GONE);
        rvCreditMng_CustomerCredits.setVisibility(View.GONE);
        rlCreditMng_NO_CREDITS_FOUND.setVisibility(View.GONE);

        rlCreditMng_NoCustomerSelected.setVisibility(View.VISIBLE);
        btnCreditMng_PickACustomer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!((BaseActivity) getActivity())
                .isFragmentAlreadyCreated(CreditManagementFragment.class.getSimpleName())) {
            Constants.FRAGMENTS_CREATED.add(CreditManagementFragment.class.getSimpleName());
        }

        ((BaseActivity) getActivity()).setActiveMenuItem(CreditManagementFragment.class.getSimpleName());

        objPresenter.getSelectedCustomerFromLocalStorage();
    }

    @Override
    public void onCreateOptionsMenu(Menu objMenu, MenuInflater objInflater) {
        objInflater.inflate(R.menu.menu_creditmanagement, objMenu);
        super.onCreateOptionsMenu(objMenu, objInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem objMenuItem) {
        switch (objMenuItem.getItemId()) {
            case R.id.abAction_CreditManagement_CleanForm:
                cleanForm();
                return true;
            default:
                return true;
        }
    }

    public void cleanForm() {
        ((BaseActivity) getActivity()).showProgressScreen(getResources().getString(R.string.gen_cleaning_action));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rlCreditMng_CustomerSelectedPanel.setVisibility(View.GONE);
                rvCreditMng_CustomerCredits.setVisibility(View.GONE);
                rlCreditMng_NO_CREDITS_FOUND.setVisibility(View.GONE);

                rlCreditMng_NoCustomerSelected.setVisibility(View.VISIBLE);
                btnCreditMng_PickACustomer.setVisibility(View.VISIBLE);

                objSelectedCustomerDTO = null;
                ((BaseActivity) getActivity()).hideProgressScreen();
            }
        }, 500);
    }

    @Override
    public void onClick(View objView) {
        switch (objView.getId()) {
            case R.id.btnCreditMng_PickACustomer:
                RouteDTO objRouteDTO = ((BaseActivity) getActivity()).getCurrentSelectedRoute();
                objPresenter.getCustomersByRoute(objRouteDTO.getNuCode());
                break;
            case R.id.btnCreditMng_CustomerInfo:
                objPresenter.getCustomerByDocument(objSelectedCustomerDTO.getSbDocument());
                break;
            case R.id.btnCreditMng_CreateCredit:
                objPresenter.setSelectedCustomerAtLocalStorage(objSelectedCustomerDTO);
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
    }

    @Override
    public void showErrorMessage(String sbError, String sbRecommend) {
        ((BaseActivity) getActivity()).showErrorMessage(sbError, sbRecommend);
    }

    @Override
    public void renderCustomers(List<CustomerDTO> lstCustomersDTO) {
        CustomersDialog objCustomersDialogs = new CustomersDialog();
        objCustomersDialogs.setupDialog(lstCustomersDTO, new OnRecyclerItemActionListener<CustomerDTO>() {
            @Override
            public void OnAction(CustomerDTO objItem, Integer nuPosition) {
                objSelectedCustomerDTO = objItem;
                tvCreditMng_Customer.setText(objSelectedCustomerDTO.getSbName() + " "
                        + objSelectedCustomerDTO.getSbLastName());

                btnCreditMng_PickACustomer.setVisibility(View.GONE);
                rlCreditMng_NoCustomerSelected.setVisibility(View.GONE);
                rlCreditMng_CustomerSelectedPanel.setVisibility(View.VISIBLE);

                objPresenter.getCreditsByCustomer(objSelectedCustomerDTO.getNuCode());
            }
        }, new OnSearchDialogAction() {
            @Override
            public void OnSearch(String sbFilterValue) {
                RouteDTO objRouteDTO = ((BaseActivity) getActivity()).getCurrentSelectedRoute();
                objPresenter.getCustomersByRouteFilter(objRouteDTO.getNuCode(), sbFilterValue);
            }

            @Override
            public void OnCancelSearch() {
                RouteDTO objRouteDTO = ((BaseActivity) getActivity()).getCurrentSelectedRoute();
                objPresenter.getCustomersByRouteFilter(objRouteDTO.getNuCode(), null);
            }
        });
        objCustomersDialogs.show(getFragmentManager(), "CustomersDialog");
    }

    @Override
    public void showSuccessMessage(String sbMessage) {
        Utilities.showSnackBar(getView(), sbMessage);
    }

    @Override
    public void renderCustomersWithinDialog(List<CustomerDTO> lstCustomersDTO) {
        CustomersDialog objCustomersDialogs = (CustomersDialog)
                getFragmentManager().findFragmentByTag("CustomersDialog");
        objCustomersDialogs.refreshCustomers(lstCustomersDTO);
    }

    @Override
    public void renderCustomerData(CustomerDTO objCustomerDTO) {
        CustomerInfoDialog objCustomerInfoDialog = new CustomerInfoDialog();
        objCustomerInfoDialog.setupDialog(objCustomerDTO);
        objCustomerInfoDialog.show(getFragmentManager(), "CustomerInfoDialog");
    }

    @Override
    public void notifyNoCustomersFound() {
        CustomersDialog objCustomersDialogs = (CustomersDialog)
                getFragmentManager().findFragmentByTag("CustomersDialog");

        if (objCustomersDialogs == null) {
            objCustomersDialogs = new CustomersDialog();
            objCustomersDialogs.setupDialog(new ArrayList<CustomerDTO>(),
                    new OnRecyclerItemActionListener<CustomerDTO>() {
                        @Override
                        public void OnAction(CustomerDTO objItem, Integer nuPosition) {
                            objSelectedCustomerDTO = objItem;
                            tvCreditMng_Customer.setText(objSelectedCustomerDTO.getSbName() + " "
                                    + objSelectedCustomerDTO.getSbLastName());

                            btnCreditMng_PickACustomer.setVisibility(View.GONE);
                            rlCreditMng_NoCustomerSelected.setVisibility(View.GONE);
                            rlCreditMng_CustomerSelectedPanel.setVisibility(View.VISIBLE);

                            objPresenter.getCreditsByCustomer(objSelectedCustomerDTO.getNuCode());
                        }
                    });
            objCustomersDialogs.show(getFragmentManager(), "CustomersDialog");
        } else {
            objCustomersDialogs.notifyNoDataFound();
        }
    }

    @Override
    public void notifyNoCreditsAssociated() {
        lstCreditDTO.clear();
        rvCreditMng_CustomerCredits.getAdapter().notifyDataSetChanged();
        rvCreditMng_CustomerCredits.setVisibility(View.GONE);
        rlCreditMng_NO_CREDITS_FOUND.setVisibility(View.VISIBLE);
    }

    @Override
    public void renderAssociatedCredits(List<CreditDTO> lstCreditsDTO) {
        rlCreditMng_NO_CREDITS_FOUND.setVisibility(View.GONE);
        rvCreditMng_CustomerCredits.setVisibility(View.VISIBLE);
        this.lstCreditDTO.clear();
        this.lstCreditDTO.addAll(lstCreditsDTO);
        rvCreditMng_CustomerCredits.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void renderCreditMovements(List<MovementDTO> lstMovementDTO,
                                      Integer nuCreditCode) {
        MovementsDialog objMovementsDialog = new MovementsDialog();
        objMovementsDialog.setupDialog(lstMovementDTO, nuCreditCode);
        objMovementsDialog.show(getFragmentManager(), "MovementsDialog");
    }

    @Override
    public void notifyNoMovements(Integer nuCreditCode) {
        MovementsDialog objMovementsDialog = new MovementsDialog();
        objMovementsDialog.setupDialog(null, nuCreditCode);
        objMovementsDialog.show(getFragmentManager(), "MovementsDialog");
    }

    @Override
    public void redirectToCreateCredit(CustomerDTO objCustomerDTO) {
        CreateCreditFragment objCreateCreditFragment = new CreateCreditFragment();
        objCreateCreditFragment.setCustomerData(objCustomerDTO);
        ((BaseActivity) getActivity()).replaceFragment(objCreateCreditFragment, true);
    }

    @Override
    public void refreshCustomerData(CustomerDTO obCustomerDTO) {
        this.objSelectedCustomerDTO = obCustomerDTO;

        tvCreditMng_Customer.setText(objSelectedCustomerDTO.getSbName() + " "
                + objSelectedCustomerDTO.getSbLastName());

        btnCreditMng_PickACustomer.setVisibility(View.GONE);
        rlCreditMng_NoCustomerSelected.setVisibility(View.GONE);
        rlCreditMng_CustomerSelectedPanel.setVisibility(View.VISIBLE);

        objPresenter.getCreditsByCustomer(objSelectedCustomerDTO.getNuCode());
    }

}
