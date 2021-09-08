package com.cristiancollazos.paybird.repository.dto;

import com.google.gson.annotations.SerializedName;

public class SettingDTO {

    @SerializedName("ip")
    private String sbIp;

    @SerializedName("port")
    private String sbPort;

    @SerializedName("printer_name")
    private String sbPrinterName;

    @SerializedName("printer_mac")
    private String sbPrinterMac;

    public SettingDTO(String sbIp, String sbPort, String sbPrinterName, String sbPrinterMac) {
        this.sbIp = sbIp;
        this.sbPort = sbPort;
        this.sbPrinterName = sbPrinterName;
        this.sbPrinterMac = sbPrinterMac;
    }

    public SettingDTO() {
    }

    public String getSbIp() {
        return sbIp;
    }

    public void setSbIp(String sbIp) {
        this.sbIp = sbIp;
    }

    public String getSbPort() {
        return sbPort;
    }

    public void setSbPort(String sbPort) {
        this.sbPort = sbPort;
    }

    public String getSbPrinterName() {
        return sbPrinterName;
    }

    public void setSbPrinterName(String sbPrinterName) {
        this.sbPrinterName = sbPrinterName;
    }

    public String getSbPrinterMac() {
        return sbPrinterMac;
    }

    public void setSbPrinterMac(String sbPrinterMac) {
        this.sbPrinterMac = sbPrinterMac;
    }

}
