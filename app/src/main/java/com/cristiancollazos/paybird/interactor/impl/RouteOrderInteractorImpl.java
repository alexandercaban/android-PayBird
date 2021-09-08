package com.cristiancollazos.paybird.interactor.impl;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.cristiancollazos.paybird.interactor.RouteOrderInteractor;
import com.cristiancollazos.paybird.interactor.callback.ResponseCallback;
import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.repository.RouteOrderRepository;
import com.cristiancollazos.paybird.repository.dto.RouteItemDTO;
import com.cristiancollazos.paybird.repository.impl.RouteOrderRepositoryImpl;

import java.util.ArrayList;
import java.util.List;

public class RouteOrderInteractorImpl implements RouteOrderInteractor {

    private RouteOrderRepository objRouteOrderRepository;

    public RouteOrderInteractorImpl() {
        objRouteOrderRepository = new RouteOrderRepositoryImpl();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getRouteOrderByRoute(final ResponseCallback<List<RouteItemDTO>> objResponseCallback,
                                     final Integer nuRouteCode) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... voids) {
                try {
                    return objRouteOrderRepository.getRouteOrderByRoute(nuRouteCode);
                } catch (Exception objException) {
                    return objException;
                }
            }

            @Override
            protected void onPostExecute(Object objResponse) {
                if (objResponse instanceof AppException) {
                    objResponseCallback.failure((AppException) objResponse);
                } else {
                    if (objResponse instanceof NullPointerException) {
                        objResponseCallback.success(new ArrayList<RouteItemDTO>());
                    } else {
                        objResponseCallback.success((List<RouteItemDTO>) objResponse);
                    }
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void setReorderRoute(final ResponseCallback<Boolean> obResponseCallback,
                                final Integer nuRouteCode,
                                final Integer nuRouteOrderFrom,
                                final Integer nuRouteOrderTo) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... voids) {
                try {
                    return objRouteOrderRepository.setReorderRoute(nuRouteCode,
                            nuRouteOrderFrom, nuRouteOrderTo);
                } catch (Exception objException) {
                    return objException;
                }
            }

            @Override
            protected void onPostExecute(Object objResponse) {
                if (objResponse instanceof AppException) {
                    obResponseCallback.failure((AppException) objResponse);
                } else {
                    obResponseCallback.success((Boolean) objResponse);
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getRouteOrderByRouteWithOffset(final ResponseCallback<List<RouteItemDTO>> objResponseCallback,
                                               final Integer nuRouteCode) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... voids) {
                try {
                    return objRouteOrderRepository.getRouteOrderByRouteWithOffset(nuRouteCode);
                } catch (Exception objException) {
                    return objException;
                }
            }

            @Override
            protected void onPostExecute(Object objResponse) {
                if (objResponse instanceof AppException) {
                    objResponseCallback.failure((AppException) objResponse);
                } else {
                    if (objResponse instanceof NullPointerException) {
                        objResponseCallback.success(new ArrayList<RouteItemDTO>());
                    } else {
                        objResponseCallback.success((List<RouteItemDTO>) objResponse);
                    }
                }
            }
        }.execute();
    }

}
