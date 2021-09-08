package com.cristiancollazos.paybird.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cristiancollazos.paybird.R;
import com.cristiancollazos.paybird.misc.Utilities;
import com.cristiancollazos.paybird.presenter.SettingsPresenter;
import com.cristiancollazos.paybird.view.activity.BaseActivity;
import com.playtechla.bluetoothlink.LinkBluetoothDeviceActivity;
import com.playtechla.bluetoothlink.LinkBluetoothDeviceHandler;

public class SettingsFragment extends Fragment
        implements SettingsPresenter.View, View.OnClickListener {

    private static final int REQUEST_BLUETOOTH_CONNECT = 1;
    private TextInputEditText etSettingsIp, etSettingsPort;
    private FloatingActionButton btnSettingSaveNetwork;
    private SettingsPresenter objPresenter;
    private Button btnSettingsPrinter_getAvailablePrinters;
    private TextView tvSettingsPrinter_Name, tvSettingsPrinter_Mac;

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setBaseEnvironment();

        View objView = inflater.inflate(R.layout.fragment_settings, container, false);

        etSettingsIp = objView.findViewById(R.id.etSettingsIp);
        etSettingsPort = objView.findViewById(R.id.etSettingsPort);
        btnSettingSaveNetwork = objView.findViewById(R.id.btnSettingsNetworkSave);
        btnSettingsPrinter_getAvailablePrinters =
                objView.findViewById(R.id.btnSettingsPrinter_getAvailablePrinters);
        tvSettingsPrinter_Name = objView.findViewById(R.id.tvSettingsPrinter_Name);
        tvSettingsPrinter_Mac = objView.findViewById(R.id.tvSettingsPrinter_Mac);

        btnSettingSaveNetwork.setOnClickListener(this);
        btnSettingsPrinter_getAvailablePrinters.setOnClickListener(this);

        initializeFragment();
        return objView;
    }

    private void setBaseEnvironment() {
        ((BaseActivity) getActivity()).setToolbarVisible(false);
        ((BaseActivity) getActivity()).setDrawerEnabled(false);
        ((BaseActivity) getActivity()).setActionBarTitle(null);
        ((BaseActivity) getActivity()).setActionBarSubtitle(null);
    }

    public void initializeFragment() {
        objPresenter = new SettingsPresenter(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        objPresenter.getSavedSettings();
    }

    @Override
    public void onClick(View objView) {
        switch (objView.getId()) {
            case R.id.btnSettingsNetworkSave:
                if (etSettingsIp.getText().toString().isEmpty()) {
                    etSettingsIp.setError(getResources().getString(R.string.gen_error_mandatory));
                    break;
                }

                if (etSettingsPort.getText().toString().isEmpty()) {
                    etSettingsPort.setError(getResources().getString(R.string.gen_error_mandatory));
                    break;
                }

                String sbIp = etSettingsIp.getText().toString();
                String sbPort = etSettingsPort.getText().toString();
                String sbPrinterName = tvSettingsPrinter_Name.equals("SIN DEFINIR")? null
                        : tvSettingsPrinter_Name.getText().toString();
                String sbPrinterMac = tvSettingsPrinter_Mac.equals("SIN DEFINIR")? null
                        : tvSettingsPrinter_Mac.getText().toString();

                objPresenter.doSaveSettings(sbIp, sbPort, sbPrinterName, sbPrinterMac);
                break;

            case R.id.btnSettingsPrinter_getAvailablePrinters:
                Intent objIntent = new Intent(getActivity(), LinkBluetoothDeviceActivity.class);
                startActivityForResult(objIntent, REQUEST_BLUETOOTH_CONNECT);
                break;
        }
    }

    @Override
    public void notifySaveSettingSuccess() {
        Utilities.showMessageDialog(getContext(), getResources().getString(R.string.settings_action_success));
        ((BaseActivity) getActivity()).onBackPressed();
    }

    @Override
    public void notifySaveSettingFailure() {
        Utilities.showMessageDialog(getContext(), getResources().getString(R.string.settings_action_failure));
    }

    @Override
    public void showMessageError(String sbMessage) {
        Utilities.showMessageDialog(getContext(), sbMessage);
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
    public void renderSettings(String sbIp, String sbPort, String sbPrinterName,
                               String sbPrinterMac) {
        etSettingsIp.setText(sbIp);
        etSettingsPort.setText(sbPort);
        tvSettingsPrinter_Name.setText((sbPrinterName != null)? sbPrinterName : "SIN DEFINIR");
        tvSettingsPrinter_Mac.setText((sbPrinterMac != null)? sbPrinterMac : "SIN DEFINIR");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_BLUETOOTH_CONNECT) {
            if (resultCode == Activity.RESULT_OK) {
                String sbBTAddress = data.getStringExtra(LinkBluetoothDeviceHandler.EXTRA_ADDRESS);
                String sbNameBT = data.getStringExtra(LinkBluetoothDeviceHandler.EXTRA_DEVICE_NAME);
                tvSettingsPrinter_Mac.setText(sbBTAddress);
                tvSettingsPrinter_Name.setText(sbNameBT);
            }
        }
    }

}
