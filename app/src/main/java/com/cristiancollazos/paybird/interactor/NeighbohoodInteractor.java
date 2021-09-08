package com.cristiancollazos.paybird.interactor;

import com.cristiancollazos.paybird.interactor.callback.ResponseCallback;
import com.cristiancollazos.paybird.repository.dto.NeighborhoodDTO;

import java.util.List;

public interface NeighbohoodInteractor {

    void getNeighborhoods(ResponseCallback<List<NeighborhoodDTO>> objResponseCallback);

}
