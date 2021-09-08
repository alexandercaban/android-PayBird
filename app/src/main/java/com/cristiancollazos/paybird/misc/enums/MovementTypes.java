package com.cristiancollazos.paybird.misc.enums;

public enum MovementTypes {

    ABONO("S", "Abono"),
    DESEMBOLSO("D", "Desembolso"),
    GASTO("1", "Gastos"),
    ENTREGA_ADMINISTRADOR("2", "Entrada Efectivo"),
    RECIBE_ADMINISTRADOR("3", "Salida Efectivo"),
    ENTREGA_SOCIO("4", "Salida a Socio"),
    VALE_COBRADOR("5", "Vale"),
    VALE_AYUDANTE("6", "Vale a ayudante"),
    PAGO_COBRADOR("7", "Pago vale"),
    PAGO_AYUDANTE("8", "Pago a ayudante"),
    PAGO_SUELDO("9", "Sueldo"),
    SOBRANTE("S", "Sobrante"),
    ABONO_CREDITOSIMPLE("N", "Abono a credito");

    private String sbId;
    private String sbDescription;

    MovementTypes(String sbId, String sbDescription) {
        this.sbId = sbId;
        this.sbDescription = sbDescription;
    }

    public String getSbId() {
        return sbId;
    }

    public String getSbDescription() {
        return sbDescription;
    }

}