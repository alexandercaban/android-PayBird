package com.cristiancollazos.paybird.presenter;

import com.cristiancollazos.paybird.interactor.CreditsInteractor;
import com.cristiancollazos.paybird.interactor.CustomerInteractor;
import com.cristiancollazos.paybird.interactor.RouteOrderInteractor;
import com.cristiancollazos.paybird.interactor.callback.ResponseCallback;
import com.cristiancollazos.paybird.interactor.impl.CreditsInteractorImpl;
import com.cristiancollazos.paybird.interactor.impl.CustomerInteractorImpl;
import com.cristiancollazos.paybird.interactor.impl.RouteOrderInteractorImpl;
import com.cristiancollazos.paybird.misc.Constants;
import com.cristiancollazos.paybird.misc.enums.ErrorConstants;
import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.repository.LocalRepository;
import com.cristiancollazos.paybird.repository.dto.CreditDTO;
import com.cristiancollazos.paybird.repository.dto.CustomerDTO;
import com.cristiancollazos.paybird.repository.dto.PeriodDTO;
import com.cristiancollazos.paybird.repository.dto.RouteItemDTO;
import com.cristiancollazos.paybird.repository.dto.UserDTO;
import com.cristiancollazos.paybird.repository.impl.LocalRepositoryImpl;

import java.util.List;

public class CreateCreditPresenter {

    public interface View {

        void showProgress();

        void hideProgress();

        void showErrorMessage(String sbError, String sbRecommend);

        void showSuccessMessage(String sbMessage);

        void renderCustomerData(CustomerDTO objCustomerDTO);

        void renderPeriods(List<PeriodDTO> lsPeriodDTO);

        void renderRouteOrder(List<RouteItemDTO> lstRouteItemDTO,
                              CreditDTO objCreditDTO,
                              Integer nuUserCode);

        void showProgressScreen(String sbLoadingMessage);

        void hideProgressScreen();

        void promptPrintOption(CreditDTO objRecentlyCreatedCredit);

    }

    private View objView;
    private CustomerInteractor objCustomerInteractor;
    private CreditsInteractor objCreditsInteractor;
    private LocalRepository objLocalRepository;
    private RouteOrderInteractor objRouteOrderInteractor;

    public CreateCreditPresenter(View objView) {
        this.objView = objView;
        objCustomerInteractor = new CustomerInteractorImpl();
        objCreditsInteractor = new CreditsInteractorImpl();
        objLocalRepository = new LocalRepositoryImpl();
        objRouteOrderInteractor = new RouteOrderInteractorImpl();
    }

    public void getCustomerByDocument(String sbDocument) {
        objView.showProgress();

        objCustomerInteractor.getCustomerByDocument(new ResponseCallback<CustomerDTO>() {
            @Override
            public void success(CustomerDTO objResponse) {
                objView.renderCustomerData(objResponse);
                objView.hideProgress();
            }

            @Override
            public void failure(AppException objException) {
                objView.showErrorMessage(objException.getMessage(),
                        objException.getRecommend());
                objView.hideProgress();
            }
        }, sbDocument);
    }

    public void getPeriods() {
        objView.showProgress();

        objCreditsInteractor.getPeriods(new ResponseCallback<List<PeriodDTO>>() {
            @Override
            public void success(List<PeriodDTO> objResponse) {
                objView.renderPeriods(objResponse);
                objView.hideProgress();
            }

            @Override
            public void failure(AppException objException) {
                objView.hideProgress();
                objView.showErrorMessage(objException.getMessage(), objException.getRecommend());
            }
        });
    }

    public void getRouteOrder(Integer nuRouteCode, final CreditDTO objCreditDTO) {
        objView.showProgress();

        objRouteOrderInteractor.getRouteOrderByRouteWithOffset(new ResponseCallback<List<RouteItemDTO>>() {
            @Override
            public void success(List<RouteItemDTO> objResponse) {
                UserDTO objUserDTO = objLocalRepository
                        .getSingleData(Constants.LOCALKEY_USERDATA, UserDTO.class);

                if (objUserDTO != null) {
                    objView.renderRouteOrder(objResponse, objCreditDTO, objUserDTO.getNuCode());
                    objView.hideProgress();
                } else {
                    objView.hideProgress();
                    objView.showErrorMessage(ErrorConstants.E0016.def(),
                            ErrorConstants.E0016.recommend());
                }
            }

            @Override
            public void failure(AppException objException) {
                objView.hideProgress();
                objView.showErrorMessage(objException.getMessage(), objException.getRecommend());
            }
        }, nuRouteCode);
    }

    public void setNewCredit(final CreditDTO objCreditDTO,
                             Integer nuRouteCode,
                             Integer nuRouteOrderPosition,
                             final Integer nuUserCode) {
        objView.showProgressScreen("Creando crédito");

        objCreditsInteractor.setNewCredit(new ResponseCallback<Boolean>() {
            @Override
            public void success(Boolean objResponse) {
                getRecentlyCreatedCredit(objCreditDTO.getNuCustomerCode());
            }

            @Override
            public void failure(AppException objException) {
                objView.hideProgressScreen();
                objView.showErrorMessage(objException.getMessage(), objException.getRecommend());
            }
        }, objCreditDTO, nuRouteCode, nuRouteOrderPosition, nuUserCode);
    }

    public void getRecentlyCreatedCredit(Integer nuCustomerCode) {
        objView.showProgressScreen("Generando recibo");

        objCreditsInteractor.getRecentlyCreatedCredit(new ResponseCallback<CreditDTO>() {
            @Override
            public void success(CreditDTO objResponse) {
                objView.promptPrintOption(objResponse);
                objView.hideProgressScreen();
            }

            @Override
            public void failure(AppException objException) {
                objView.hideProgressScreen();
                objView.showErrorMessage(objException.getMessage(), objException.getRecommend());
            }
        }, nuCustomerCode);
    }

}
