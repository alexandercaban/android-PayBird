package com.cristiancollazos.paybird.presenter;

import com.cristiancollazos.paybird.interactor.AuthenticationInteractor;
import com.cristiancollazos.paybird.interactor.impl.AuthenticationInteractorImpl;
import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.misc.Constants;
import com.cristiancollazos.paybird.interactor.callback.ResponseCallback;
import com.cristiancollazos.paybird.repository.LocalRepository;
import com.cristiancollazos.paybird.repository.dto.RouteDTO;
import com.cristiancollazos.paybird.repository.dto.UserDTO;
import com.cristiancollazos.paybird.repository.impl.LocalRepositoryImpl;

import java.util.Date;
import java.util.List;

public class LoginPresenter {

    public interface View {
        void showProgress(String sbLoadingMessage);
        void hideProgress();
        void showErrorMessage(String sbError, String sbRecommend);
        void redirectToSettingsFragment();
        void redirectToMainFragment();
        void notifyUserLoggedOnUi(String sbFullname, String sbEmail,
                                  String sbDocument, List<RouteDTO> lstRoutesDTO);
    }

    private View objView;
    private AuthenticationInteractor objAuthenticationInteractor;
    private LocalRepository objLocalRepository;

    public LoginPresenter(View objView) {
        this.objView = objView;
        this.objAuthenticationInteractor = new AuthenticationInteractorImpl();
        this.objLocalRepository = new LocalRepositoryImpl();
    }

    public void goToSettings() {
        objView.redirectToSettingsFragment();
    }

    public void doLogin(String sbUser, String sbPassword) {
        objView.showProgress("Iniciando Sesión");
        objAuthenticationInteractor.doLogin(new ResponseCallback<UserDTO>() {
            @Override
            public void success(UserDTO objResponse) {
                getRoute(objResponse);
                objView.hideProgress();
            }

            @Override
            public void failure(AppException objException) {
                objView.showErrorMessage(objException.getMessage(), objException.getRecommend());
                objView.hideProgress();
            }
        }, sbUser, sbPassword);
    }

    private void getRoute(final UserDTO objUserDTO) {
        objView.showProgress("Obteniendo Ruta");
        objAuthenticationInteractor.getRoute(new ResponseCallback<List<RouteDTO>>() {
            @Override
            public void success(List<RouteDTO> objResponse) {
                objUserDTO.setLgLoginTime(new Date().getTime());

                objLocalRepository.setSingleData(Constants.LOCALKEY_USERDATA,
                        objUserDTO, UserDTO.class);
                objLocalRepository.setListData(Constants.LOCALKEY_ROUTEDATA,
                        objResponse, RouteDTO.class);

                objView.notifyUserLoggedOnUi(objUserDTO.getSbName() + " " + objUserDTO.getSbLastName(),
                        objUserDTO.getSbEmail(),
                        objUserDTO.getSbDocumentType() + " " + objUserDTO.getSbDocument(),
                        objResponse);

                objView.redirectToMainFragment();
                objView.hideProgress();
            }

            @Override
            public void failure(AppException objException) {
                objView.showErrorMessage(objException.getMessage(), objException.getRecommend());
                objView.hideProgress();
            }
        }, objUserDTO.getNuCode());
    }

}
