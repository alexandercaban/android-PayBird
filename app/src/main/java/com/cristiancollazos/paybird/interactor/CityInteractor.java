package com.cristiancollazos.paybird.interactor;

import com.cristiancollazos.paybird.interactor.callback.ResponseCallback;
import com.cristiancollazos.paybird.repository.dto.CityDTO;

import java.util.List;

public interface CityInteractor {

    void getCities(ResponseCallback<List<CityDTO>> objResponseCallback);

}
