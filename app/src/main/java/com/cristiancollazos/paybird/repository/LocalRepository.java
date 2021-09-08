package com.cristiancollazos.paybird.repository;

import com.cristiancollazos.paybird.misc.exceptions.AppException;
import com.cristiancollazos.paybird.repository.dto.DocumentTypeDTO;

import java.util.List;

public interface LocalRepository {

    <T> T getSingleData(String sbKey,
                    Class<T> objType);

    <T> List<T> getListData(String sbKey,
                        Class<T> objType);

    <T> Boolean setSingleData(String sbKey,
                          T objData,
                          Class<T> objType);

    <T> Boolean setListData(String sbKey,
                        List<T> lstData,
                        Class<T> objType);

    List<DocumentTypeDTO> getDocumentTypes() throws AppException;

}
