package com.cristiancollazos.paybird.interactor.impl;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.cristiancollazos.paybird.interactor.CityInteractor;
import com.cristiancollazos.paybird.interactor.callback.ResponseCallback;
import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.repository.CityRepository;
import com.cristiancollazos.paybird.repository.dto.CityDTO;
import com.cristiancollazos.paybird.repository.impl.CityRepositoryImpl;

import java.util.List;

public class CityInteractorImpl implements CityInteractor {

    private CityRepository objCityRepository;

    public CityInteractorImpl() {
        objCityRepository = new CityRepositoryImpl();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getCities(final ResponseCallback<List<CityDTO>> objResponseCallback) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... voids) {
                try {
                    return objCityRepository.getCities();
                } catch (Exception objException) {
                    return objException;
                }
            }

            @Override
            protected void onPostExecute(Object objResponse) {
                if (objResponse instanceof AppException) {
                    objResponseCallback.failure((AppException) objResponse);
                } else {
                    objResponseCallback.success((List<CityDTO>) objResponse);
                }
            }
        }.execute();
    }

}
