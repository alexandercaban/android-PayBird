package com.cristiancollazos.paybird.interactor;

import com.cristiancollazos.paybird.interactor.callback.ResponseCallback;
import com.cristiancollazos.paybird.repository.dto.RouteItemDTO;

import java.util.List;

public interface RouteOrderInteractor {

    void getRouteOrderByRoute(ResponseCallback<List<RouteItemDTO>> objResponseCallback,
                              Integer nuRouteCode);

    void setReorderRoute(ResponseCallback<Boolean> obResponseCallback,
                         Integer nuRouteCode,
                         Integer nuRouteOrderFrom,
                         Integer nuRouteOrderTo);

    void getRouteOrderByRouteWithOffset(ResponseCallback<List<RouteItemDTO>> objResponseCallback,
                                        Integer nuRouteCode);

}
