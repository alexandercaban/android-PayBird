package com.cristiancollazos.paybird.interactor;

import com.cristiancollazos.paybird.interactor.callback.ResponseCallback;
import com.cristiancollazos.paybird.repository.dto.RouteDTO;
import com.cristiancollazos.paybird.repository.dto.UserDTO;

import java.util.List;

public interface AuthenticationInteractor {

    void doLogin(ResponseCallback<UserDTO> objResponseCallback,
                 String sbUser,
                 String sbPassword);

    void getRoute(ResponseCallback<List<RouteDTO>> objResponseCallback,
                  Integer nuUserCode);

}
