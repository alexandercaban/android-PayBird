package com.cristiancollazos.paybird.misc;

import com.cristiancollazos.paybird.misc.exceptions.AppException;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Parser {
    private String sbStringToParser;
    private String sbToken;
    private boolean boHasMoreTokens;
    private String sbStringBuffer;

    /**
     * @param sbNewStringToParser
     * @param sbNewToken
     */
    public Parser(String sbNewStringToParser, String sbNewToken) {
        this.sbStringToParser = sbNewStringToParser;
        this.sbToken = sbNewToken;

        if (sbNewStringToParser != null) {
            this.sbStringBuffer = new String(sbNewStringToParser);
        }

        boHasMoreTokens = true;
    }

    public void stringTokenizer(String sbNewStringToParser, String sbNewToken)
            throws AppException {
        try {
            this.sbStringToParser = sbNewStringToParser;
            this.sbToken = sbNewToken;
            boHasMoreTokens = true;
        } catch (Exception e) {
            throw new AppException(-1, e.getMessage());
        }
    }


    public String nextString()
            throws AppException {
        try {
            int nuInd;
            String sbResultado;

            nuInd = this.sbStringToParser.indexOf(this.sbToken);
            if (nuInd == -1) {
                sbResultado = this.sbStringToParser;
                this.sbStringToParser = "";
                boHasMoreTokens = false;
                if (sbResultado.equals("")) {
                    return null;
                } else {
                    return sbResultado;
                }
            }
            sbResultado = this.sbStringToParser.substring(0, nuInd);
            this.sbStringToParser = this.sbStringToParser.substring(nuInd + 1);
            if (sbResultado.equals("")) {
                return null;
            } else {
                return sbResultado;
            }
        } catch (Exception e) {
            throw new AppException(-1, e.getMessage());
        }
    }

    public int nextInt()
            throws AppException {
        try {
            String sbResultado;
            int nuResultado;
            sbResultado = this.nextString();
            if (sbResultado == null) {
                nuResultado = Constants.INT_NULL;
            } else {
                nuResultado = Integer.parseInt(sbResultado);
            }
            return nuResultado;
        } catch (Exception e) {
            throw new AppException(-1, e.getMessage());
        }
    }

    public float nextFloat()
            throws AppException {
        try {
            String sbResultado;
            float flResultado;
            sbResultado = this.nextString();
            if (sbResultado == null) {
                flResultado = Constants.INT_NULL;
            } else {
                flResultado = Float.parseFloat(sbResultado);
            }
            return flResultado;
        } catch (Exception e) {
            throw new AppException(-1, e.getMessage());
        }
    }

    public Date nextDate()
            throws AppException {
        try {
            String sbResultado;
            Date dtResultado;
            sbResultado = this.nextString();
            if (sbResultado == null) {
                return null;
            } else {
                dtResultado = getDate(sbResultado);
            }
            Calendar objCalendario = Calendar.getInstance();
            objCalendario.setTime(dtResultado);
            objCalendario.set(Calendar.HOUR_OF_DAY, 0);
            objCalendario.set(Calendar.MINUTE, 0);
            objCalendario.set(Calendar.SECOND, 0);
            objCalendario.set(Calendar.MILLISECOND, 0);

            //return new Date(objCalendario.getTimeZone().getTime());
            return objCalendario.getTime();

        } catch (Exception e) {
            throw new AppException(-1, e.getMessage());
        }
    }

    public boolean nextBoolean()
            throws AppException {
        try {
            String sbResultado;
            boolean boResultado;
            sbResultado = this.nextString();
            boResultado = sbResultado.equals(Constants.sbTRUE);
            return boResultado;
        } catch (Exception e) {
            throw new AppException(-1, e.getMessage());
        }
    }

    public Date nextTime()
            throws AppException {
        try {
            String sbResultado;
            Date tiResultado;
            sbResultado = this.nextString();
            if (sbResultado == null) {
                tiResultado = null;
            } else {
                tiResultado = getTime(sbResultado);
            }

            Calendar objCalendario = Calendar.getInstance();
            objCalendario.setTime(tiResultado);
            objCalendario.set(Calendar.YEAR, 0);
            objCalendario.set(Calendar.MONTH, 0);
            objCalendario.set(Calendar.DAY_OF_MONTH, 0);

            //return new Date(objCalendario.getTimeZone().getTime());
            return objCalendario.getTime();

        } catch (Exception e) {
            throw new AppException(-1, e.getMessage());
        }
    }

    public boolean hasMoreTokens()
            throws AppException {
        try {
            return boHasMoreTokens;
        } catch (Exception e) {
            throw new AppException(-1, e.getMessage());
        }
    }

    static public Date getDate(String sbDate)
            throws AppException {
        try {
            int nuAnno;
            int nuMes;
            int nuDia;
            Date dtResult;
            Parser paDate = new Parser(sbDate, "-");
            nuAnno = paDate.nextInt();
            nuMes = paDate.nextInt();
            nuDia = paDate.nextInt();

            Calendar clFecha = Calendar.getInstance();
            clFecha.set(Calendar.YEAR, nuAnno);
            clFecha.set(Calendar.MONTH, nuMes - 1);
            clFecha.set(Calendar.DAY_OF_MONTH, nuDia);

            clFecha.set(Calendar.HOUR_OF_DAY, 0);
            clFecha.set(Calendar.MINUTE, 0);
            clFecha.set(Calendar.SECOND, 0);
            clFecha.set(Calendar.MILLISECOND, 0);

            // Se modifica para no tener el warning de deprecated
            //dtResult = new Date(nuAnno-1900,nuMes-1,nuDia);   

            //dtResult = new Date(clFecha.getTimeZone().getTimeZone());
            dtResult = clFecha.getTime();

            return dtResult;
        } catch (Exception e) {
            throw new AppException(-1, e.getMessage());
        }
    }

    static public Date getTime(String sbTime)
            throws AppException {
        try {
            int nuHora;
            int nuMinutos;
            int nuSegundos;
            Date tiResult;
            Parser paDate = new Parser(sbTime, ":");
            nuHora = paDate.nextInt();
            nuMinutos = paDate.nextInt();
            nuSegundos = paDate.nextInt();

            Calendar clFecha = Calendar.getInstance();
            clFecha.set(Calendar.YEAR, 0);
            clFecha.set(Calendar.MONTH, 0);
            clFecha.set(Calendar.DAY_OF_MONTH, 0);

            clFecha.set(Calendar.HOUR_OF_DAY, nuHora);
            clFecha.set(Calendar.MINUTE, nuMinutos);
            clFecha.set(Calendar.SECOND, nuSegundos);
            clFecha.set(Calendar.MILLISECOND, 0);

            tiResult = clFecha.getTime();

            return tiResult;
        } catch (Exception e) {
            throw new AppException(-1, e.getMessage());
        }
    }

    static public TimeZone getTime(int nuHora, int nuMinuto, int nuSegundos)
            throws AppException {
        try {

            TimeZone tiResult;
            Calendar clTimeZone = Calendar.getInstance();
            clTimeZone.set(Calendar.HOUR_OF_DAY, nuHora);
            clTimeZone.set(Calendar.MINUTE, nuMinuto);
            clTimeZone.set(Calendar.SECOND, nuSegundos);

            //tiResult =  new TimeZone(clTimeZone.getTimeZone().getTimeZone());
            tiResult = clTimeZone.getTimeZone();

            return tiResult;
        } catch (Exception e) {
            throw new AppException(-1, e.getMessage());
        }
    }

    public static String getBool(boolean blEstado)
            throws AppException {
        try {
            String sbEstado = new String();
            if (blEstado) {
                sbEstado = "T";
            } else {
                sbEstado = "F";
            }
            return sbEstado;
        } catch (Exception e) {
            throw new AppException(-1, e.getMessage());
        }
    }

    public static String getStringNull(String sbBuffer)
            throws AppException {
        try {
            String sbEstado = new String();
            if (sbBuffer == null) {
                sbEstado = "";
            } else {
                if (sbBuffer.equals("null")) {
                    sbEstado = "";
                } else {
                    sbEstado = sbBuffer;
                }
            }
            return sbEstado;
        } catch (Exception e) {
            throw new AppException(-1, e.getMessage());
        }
    }

    /**
     * @return Returns the sbStringBuffer.
     */
    public String getBuffer() {
        return this.sbStringBuffer;
    }

    public String getEndString() {
        return this.sbStringToParser;
    }

    /**
     * @param sbStringBuffer The sbStringBuffer to set.
     */
    public void setBuffer(String sbStringBuffer) {
        this.sbStringBuffer = sbStringBuffer;
    }
}
