package com.cristiancollazos.paybird.view.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.cristiancollazos.paybird.R;
import com.cristiancollazos.paybird.repository.dto.RouteDTO;
import com.cristiancollazos.paybird.view.dialog.interfaces.ConfirmDialogListener;
import com.cristiancollazos.paybird.misc.Utilities;
import com.cristiancollazos.paybird.presenter.LoginPresenter;
import com.cristiancollazos.paybird.view.activity.BaseActivity;

import java.util.List;

public class LoginFragment extends Fragment
        implements View.OnClickListener, LoginPresenter.View {

    private static final String TAG = LoginFragment.class.getSimpleName();
    private TextInputEditText etLoginUsername, etLoginPassword;
    private Button btnLoginDologin;
    private ImageButton btnLoginGoSettings;
    private LoginPresenter objPresenter;

    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        setBaseEnvironment();

        View objView = inflater.inflate(R.layout.fragment_login, container, false);

        etLoginUsername = objView.findViewById(R.id.etLoginUsername);
        etLoginPassword = objView.findViewById(R.id.etLoginPassword);
        btnLoginDologin = objView.findViewById(R.id.btnLoginDoLogin);
        btnLoginGoSettings = objView.findViewById(R.id.btnLoginGoSettings);

        btnLoginDologin.setOnClickListener(this);
        btnLoginGoSettings.setOnClickListener(this);

        initializeFragment();

        return objView;
    }

    private void setBaseEnvironment() {
        ((BaseActivity) getActivity()).setToolbarVisible(false);
        ((BaseActivity) getActivity()).setDrawerEnabled(false);
        ((BaseActivity) getActivity()).setActionBarTitle(null);
        ((BaseActivity) getActivity()).setActionBarSubtitle(null);
    }

    private void initializeFragment() {
        objPresenter = new LoginPresenter(this);
    }

    @Override
    public void onClick(View objView) {
        switch (objView.getId()) {
            case R.id.btnLoginGoSettings:
                Utilities.showConfirmationDialog(getContext(),
                        getResources().getString(R.string.login_confirmGoSettings),
                        new ConfirmDialogListener() {
                            @Override
                            public void onConfirm(AlertDialog objAlertDialog, Object... rcDialogData) {
                                objPresenter.goToSettings();
                                objAlertDialog.dismiss();
                            }

                            @Override
                            public void onCancel(AlertDialog objAlertDialog) {
                                objAlertDialog.dismiss();
                            }
                        }, R.drawable.ic_settings_black_24dp);
                break;
            case R.id.btnLoginDoLogin:
                if (etLoginUsername.getText().toString().isEmpty()) {
                    etLoginUsername.setError(getResources().getString(R.string.gen_error_mandatory));
                    break;
                }

                if (etLoginPassword.getText().toString().isEmpty()) {
                    etLoginPassword.setError(getResources().getString(R.string.gen_error_mandatory));
                    break;
                }

                String sbUser = etLoginUsername.getText().toString();
                String sbPassword = etLoginPassword.getText().toString();

                objPresenter.doLogin(sbUser, sbPassword);
                break;
        }
    }

    @Override
    public void redirectToSettingsFragment() {
        SettingsFragment objFragment = new SettingsFragment();
        ((BaseActivity) getActivity()).replaceFragment(objFragment, true);
    }

    @Override
    public void showProgress(String sbLoadingMessage) {
        ((BaseActivity) getActivity()).showProgressScreen(sbLoadingMessage);
    }

    @Override
    public void hideProgress() {
        ((BaseActivity) getActivity()).hideProgressScreen();
    }

    @Override
    public void showErrorMessage(String sbError, String sbRecommend) {
        ((BaseActivity) getActivity()).showErrorMessage(sbError, sbRecommend);
    }

    @Override
    public void redirectToMainFragment() {
        TodayPaymentsFragment objFragment = new TodayPaymentsFragment();
        ((BaseActivity) getActivity()).replaceFragment(objFragment, false);
    }

    @Override
    public void notifyUserLoggedOnUi(String sbFullname, String sbEmail,
                                     String sbDocument, List<RouteDTO> lstRoutesDTO) {
        ((BaseActivity) getActivity()).setLoggedUserOnUi(sbFullname, sbEmail, sbDocument, lstRoutesDTO);
    }

}
