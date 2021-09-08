package com.cristiancollazos.paybird.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.cristiancollazos.paybird.R;
import com.cristiancollazos.paybird.misc.Constants;
import com.cristiancollazos.paybird.misc.Utilities;
import com.cristiancollazos.paybird.presenter.CustomerPresenter;
import com.cristiancollazos.paybird.repository.dto.CityDTO;
import com.cristiancollazos.paybird.repository.dto.CustomerDTO;
import com.cristiancollazos.paybird.repository.dto.DocumentTypeDTO;
import com.cristiancollazos.paybird.repository.dto.NeighborhoodDTO;
import com.cristiancollazos.paybird.repository.dto.RouteDTO;
import com.cristiancollazos.paybird.view.activity.BaseActivity;
import com.cristiancollazos.paybird.view.adapter.SimpleAdapter;
import com.cristiancollazos.paybird.view.adapter.interfaces.OnRecyclerItemActionListener;
import com.cristiancollazos.paybird.view.dialog.CustomersDialog;

import java.util.ArrayList;
import java.util.List;

public class CustomerFragment extends Fragment
        implements CustomerPresenter.View, TextView.OnEditorActionListener, View.OnClickListener,
        OnRecyclerItemActionListener<CustomerDTO> {

    private Spinner spCustomerDocumentType, spCustomerCity, spCustomerNeighborhood,
            spCustomerRoutes, spCustomerCompanyCity;
    private TextInputEditText etCustomerDocument, etCustomerName, etCustomerLastName,
            etCustomerAddress, etCustomerPhone, etCustomerMobile, etCustomerCompanyName,
            etCustomerCompanyAddress, etCustomerCompanyPhone;
    private Button btnCustomerSave;

    private Boolean blCustomerExist = false;
    private Integer nuCode = null;
    private CustomerPresenter objPresenter;

    public CustomerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setBaseEnvironment();

        View objView = inflater.inflate(R.layout.fragment_customer, container, false);
        spCustomerDocumentType = objView.findViewById(R.id.spCustomerDocumentType);
        spCustomerCity = objView.findViewById(R.id.spCustomerCity);
        spCustomerNeighborhood = objView.findViewById(R.id.spCustomerNeighborhood);
        spCustomerRoutes = objView.findViewById(R.id.spCustomerRoutes);
        spCustomerCompanyCity = objView.findViewById(R.id.spCustomerCompanyCity);
        etCustomerDocument = objView.findViewById(R.id.etCustomerDocument);
        etCustomerName = objView.findViewById(R.id.etCustomerName);
        etCustomerLastName = objView.findViewById(R.id.etCustomerLastName);
        etCustomerAddress = objView.findViewById(R.id.etCustomerAddres);
        etCustomerPhone = objView.findViewById(R.id.etCustomerPhone);
        etCustomerMobile = objView.findViewById(R.id.etCustomerMobile);
        etCustomerCompanyName = objView.findViewById(R.id.etCustomerCompanyName);
        etCustomerCompanyAddress = objView.findViewById(R.id.etCustomerCompanyAddress);
        etCustomerCompanyPhone = objView.findViewById(R.id.etCustomerCompanyPhone);
        btnCustomerSave = objView.findViewById(R.id.btnCustomerSave);

        btnCustomerSave.setOnClickListener(this);
        etCustomerDocument.setOnEditorActionListener(this);

        setHasOptionsMenu(true);

        initializeFragment();
        return objView;
    }

    private void setBaseEnvironment() {
        ((BaseActivity) getActivity()).setToolbarVisible(true);
        ((BaseActivity) getActivity()).setDrawerEnabled(true);
        ((BaseActivity) getActivity()).setActionBarTitle(getResources().getString(R.string.customers_menu_title));
        ((BaseActivity) getActivity()).setActionBarSubtitle(getResources().getString(R.string.customers_menu_subtitle_create));
    }

    private void initializeFragment() {
        objPresenter = new CustomerPresenter(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                objPresenter.getDocumentTypes();
                objPresenter.getCities();
                objPresenter.getNeighborhoods();
                objPresenter.getRoutes();
            }
        }, 500);
    }

    @Override
    public void onCreateOptionsMenu(Menu objMenu, MenuInflater objInflater) {
        objInflater.inflate(R.menu.menu_customer, objMenu);
        super.onCreateOptionsMenu(objMenu, objInflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem objItem) {
        switch (objItem.getItemId()) {
            case R.id.abAction_Customers_CleanForm:
                cleanForm(true);
                break;
            case R.id.abAction_Customers_ListCustomers:
                RouteDTO objRouteDTO = ((BaseActivity) getActivity()).getCurrentSelectedRoute();
                objPresenter.getCustomersByRoute(objRouteDTO.getNuCode());
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!((BaseActivity) getActivity())
                .isFragmentAlreadyCreated(CustomerFragment.class.getSimpleName())) {
            Constants.FRAGMENTS_CREATED.add(CustomerFragment.class.getSimpleName());
        }
        ((BaseActivity) getActivity()).setActiveMenuItem(CustomerFragment.class.getSimpleName());
    }

    @Override
    public void onClick(View objView) {
        switch (objView.getId()) {
            case R.id.btnCustomerSave:
                if (etCustomerDocument.getText().toString().isEmpty()) {
                    etCustomerDocument.setError(getResources().getString(R.string.gen_error_mandatory));
                    break;
                }
                if (etCustomerName.getText().toString().isEmpty()) {
                    etCustomerName.setError(getResources().getString(R.string.gen_error_mandatory));
                    break;
                }
                if (etCustomerLastName.getText().toString().isEmpty()) {
                    etCustomerLastName.setError(getResources().getString(R.string.gen_error_mandatory));
                    break;
                }
                if (etCustomerAddress.getText().toString().isEmpty()) {
                    etCustomerAddress.setError(getResources().getString(R.string.gen_error_mandatory));
                    break;
                }
                if (etCustomerPhone.getText().toString().isEmpty()) {
                    etCustomerPhone.setError(getResources().getString(R.string.gen_error_mandatory));
                    break;
                }
                if (etCustomerMobile.getText().toString().isEmpty()) {
                    etCustomerMobile.setError(getResources().getString(R.string.gen_error_mandatory));
                    break;
                }
                if (etCustomerCompanyName.getText().toString().isEmpty()) {
                    etCustomerCompanyName.setError(getResources().getString(R.string.gen_error_mandatory));
                    break;
                }
                if (etCustomerCompanyAddress.getText().toString().isEmpty()) {
                    etCustomerCompanyAddress.setError(getResources().getString(R.string.gen_error_mandatory));
                    break;
                }
                if (etCustomerCompanyPhone.getText().toString().isEmpty()) {
                    etCustomerCompanyPhone.setError(getResources().getString(R.string.gen_error_mandatory));
                    break;
                }

                if (!blCustomerExist) {
                    objPresenter.setNewCustomer(
                            ((DocumentTypeDTO) spCustomerDocumentType.getSelectedItem()).getSbDefinition(),
                            etCustomerDocument.getText().toString(),
                            etCustomerName.getText().toString(),
                            etCustomerLastName.getText().toString(),
                            etCustomerAddress.getText().toString(),
                            ((CityDTO) spCustomerCity.getSelectedItem()).getNuCode(),
                            etCustomerPhone.getText().toString(),
                            etCustomerMobile.getText().toString(),
                            etCustomerCompanyAddress.getText().toString(),
                            ((CityDTO) spCustomerCompanyCity.getSelectedItem()).getNuCode(),
                            etCustomerCompanyPhone.getText().toString(),
                            etCustomerCompanyName.getText().toString(),
                            ((RouteDTO) spCustomerRoutes.getSelectedItem()).getNuCode(),
                            ((NeighborhoodDTO) spCustomerNeighborhood.getSelectedItem()).getNuCode());
                } else {
                    objPresenter.setEditCustomer(
                            nuCode,
                            ((DocumentTypeDTO) spCustomerDocumentType.getSelectedItem()).getSbDefinition(),
                            etCustomerDocument.getText().toString(),
                            etCustomerName.getText().toString(),
                            etCustomerLastName.getText().toString(),
                            etCustomerAddress.getText().toString(),
                            ((CityDTO) spCustomerCity.getSelectedItem()).getNuCode(),
                            etCustomerPhone.getText().toString(),
                            etCustomerMobile.getText().toString(),
                            etCustomerCompanyAddress.getText().toString(),
                            ((CityDTO) spCustomerCompanyCity.getSelectedItem()).getNuCode(),
                            etCustomerCompanyPhone.getText().toString(),
                            etCustomerCompanyName.getText().toString(),
                            ((RouteDTO) spCustomerRoutes.getSelectedItem()).getNuCode(),
                            ((NeighborhoodDTO) spCustomerNeighborhood.getSelectedItem()).getNuCode());
                }
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
    public void renderCities(List<CityDTO> lstCitiesDTO) {
        SimpleAdapter<CityDTO> objAdapter = new SimpleAdapter<>(getActivity(), lstCitiesDTO);
        spCustomerCity.setAdapter(objAdapter);
        spCustomerCompanyCity.setAdapter(objAdapter);
    }

    @Override
    public void renderNeighborhoods(List<NeighborhoodDTO> lstNeighborhoodsDTO) {
        SimpleAdapter<NeighborhoodDTO> objAdapter = new SimpleAdapter<>(getActivity(), lstNeighborhoodsDTO);
        spCustomerNeighborhood.setAdapter(objAdapter);
    }

    @Override
    public void renderRoutes(List<RouteDTO> lstRoutesDTO) {
        SimpleAdapter<RouteDTO> objAdapter = new SimpleAdapter<>(getActivity(), lstRoutesDTO);
        spCustomerRoutes.setAdapter(objAdapter);
    }

    @Override
    public void renderCustomerData(CustomerDTO objCustomer) {
        ((BaseActivity) getActivity()).setActionBarSubtitle(getResources().getString(R.string.customers_menu_subtitle_edit));

        spCustomerDocumentType.setSelection(((SimpleAdapter<DocumentTypeDTO>) spCustomerDocumentType.getAdapter())
                .getPositionByKey(objCustomer.getSbDocumentType()));
        etCustomerDocument.setText(objCustomer.getSbDocument());
        etCustomerName.setText(objCustomer.getSbName());
        etCustomerLastName.setText(objCustomer.getSbLastName());
        spCustomerCity.setSelection(((SimpleAdapter<CityDTO>) spCustomerCity.getAdapter())
                .getPositionByKey(objCustomer.getNuHomeCityCode().toString()));
        spCustomerNeighborhood.setSelection(((SimpleAdapter<NeighborhoodDTO>) spCustomerNeighborhood.getAdapter())
                .getPositionByKey(objCustomer.getNuNeighborhoodCode().toString()));
        etCustomerAddress.setText(objCustomer.getSbHomeAddress());
        etCustomerPhone.setText(objCustomer.getSbHomePhoneNumber());
        etCustomerMobile.setText(objCustomer.getSbMobileNumber());
        spCustomerRoutes.setSelection(((SimpleAdapter<RouteDTO>) spCustomerRoutes.getAdapter())
                .getPositionByKey(objCustomer.getNuRouteCode().toString()));
        etCustomerCompanyName.setText(objCustomer.getSbCompanyName());
        etCustomerCompanyAddress.setText(objCustomer.getSbWorkAddress());
        etCustomerCompanyPhone.setText(objCustomer.getSbWorkPhoneNumber());
        spCustomerCompanyCity.setSelection(((SimpleAdapter<RouteDTO>) spCustomerCompanyCity.getAdapter())
                .getPositionByKey(objCustomer.getNuWorkCityCode().toString()));

        blCustomerExist = true;
        nuCode = objCustomer.getNuCode();
    }

    @Override
    public void cleanForm(Boolean blAlsoCleanDocument) {
        ((BaseActivity) getActivity()).setActionBarSubtitle(getResources().getString(R.string.customers_menu_subtitle_create));

        if (blAlsoCleanDocument) {
            etCustomerDocument.setText(null);
        }

        etCustomerName.setText(null);
        etCustomerLastName.setText(null);
        spCustomerCity.setSelection(0);
        spCustomerNeighborhood.setSelection(0);
        etCustomerAddress.setText(null);
        etCustomerPhone.setText(null);
        etCustomerMobile.setText(null);
        spCustomerRoutes.setSelection(0);
        etCustomerCompanyName.setText(null);
        etCustomerCompanyAddress.setText(null);
        etCustomerCompanyPhone.setText(null);
        spCustomerCompanyCity.setSelection(0);

        blCustomerExist = false;
        nuCode = null;
    }


    @Override
    public void renderDocumentTypes(List<DocumentTypeDTO> lstDocumentTypesDTO) {
        SimpleAdapter<DocumentTypeDTO> objAdapter = new SimpleAdapter<>(getActivity(), lstDocumentTypesDTO);
        spCustomerDocumentType.setAdapter(objAdapter);
    }

    @Override
    public boolean onEditorAction(TextView tvView, int nuActionId, KeyEvent objKeyEvent) {
        if (nuActionId == EditorInfo.IME_ACTION_NEXT) {
            switch (tvView.getId()) {
                case R.id.etCustomerDocument:
                    objPresenter.validateIfCustomerExist(tvView.getText().toString());
                    break;
            }
        }

        etCustomerName.requestFocus();
        return true;
    }

    @Override
    public void renderCustomers(List<CustomerDTO> lstCustomersDTO) {
        CustomersDialog objCustomersDialogs = new CustomersDialog();
        objCustomersDialogs.setupDialog(lstCustomersDTO, this);
        objCustomersDialogs.show(getFragmentManager(), "CustomersDialog");
    }

    @Override
    public void showSuccessMessage(String sbMessage) {
        Utilities.showSnackBar(getView(), sbMessage);
    }

    @Override
    public void OnAction(CustomerDTO objItem, Integer nuPosition) {
        String sbDocument = objItem.getSbDocument();
        objPresenter.validateIfCustomerExist(sbDocument);
    }

    @Override
    public void notifyNoCustomersFound() {
        CustomersDialog objCustomersDialogs = (CustomersDialog)
                getFragmentManager().findFragmentByTag("CustomersDialog");

        if (objCustomersDialogs == null) {
            objCustomersDialogs = new CustomersDialog();
            objCustomersDialogs.setupDialog(new ArrayList<CustomerDTO>(), this);
            objCustomersDialogs.show(getFragmentManager(), "CustomersDialog");
        } else {
            objCustomersDialogs.notifyNoDataFound();
        }
    }

}
