package com.cristiancollazos.paybird.presenter;

import com.cristiancollazos.paybird.interactor.AuthenticationInteractor;
import com.cristiancollazos.paybird.interactor.CityInteractor;
import com.cristiancollazos.paybird.interactor.CustomerInteractor;
import com.cristiancollazos.paybird.interactor.LocalInteractor;
import com.cristiancollazos.paybird.interactor.NeighbohoodInteractor;
import com.cristiancollazos.paybird.interactor.callback.ResponseCallback;
import com.cristiancollazos.paybird.interactor.impl.AuthenticationInteractorImpl;
import com.cristiancollazos.paybird.interactor.impl.CityInteractorImpl;
import com.cristiancollazos.paybird.interactor.impl.CustomerInteractorImpl;
import com.cristiancollazos.paybird.interactor.impl.LocalInteractorImpl;
import com.cristiancollazos.paybird.interactor.impl.NeighborhoodInteractorImpl;
import com.cristiancollazos.paybird.misc.Constants;
import com.cristiancollazos.paybird.misc.enums.ErrorConstants;
import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.repository.LocalRepository;
import com.cristiancollazos.paybird.repository.dto.CityDTO;
import com.cristiancollazos.paybird.repository.dto.CustomerDTO;
import com.cristiancollazos.paybird.repository.dto.DocumentTypeDTO;
import com.cristiancollazos.paybird.repository.dto.NeighborhoodDTO;
import com.cristiancollazos.paybird.repository.dto.RouteDTO;
import com.cristiancollazos.paybird.repository.dto.UserDTO;
import com.cristiancollazos.paybird.repository.impl.LocalRepositoryImpl;

import java.util.List;

public class CustomerPresenter {

    public interface View {

        void showProgress();

        void hideProgress();

        void showErrorMessage(String sbError, String sbRecommend);

        void renderCities(List<CityDTO> lstCitiesDTO);

        void renderNeighborhoods(List<NeighborhoodDTO> lstNeighborhoodsDTO);

        void renderRoutes(List<RouteDTO> lstRoutesDTO);

        void renderCustomerData(CustomerDTO objCustomer);

        void renderDocumentTypes(List<DocumentTypeDTO> lstDocumentTypesDTO);

        void cleanForm(Boolean blAlsoCleanDocument);

        void renderCustomers(List<CustomerDTO> lstCustomersDTO);

        void showSuccessMessage(String sbMessage);

        void notifyNoCustomersFound();

    }

    private AuthenticationInteractor objAuthenticationInteractor;
    private CityInteractor objCityInteractor;
    private NeighbohoodInteractor objNeighborhoodInteractor;
    private CustomerInteractor objCustomerInteractor;
    private LocalRepository objLocalRepository;
    private LocalInteractor objLocalInteractor;
    private View objView;

    public CustomerPresenter(View objView) {
        this.objView = objView;
        objAuthenticationInteractor = new AuthenticationInteractorImpl();
        objCityInteractor = new CityInteractorImpl();
        objNeighborhoodInteractor = new NeighborhoodInteractorImpl();
        objLocalRepository = new LocalRepositoryImpl();
        objCustomerInteractor = new CustomerInteractorImpl();
        objLocalInteractor = new LocalInteractorImpl();
    }

    public void getCities() {
        objView.showProgress();

        objCityInteractor.getCities(new ResponseCallback<List<CityDTO>>() {
            @Override
            public void success(List<CityDTO> objResponse) {
                objView.renderCities(objResponse);
                objView.hideProgress();
            }

            @Override
            public void failure(AppException objException) {
                objView.hideProgress();
                objView.showErrorMessage(objException.getMessage(), objException.getRecommend());
            }
        });
    }

    public void getNeighborhoods() {
        objView.showProgress();

        objNeighborhoodInteractor.getNeighborhoods(new ResponseCallback<List<NeighborhoodDTO>>() {
            @Override
            public void success(List<NeighborhoodDTO> objResponse) {
                objView.renderNeighborhoods(objResponse);
                objView.hideProgress();
            }

            @Override
            public void failure(AppException objException) {
                objView.hideProgress();
                objView.showErrorMessage(objException.getMessage(), objException.getRecommend());
            }
        });
    }

