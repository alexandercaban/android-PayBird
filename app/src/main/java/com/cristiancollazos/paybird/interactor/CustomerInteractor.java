package com.cristiancollazos.paybird.interactor;

import com.cristiancollazos.paybird.interactor.callback.ResponseCallback;
import com.cristiancollazos.paybird.repository.dto.CustomerDTO;

import java.util.List;

public interface CustomerInteractor {

    void getCustomerByDocument(ResponseCallback<CustomerDTO> objResponseCallback,
                               String sbDocument);

    void getCustomersByRoute(ResponseCallback<List<CustomerDTO>> objResponseCallback,
                             Integer nuRoute);

    void setNewCustomer(ResponseCallback<Boolean> objResponseCallback,
                        CustomerDTO objCustomerDTO);

    void setEditCustomer(ResponseCallback<Boolean> objResponseCallback,
                         CustomerDTO objCustomerDTO);

    void getSimpleCustomersByRouteFilter(ResponseCallback<List<CustomerDTO>> objResponseCallback,
                                         Integer nuRoute,
                                         String sbCustomerName);

}
