package com.cristiancollazos.paybird.misc;

import java.util.ArrayList;
import java.util.List;

public class Constants {

    public static final String APP_NAME = "PayBird";
    public static final String DEFAULT_IP = "159.203.28.56";
    public static final String DEFAULT_PORT = "5253";
    public static String LOCAL_SHAREDPREFERENCES = "com.cristiancollazos.paybird";
    public static int BASEACTIVITY_HOMEBUTTON_ID = -1;
    public static List<String> FRAGMENTS_CREATED = new ArrayList<>();

    public static String LOCALKEY_SETTINGS = "SETTINGS_LOCALKEY";
    public static String LOCALKEY_USERDATA = "USERDATA_LOCALKEY";
    public static String LOCALKEY_ROUTEDATA = "ROUTEDATA_LOCALKEY";
    public static String LOCALKEY_CREATEALARMSINDICATOR = "CREATEALARMSINDICATOR_LOCALKEY";
    public static String LOCALKEY_CUSTOMER = "CUSTOMER_LOCALKEY";

    public static final String SEPARADOR_GUION = new String("-");
    public static final String SEPARADOR_SERIE_MANUAL = new String("_");
    public static final String SEPARADOR_CAMPO = new String("|");
    public static final String SEPARADOR_REGISTRO = new String("&");
    public static final String SEPARADOR_LINEA = new String("@");
    public static final String SEPARADOR_LOTERIAS = new String(",");
    public static final String SEPARADOR_VERIFICACION_DOBLE_CHANCE = new String(";");
    public static final String SEPARADOR_APUESTAS = new String("___");
    public static final String SEPARADOR_VENTA_FUERA_LINEA_MOVIL = new String("##");
    public static final String SEPARADOR_NUMERAL = new String("#");
    public static final String SEPARADOR_PORCENTAJE = new String("%");

    public static final int INT_NULL = -999999;

    public static final String sbTRUE = new String("T");

    public static final Integer NO_DATA_FOUND = 170;

    public static final Integer TIEMPOPAGO_DIARIO = 1;
    public static final Integer TIEMPOPAGO_SEMANAL = 2;
    public static final Integer TIEMPOPAGO_QUINCENAL = 3;
    public static final Integer TIEMPOPAGO_MENSUAL = 4;

    public static final int WIDTH_IMG_HEADER_PRINT = 370;

}
