package com.cristiancollazos.paybird.interactor.impl;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.cristiancollazos.paybird.interactor.AuthenticationInteractor;
import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.interactor.callback.ResponseCallback;
import com.cristiancollazos.paybird.repository.AuthenticationRepository;
import com.cristiancollazos.paybird.repository.dto.RouteDTO;
import com.cristiancollazos.paybird.repository.dto.UserDTO;
import com.cristiancollazos.paybird.repository.impl.AuthenticationRepositoryImpl;

import java.util.List;

public class AuthenticationInteractorImpl implements AuthenticationInteractor {

    private AuthenticationRepository objAuthenticationRepository;

    public AuthenticationInteractorImpl() {
        objAuthenticationRepository = new AuthenticationRepositoryImpl();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void doLogin(final ResponseCallback<UserDTO> objResponseCallback,
                        final String sbUser,
                        final String sbPassword) {

        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... objParams) {
                try {
                    return objAuthenticationRepository.doLogin(sbUser, sbPassword);
                } catch (AppException objException) {
                    return objException;
                }
            }

            @Override
            protected void onPostExecute(Object objResponse) {
                if (objResponse instanceof AppException) {
                    objResponseCallback.failure((AppException) objResponse);
                } else {
                    objResponseCallback.success((UserDTO) objResponse);
                }
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getRoute(final ResponseCallback<List<RouteDTO>> objResponseCallback,
                         final Integer nuUserCode) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... objParams) {
                try {
                    return objAuthenticationRepository.getRoute(nuUserCode);
                } catch (AppException objException) {
                    return objException;
                }
            }

            @Override
            protected void onPostExecute(Object objResponse) {
                if (objResponse instanceof AppException) {
                    objResponseCallback.failure((AppException) objResponse);
                } else {
                    objResponseCallback.success((List<RouteDTO>) objResponse);
                }
            }
        }.execute();
    }

}