    public void getRoutes() {
        objView.showProgress();

        UserDTO objLoggedUserDTO = objLocalRepository
                .getSingleData(Constants.LOCALKEY_USERDATA, UserDTO.class);

        if (objLoggedUserDTO != null) {
            objAuthenticationInteractor.getRoute(new ResponseCallback<List<RouteDTO>>() {
                @Override
                public void success(List<RouteDTO> objResponse) {
                    objView.renderRoutes(objResponse);
                    objView.hideProgress();
                }

                @Override
                public void failure(AppException objException) {
                    objView.hideProgress();
                    objView.showErrorMessage(objException.getMessage(), objException.getRecommend());
                }
            }, objLoggedUserDTO.getNuCode());
        } else {
            objView.hideProgress();
            objView.showErrorMessage("No hay un usuario logueado",
                    "Debe loguearse con un usuario para obtener las rutas asociadas");
        }
    }

    public void validateIfCustomerExist(String sbDocument) {
        objView.showProgress();

        objCustomerInteractor.getCustomerByDocument(new ResponseCallback<CustomerDTO>() {
            @Override
            public void success(CustomerDTO objResponse) {
                objView.renderCustomerData(objResponse);
                objView.hideProgress();
            }

            @Override
            public void failure(AppException objException) {
                objView.cleanForm(false);
                objView.hideProgress();
            }
        }, sbDocument);
    }

    public void getDocumentTypes() {
        objView.showProgress();

        objLocalInteractor.getDocumentTypes(new ResponseCallback<List<DocumentTypeDTO>>() {
            @Override
            public void success(List<DocumentTypeDTO> objResponse) {
                objView.renderDocumentTypes(objResponse);
                objView.hideProgress();
            }

            @Override
            public void failure(AppException objException) {
                objView.hideProgress();
                objView.showErrorMessage(objException.getMessage(), objException.getRecommend());
            }
        });
    }

