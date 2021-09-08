package com.cristiancollazos.paybird.presenter;

import com.cristiancollazos.paybird.interactor.AuthenticationInteractor;
import com.cristiancollazos.paybird.interactor.callback.ResponseCallback;
import com.cristiancollazos.paybird.interactor.impl.AuthenticationInteractorImpl;
import com.cristiancollazos.paybird.misc.Constants;
import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.misc.interfaces.BluetoothPrinterNotifier;
import com.cristiancollazos.paybird.repository.LocalRepository;
import com.cristiancollazos.paybird.repository.dto.AlarmIndicatorDTO;
import com.cristiancollazos.paybird.repository.dto.RouteDTO;
import com.cristiancollazos.paybird.repository.dto.SettingDTO;
import com.cristiancollazos.paybird.repository.dto.UserDTO;
import com.cristiancollazos.paybird.repository.impl.LocalRepositoryImpl;

import java.util.Date;
import java.util.List;

public class BasePresenter {

    public interface View {
        void redirectToLoginFragment();
        void redirectToTodayFragment();
        void showProgressBar();
        void hideProgressBar();
        void setLoggedUserOnUi(String sbFullname, String sbEmail,
                               String sbDocument, List<RouteDTO> lstRoutesDTO);
        void showProgressScreen(String sbLoadingMessage);
        void hideProgressScreen();
        void showErrorMessage(String sbError, String sbRecommend);
        void showExpiredSesion();
        void connectToPrinter(String sbPrinterMac, BluetoothPrinterNotifier objNotifier);
        void notifyNoPrinterFound();
        void createAlarms();
    }

    private View objView;
    private LocalRepository objLocalRepository;
    private AuthenticationInteractor objAuthenticationInteractor;

    public BasePresenter(View objView) {
        this.objView = objView;
        this.objLocalRepository = new LocalRepositoryImpl();
        this.objAuthenticationInteractor = new AuthenticationInteractorImpl();
    }

    public void initializeApplication() {
        UserDTO objUserDTO = objLocalRepository
                .getSingleData(Constants.LOCALKEY_USERDATA, UserDTO.class);

        if (objUserDTO != null) {
            if ((objUserDTO.getLgLoginTime() + 3600000) > new Date().getTime()) {
                objView.showProgressScreen("Recuperando sesión");

                objAuthenticationInteractor.doLogin(new ResponseCallback<UserDTO>() {
                    @Override
                    public void success(UserDTO objResponse) {
                        getRoute(objResponse);
                    }

                    @Override
                    public void failure(AppException objException) {
                        objView.redirectToLoginFragment();
                        objView.showErrorMessage(objException.getMessage(), objException.getRecommend());
                        objView.hideProgressScreen();
                    }
                }, objUserDTO.getSbLogin(), objUserDTO.getSbPassword());
            } else {
                objView.redirectToLoginFragment();
                objView.showExpiredSesion();
            }
        } else {
            objView.redirectToLoginFragment();
        }
    }

    public void getRoute(final UserDTO objUserDTO) {
        objAuthenticationInteractor.getRoute(new ResponseCallback<List<RouteDTO>>() {
            @Override
            public void success(List<RouteDTO> objResponse) {
                objUserDTO.setLgLoginTime(new Date().getTime());

                objLocalRepository.setSingleData(Constants.LOCALKEY_USERDATA,
                        objUserDTO, UserDTO.class);
                objLocalRepository.setListData(Constants.LOCALKEY_ROUTEDATA,
                        objResponse, RouteDTO.class);

                objView.setLoggedUserOnUi(objUserDTO.getSbName() + " " + objUserDTO.getSbLastName(),
                        objUserDTO.getSbEmail(),
                        objUserDTO.getSbDocumentType() + " " + objUserDTO.getSbDocument(),
                        objResponse);

                objView.redirectToTodayFragment();
                objView.hideProgressScreen();
            }

            @Override
            public void failure(AppException objException) {
                objLocalRepository.setSingleData(Constants.LOCALKEY_USERDATA,
                        null, UserDTO.class);
                objView.showErrorMessage(objException.getMessage(), objException.getRecommend());
                objView.redirectToLoginFragment();
                objView.hideProgressScreen();
            }
        }, objUserDTO.getNuCode());
    }

    public void checkIfAlarmsCreatingNeeded() {
        AlarmIndicatorDTO objAlarmIndicatorDTO =
                objLocalRepository.getSingleData(Constants.LOCALKEY_CREATEALARMSINDICATOR,
                        AlarmIndicatorDTO.class);
        if (objAlarmIndicatorDTO == null || objAlarmIndicatorDTO.getBlIndicator()) {
            objView.createAlarms();
        }
    }

    public void doLogout() {
        objView.showProgressScreen("Cerrando sesión");
        if (objLocalRepository
                .setSingleData(Constants.LOCALKEY_USERDATA, null, UserDTO.class)) {
            objLocalRepository
                    .setSingleData(Constants.LOCALKEY_ROUTEDATA, null, RouteDTO.class);
            objView.redirectToLoginFragment();
        }
        objView.hideProgressScreen();
    }

    public void getPrinterMacAndConnect(BluetoothPrinterNotifier objNotifier) {
        objView.showProgressBar();
        SettingDTO objSettingDTO = objLocalRepository.getSingleData(Constants.LOCALKEY_SETTINGS,
                SettingDTO.class);

        if (objSettingDTO != null) {
            if (objSettingDTO.getSbPrinterMac() != null) {
                objView.connectToPrinter(objSettingDTO.getSbPrinterMac(), objNotifier);
                objView.hideProgressBar();
            } else {
                objView.hideProgressBar();
                objView.notifyNoPrinterFound();
            }
        } else {
            objView.hideProgressBar();
            objView.notifyNoPrinterFound();
        }
    }

}
