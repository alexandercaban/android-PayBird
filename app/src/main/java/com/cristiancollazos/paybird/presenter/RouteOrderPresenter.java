package com.cristiancollazos.paybird.presenter;

import com.cristiancollazos.paybird.interactor.RouteOrderInteractor;
import com.cristiancollazos.paybird.interactor.callback.ResponseCallback;
import com.cristiancollazos.paybird.interactor.impl.RouteOrderInteractorImpl;
import com.cristiancollazos.paybird.misc.Constants;
import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.repository.dto.RouteItemDTO;

import java.util.List;

public class RouteOrderPresenter {

    public interface View {

        void showProgress();

        void hideProgress();

        void showErrorMessage(Integer nuCode, String sbError, String sbRecommend);

        void showSuccessMessage(String sbMessage);

        void renderRouteOrder(List<RouteItemDTO> lstRouteItemDTO);

        void notifyNoRouteOrderItemsFound();

        void notifyRouteOrderChange(Integer nuPositionFrom, Integer nuOrderFrom,
                                    Integer nuPositionTo, Integer nuOrderTo);

    }

    private RouteOrderInteractor objRouteOrderInteractor;
    private View objView;

    public RouteOrderPresenter(View objView) {
        this.objView = objView;
        objRouteOrderInteractor = new RouteOrderInteractorImpl();
    }

    public void getRouteOrderByRoute(Integer nuRouteCode) {
        objView.showProgress();
        objRouteOrderInteractor.getRouteOrderByRoute(new ResponseCallback<List<RouteItemDTO>>() {
            @Override
            public void success(List<RouteItemDTO> objResponse) {
                objView.renderRouteOrder(objResponse);
                objView.hideProgress();
            }

            @Override
            public void failure(AppException objException) {
                objView.hideProgress();
                if (objException.getErrorCode() == Constants.NO_DATA_FOUND) {
                    objView.notifyNoRouteOrderItemsFound();
                } else {
                    objView.showErrorMessage(objException.getErrorCode(),
                            objException.getMessage(), objException.getRecommend());
                }
            }
        }, nuRouteCode);
    }

    public void setChangeRouteOrder(final Integer nuRouteCode,
                                    final Integer nuOrderFrom,
                                    final Integer nuPositionFrom,
                                    final Integer nuOrderTo,
                                    final Integer nuPositionTo) {
        objView.showProgress();
        objRouteOrderInteractor.setReorderRoute(new ResponseCallback<Boolean>() {
            @Override
            public void success(Boolean objResponse) {
                objView.showSuccessMessage("Se ha cambiado el orden exitosamente");
                objView.notifyRouteOrderChange(nuPositionFrom, nuOrderFrom, nuPositionTo, nuOrderTo);
                //getRouteOrderByRoute(nuRouteCode);
                objView.hideProgress();
            }

            @Override
            public void failure(AppException objException) {
                objView.hideProgress();
                objView.showErrorMessage(objException.getErrorCode(), objException.getMessage(),
                        objException.getRecommend());
            }
        }, nuRouteCode, nuOrderFrom, nuOrderTo);
    }

}
