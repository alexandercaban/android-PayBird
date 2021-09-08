package com.cristiancollazos.paybird.repository.impl;

import android.content.Context;
import android.content.SharedPreferences;

import com.cristiancollazos.paybird.ApplicationSingleton;
import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.repository.LocalRepository;
import com.cristiancollazos.paybird.misc.Constants;
import com.cristiancollazos.paybird.repository.dto.DocumentTypeDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class LocalRepositoryImpl implements LocalRepository {

    @Override
    public <T> T getSingleData(String sbKey, Class<T> objType) {
        SharedPreferences objSharedPreferences = ApplicationSingleton.getInstance()
                .getSharedPreferences(Constants.LOCAL_SHAREDPREFERENCES, Context.MODE_PRIVATE);

        String sbData = objSharedPreferences.getString(sbKey, "");

        try {
            T objData = new Gson().fromJson(sbData, objType);
            return objData;
        } catch (Exception objException) {
            return null;
        }
    }

    @Override
    public <T> List<T> getListData(String sbKey, Class<T> objType) {
        SharedPreferences objSharedPreferences = ApplicationSingleton.getInstance()
                .getSharedPreferences(Constants.LOCAL_SHAREDPREFERENCES, Context.MODE_PRIVATE);

        String sbData = objSharedPreferences.getString(sbKey, "");

        try {
            List<T> objData = new Gson().fromJson(sbData, new TypeToken<List<T>>(){}.getType());
            return objData;
        } catch (Exception objException) {
            return null;
        }
    }

    @Override
    public <T> Boolean setSingleData(String sbKey, T objData, Class<T> objType) {
        SharedPreferences objSharedPreferences = ApplicationSingleton.getInstance()
                .getSharedPreferences(Constants.LOCAL_SHAREDPREFERENCES, Context.MODE_PRIVATE);

        String sbData = new Gson().toJson(objData, objType);

        try {
            return objSharedPreferences.edit().putString(sbKey, sbData).commit();
        } catch (Exception objException) {
            return false;
        }
    }

    @Override
    public <T> Boolean setListData(String sbKey, List<T> lstData, Class<T> objType) {
        SharedPreferences objSharedPreferences = ApplicationSingleton.getInstance()
                .getSharedPreferences(Constants.LOCAL_SHAREDPREFERENCES, Context.MODE_PRIVATE);

        String sbData = new Gson().toJson(lstData, new TypeToken<List<T>>(){}.getType());

        try {
            return objSharedPreferences.edit().putString(sbKey, sbData).commit();
        } catch (Exception objException) {
            return false;
        }
    }

    @Override
    public List<DocumentTypeDTO> getDocumentTypes() throws AppException {
        List<DocumentTypeDTO> lstDocumentTypes = new ArrayList<>();
        lstDocumentTypes.add(new DocumentTypeDTO("CC","C. Ciudadanía"));
        lstDocumentTypes.add(new DocumentTypeDTO("NIT","Nit Comercial"));
        return lstDocumentTypes;
    }

}