    public void getCustomersByRoute(Integer nuRoute) {
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
                            objView.showErrorMessage(objException.getMessage(), objException.getRecommend());
                        }
                    }
                }, nuRoute, null);
    }

    public void setNewCustomer(String sbDocumentType, String sbDocument, String sbName,
                               String sbLastName, String sbHomeAddress, Integer nuHomeCityCode,
                               String sbHomePhoneNumber, String sbMobileNumber, String sbWorkAddress,
                               Integer nuWorkCityCode, String bWorkPhoneNumber, String bCompanyName,
                               Integer nuRouteCode, Integer nuNeighborhoodCode) {
        objView.showProgress();

        Boolean blValidated;
        CustomerDTO objCustomerDTO = new CustomerDTO();

        blValidated = !(sbDocumentType.isEmpty() || sbDocumentType.equals(""));
        blValidated = (blValidated && !(sbDocument.isEmpty() || sbDocument.equals("")));
        blValidated = (blValidated && !(sbName.isEmpty() || sbName.equals("")));
        blValidated = (blValidated && !(sbLastName.isEmpty() || sbLastName.equals("")));
        blValidated = (blValidated && !(sbHomeAddress.isEmpty() || sbHomeAddress.equals("")));
        blValidated = (blValidated && (nuHomeCityCode != null));
        blValidated = (blValidated && !(sbHomePhoneNumber.isEmpty() || sbHomePhoneNumber.equals("")));
        blValidated = (blValidated && !(sbMobileNumber.isEmpty() || sbMobileNumber.equals("")));
        blValidated = (blValidated && !(sbWorkAddress.isEmpty() || sbWorkAddress.equals("")));
        blValidated = (blValidated && (nuWorkCityCode != null));
        blValidated = (blValidated && !(bWorkPhoneNumber.isEmpty() || bWorkPhoneNumber.equals("")));
        blValidated = (blValidated && !(bCompanyName.isEmpty() || bCompanyName.equals("")));
        blValidated = (blValidated && (nuRouteCode != null));
        blValidated = (blValidated && (nuNeighborhoodCode != null));

        if (blValidated) {
            objCustomerDTO.setSbDocumentType(sbDocumentType);
            objCustomerDTO.setSbDocument(sbDocument);
            objCustomerDTO.setSbName(sbName);
            objCustomerDTO.setSbLastName(sbLastName);
            objCustomerDTO.setSbHomeAddress(sbHomeAddress);
            objCustomerDTO.setNuHomeCityCode(nuHomeCityCode);
            objCustomerDTO.setSbHomePhoneNumber(sbHomePhoneNumber);
            objCustomerDTO.setSbMobileNumber(sbMobileNumber);
            objCustomerDTO.setSbWorkAddress(sbWorkAddress);
            objCustomerDTO.setNuWorkCityCode(nuWorkCityCode);
            objCustomerDTO.setSbWorkPhoneNumber(bWorkPhoneNumber);
            objCustomerDTO.setSbCompanyName(bCompanyName);
            objCustomerDTO.setNuRouteCode(nuRouteCode);
            objCustomerDTO.setNuNeighborhoodCode(nuNeighborhoodCode);

            objCustomerInteractor.setNewCustomer(new ResponseCallback<Boolean>() {
                @Override
                public void success(Boolean objResponse) {
                    if (objResponse) {
                        objView.showSuccessMessage("Se ha creado el cliente satisfactoriamente");
                        objView.cleanForm(true);
                        objView.hideProgress();
                    } else {
                        this.failure(new AppException(ErrorConstants.E0004));
                    }
                }

                @Override
                public void failure(AppException objException) {
                    objView.hideProgress();
                    objView.showErrorMessage(objException.getMessage(), objException.getRecommend());
                }
            }, objCustomerDTO);
        } else {
            objView.hideProgress();
            objView.showErrorMessage(ErrorConstants.EOO03.def(), ErrorConstants.EOO03.recommend());
        }
    }

    public void setEditCustomer(Integer nuCode, String sbDocumentType, String sbDocument, String sbName,
                                String sbLastName, String sbHomeAddress, Integer nuHomeCityCode,
                                String sbHomePhoneNumber, String sbMobileNumber, String sbWorkAddress,
                                Integer nuWorkCityCode, String bWorkPhoneNumber, String bCompanyName,
                                Integer nuRouteCode, Integer nuNeighborhoodCode) {
        objView.showProgress();

        Boolean blValidated;
        CustomerDTO objCustomerDTO = new CustomerDTO();

        blValidated = nuCode != null;
        blValidated = (blValidated && !(sbDocumentType.isEmpty() || sbDocumentType.equals("")));
        blValidated = (blValidated && !(sbDocument.isEmpty() || sbDocument.equals("")));
        blValidated = (blValidated && !(sbName.isEmpty() || sbName.equals("")));
        blValidated = (blValidated && !(sbLastName.isEmpty() || sbLastName.equals("")));
        blValidated = (blValidated && !(sbHomeAddress.isEmpty() || sbHomeAddress.equals("")));
        blValidated = (blValidated && (nuHomeCityCode != null));
        blValidated = (blValidated && !(sbHomePhoneNumber.isEmpty() || sbHomePhoneNumber.equals("")));
        blValidated = (blValidated && !(sbMobileNumber.isEmpty() || sbMobileNumber.equals("")));
        blValidated = (blValidated && !(sbWorkAddress.isEmpty() || sbWorkAddress.equals("")));
        blValidated = (blValidated && (nuWorkCityCode != null));
        blValidated = (blValidated && !(bWorkPhoneNumber.isEmpty() || bWorkPhoneNumber.equals("")));
        blValidated = (blValidated && !(bCompanyName.isEmpty() || bCompanyName.equals("")));
        blValidated = (blValidated && (nuRouteCode != null));
        blValidated = (blValidated && (nuNeighborhoodCode != null));

        if (blValidated) {
            objCustomerDTO.setNuCode(nuCode);
            objCustomerDTO.setSbDocumentType(sbDocumentType);
            objCustomerDTO.setSbDocument(sbDocument);
            objCustomerDTO.setSbName(sbName);
            objCustomerDTO.setSbLastName(sbLastName);
            objCustomerDTO.setSbHomeAddress(sbHomeAddress);
            objCustomerDTO.setNuHomeCityCode(nuHomeCityCode);
            objCustomerDTO.setSbHomePhoneNumber(sbHomePhoneNumber);
            objCustomerDTO.setSbMobileNumber(sbMobileNumber);
            objCustomerDTO.setSbWorkAddress(sbWorkAddress);
            objCustomerDTO.setNuWorkCityCode(nuWorkCityCode);
            objCustomerDTO.setSbWorkPhoneNumber(bWorkPhoneNumber);
            objCustomerDTO.setSbCompanyName(bCompanyName);
            objCustomerDTO.setNuRouteCode(nuRouteCode);
            objCustomerDTO.setNuNeighborhoodCode(nuNeighborhoodCode);

            objCustomerInteractor.setEditCustomer(new ResponseCallback<Boolean>() {
                @Override
                public void success(Boolean objResponse) {
                    if (objResponse) {
                        objView.showSuccessMessage("Se ha editado el cliente satisfactoriamente");
                        objView.cleanForm(true);
                        objView.hideProgress();
                    } else {
                        this.failure(new AppException(ErrorConstants.E0004));
                    }
                }

                @Override
                public void failure(AppException objException) {
                    objView.hideProgress();
                    objView.showErrorMessage(objException.getMessage(), objException.getRecommend());
                }
            }, objCustomerDTO);
        } else {
            objView.hideProgress();
            objView.showErrorMessage(ErrorConstants.EOO03.def(), ErrorConstants.EOO03.recommend());
        }
    }

}
