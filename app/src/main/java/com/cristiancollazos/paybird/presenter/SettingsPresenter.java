package com.cristiancollazos.paybird.presenter;

import com.cristiancollazos.paybird.misc.Constants;
import com.cristiancollazos.paybird.repository.LocalRepository;
import com.cristiancollazos.paybird.repository.dto.SettingDTO;
import com.cristiancollazos.paybird.repository.impl.LocalRepositoryImpl;

public class SettingsPresenter {

    public interface View {
        void notifySaveSettingSuccess();

        void notifySaveSettingFailure();

        void showMessageError(String sbMessage);

        void showProgress();

        void hideProgress();

        void renderSettings(String sbIp, String sbPort, String sbPrinterName,
                            String sbPrinterMac);
    }

    private View objView;
    private LocalRepository objLocalRepository;

    public SettingsPresenter(View objView) {
        this.objView = objView;
        this.objLocalRepository = new LocalRepositoryImpl();
    }

    public void getSavedSettings() {
        objView.showProgress();
        SettingDTO objSettingsDTO = objLocalRepository.getSingleData(
                Constants.LOCALKEY_SETTINGS, SettingDTO.class);

        if (objSettingsDTO != null) {
            objView.renderSettings(objSettingsDTO.getSbIp(), objSettingsDTO.getSbPort(),
                    objSettingsDTO.getSbPrinterName(), objSettingsDTO.getSbPrinterMac());
        } else {
            objView.renderSettings(Constants.DEFAULT_IP, Constants.DEFAULT_PORT, null,
                    null);
        }
        objView.hideProgress();
    }

    public void doSaveSettings(String sbIp, String sbPort, String sbPrinterName,
                               String sbPrinterMac) {
        objView.showProgress();

        try {
            SettingDTO objSettingsDTO = new SettingDTO(sbIp, sbPort, sbPrinterName, sbPrinterMac);
            if (objLocalRepository.setSingleData(Constants.LOCALKEY_SETTINGS,
                    objSettingsDTO, SettingDTO.class)) {

                objView.notifySaveSettingSuccess();
                objView.hideProgress();
            } else {
                objView.notifySaveSettingFailure();
                objView.hideProgress();
            }
        } catch (Exception objException) {
            objView.showMessageError(objException.getMessage());
            objView.hideProgress();
        }
    }

}
