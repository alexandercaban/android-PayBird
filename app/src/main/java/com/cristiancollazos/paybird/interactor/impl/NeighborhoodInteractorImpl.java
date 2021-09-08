package com.cristiancollazos.paybird.interactor.impl;

import android.annotation.SuppressLint;
import android.os.AsyncTask;

import com.cristiancollazos.paybird.interactor.NeighbohoodInteractor;
import com.cristiancollazos.paybird.interactor.callback.ResponseCallback;
import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.repository.NeighborhoodRepository;
import com.cristiancollazos.paybird.repository.dto.NeighborhoodDTO;
import com.cristiancollazos.paybird.repository.impl.NeighborhoodRepositoryImpl;

import java.util.List;

public class NeighborhoodInteractorImpl implements NeighbohoodInteractor {

    private NeighborhoodRepository objNeighborhoodRepository;

    public NeighborhoodInteractorImpl() {
        objNeighborhoodRepository = new NeighborhoodRepositoryImpl();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getNeighborhoods(final ResponseCallback<List<NeighborhoodDTO>> objResponseCallback) {
        new AsyncTask<Void, Void, Object>() {
            @Override
            protected Object doInBackground(Void... voids) {
                try {
                    return objNeighborhoodRepository.getNeighborhoods();
                } catch (Exception objException) {
                    return objException;
                }
            }

            @Override
            protected void onPostExecute(Object objResponse) {
                if (objResponse instanceof AppException) {
                    objResponseCallback.failure((AppException) objResponse);
                } else {
                    objResponseCallback.success((List<NeighborhoodDTO>) objResponse);
                }
            }
        }.execute();
    }

}
