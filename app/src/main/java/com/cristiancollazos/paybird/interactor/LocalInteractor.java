package com.cristiancollazos.paybird.interactor;

import com.cristiancollazos.paybird.interactor.callback.ResponseCallback;
import com.cristiancollazos.paybird.repository.dto.DocumentTypeDTO;

import java.util.List;

public interface LocalInteractor {

    void getDocumentTypes(ResponseCallback<List<DocumentTypeDTO>> objResponseCallback);

}
