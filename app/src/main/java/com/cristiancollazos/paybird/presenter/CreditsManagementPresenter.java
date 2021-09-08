package com.cristiancollazos.paybird.presenter;

import com.cristiancollazos.paybird.interactor.CreditsInteractor;
import com.cristiancollazos.paybird.interactor.CustomerInteractor;
import com.cristiancollazos.paybird.interactor.callback.ResponseCallback;
import com.cristiancollazos.paybird.interactor.impl.CreditsInteractorImpl;
import com.cristiancollazos.paybird.interactor.impl.CustomerInteractorImpl;
import com.cristiancollazos.paybird.misc.Constants;
import com.cristiancollazos.paybird.misc.enums.ErrorConstants;
import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.repository.LocalRepository;
import com.cristiancollazos.paybird.repository.dto.CreditDTO;
import com.cristiancollazos.paybird.repository.dto.CustomerDTO;
import com.cristiancollazos.paybird.repository.dto.MovementDTO;
import com.cristiancollazos.paybird.repository.impl.LocalRepositoryImpl;

import java.util.List;

public class CreditsManagementPresenter {

    public interface View {

        void showProgress();

        void hideProgress();

        void showErrorMessage(String sbError, String sbRecommend);

        void renderCustomers(List<CustomerDTO> lstCustomersDTO);

        void renderCustomersWithinDialog(List<CustomerDTO> lstCustomersDTO);

        void showSuccessMessage(String sbMessage);

        void renderCustomerData(CustomerDTO objCustomerDTO);

        void notifyNoCustomersFound();

        void notifyNoCreditsAssociated();

        void renderAssociatedCredits(List<CreditDTO> lstCreditsDTO);

        void renderCreditMovements(List<MovementDTO> lstMovementDTO, Integer nuCreditCode);

        void notifyNoMovements(Integer nuCreditCode);

        void redirectToCreateCredit(CustomerDTO objCustomerDTO);

        void refreshCustomerData(CustomerDTO obCustomerDTO);

    }

    private View objView;
    private CustomerInteractor objCustomerInteractor;
    private CreditsInteractor objCreditsInteractor;
    private LocalRepository objLocalRepository;

    public CreditsManagementPresenter(View objView) {
        this.objView = objView;
        objCustomerInteractor = new CustomerInteractorImpl();
        objCreditsInteractor = new CreditsInteractorImpl();
        objLocalRepository = new LocalRepositoryImpl();
    }

    public void getCustomersByRoute(Integer nuRouteCode) {
        objView.showProgress();

        objCustomerInteractor.getSimpleCustomersByRouteFilter(
                new ResponseCallback<List<CustomerDTO>>() {
                    @Override
                    public void success(List<CustomerDTO> objResponse) {
                        objView.renderCustomers(objResponse);
                        objView.hideProgress();
                    }

                    @Override
                    public void failure(AppException objException) {
                        objView.hideProgress();

                        if (objException.getErrorCode() == Constants.NO_DATA_FOUND) {
                            objView.notifyNoCustomersFound();
                        } else {
                            objView.showErrorMessage(objException.getMessage(),
                                    objException.getRecommend());
                        }
                    }
                }, nuRouteCode, null);
    }

    public void getCustomersByRouteFilter(Integer nuRouteCode, String sbCustomerNameFilter) {
        objView.showProgress();

        objCustomerInteractor.getSimpleCustomersByRouteFilter(
                new ResponseCallback<List<CustomerDTO>>() {
                    @Override
                    public void success(List<CustomerDTO> objResponse) {
                        objView.renderCustomersWithinDialog(objResponse);
                        objView.hideProgress();
                    }

                    @Override
                    public void failure(AppException objException) {
                        objView.hideProgress();

                        if (objException.getErrorCode() == Constants.NO_DATA_FOUND) {
                            objView.notifyNoCustomersFound();
                        } else {
                            objView.showErrorMessage(objException.getMessage(),
                                    objException.getRecommend());
                        }
                    }
                }, nuRouteCode, sbCustomerNameFilter);
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

    public void getCreditsByCustomer(Integer nuCustomerCode) {
        objView.showProgress();

        objCreditsInteractor.getCreditsByCustomer(new ResponseCallback<List<CreditDTO>>() {
            @Override
            public void success(List<CreditDTO> objResponse) {
                objView.renderAssociatedCredits(objResponse);
                objView.hideProgress();
            }

            @Override
            public void failure(AppException objException) {
                objView.hideProgress();
                if (objException.getErrorCode() == Constants.NO_DATA_FOUND) {
                    objView.notifyNoCreditsAssociated();
                } else {
                    objView.showErrorMessage(objException.getMessage(),
                            objException.getRecommend());
                }
            }
        }, nuCustomerCode);
    }

    public void getMovementsByCredit(final Integer nuCreditCode) {
        objView.showProgress();

        objCreditsInteractor.getMovementsByCredit(new ResponseCallback<List<MovementDTO>>() {
            @Override
            public void success(List<MovementDTO> objResponse) {
                objView.renderCreditMovements(objResponse, nuCreditCode);
                objView.hideProgress();
            }

            @Override
            public void failure(AppException objException) {
                objView.hideProgress();
                if (objException.getErrorCode() == Constants.NO_DATA_FOUND) {
                    objView.notifyNoMovements(nuCreditCode);
                } else {
                    objView.showErrorMessage(objException.getMessage(),
                            objException.getRecommend());
                }
            }
        }, nuCreditCode);
    }

    public void setSelectedCustomerAtLocalStorage(CustomerDTO objCustomer) {
        objView.showProgress();

        if (objLocalRepository.setSingleData(Constants.LOCALKEY_CUSTOMER, objCustomer,
                CustomerDTO.class)) {
            objView.redirectToCreateCredit(objCustomer);
            objView.hideProgress();
        } else {
            objView.hideProgress();
            objView.showErrorMessage(ErrorConstants.E0012.def(), ErrorConstants.E0012.recommend());
        }
    }

    public void getSelectedCustomerFromLocalStorage() {
        objView.showProgress();

        CustomerDTO objCustomerDTO = objLocalRepository.getSingleData(Constants.LOCALKEY_CUSTOMER,
                CustomerDTO.class);

        if (objCustomerDTO != null) {
            if (objLocalRepository.setSingleData(Constants.LOCALKEY_CUSTOMER, null,
                    CustomerDTO.class)) {
                objView.refreshCustomerData(objCustomerDTO);
                objView.hideProgress();
            } else {
                objView.hideProgress();
                objView.showErrorMessage(ErrorConstants.E0012.def(), ErrorConstants.E0012.recommend());
            }
        } else {
            objView.hideProgress();
        }
    }

}
