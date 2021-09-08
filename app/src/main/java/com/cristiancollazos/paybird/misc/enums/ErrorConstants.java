package com.cristiancollazos.paybird.misc.enums;

public enum ErrorConstants {

    E0000(0, "Error no definido", "Comuniquese con el Administrador"),
    E0001(1, "Error en la IP / PUERTO guardados", "Verifique la información en los ajustes"),
    E0002(2, "No se pudo establecer la conexión", "Verifique si tiene acceso a la red o " +
            "comuniquese con el Administrador"),
    EOO03(3, "Hay uno o más campos sin diligenciar", "Verifique la información suministrada"),
    E0004(4, "No se pudo realizar la operación", "Comuniquese con el Administrador"),
    E0005(5, "No se pudo crear el cliente", "Ya existe en base de datos"),
    E0006(6, "No se pudo editar el cliente", "Comuniquese con el Administrador"),
    E0007(7, "No se pudo registrar el abono", "Comuniquese con el Administrador"),
    E0008(8, "No se pudo anular el movimiento", "Comuniquese con el Administrador"),
    E0009(9, "No se puede anular el desembolso", "Para anularlo, deberá primero anular los abonos de este"),
    E0010(10, "No se pudo imprimir el comprobante", ""),
    E0011(11, "No se pudo cambiar el orden de la ruta", "Comuniquese con el Administrador"),
    E0012(12, "No se pudo almacenar la información del cliente seleccionado", "Comuniquese con el Administrador"),
    E0013(13, "El saldo no puede ser menor al valor del crédito", "Inténtelo nuevamente"),
    E0014(14, "No se puede continuar", "La lista de periodos no fue cargada"),
    E0015(15, "No se puede continuar", "El valor del crédito debe ser mayor a cero"),
    E0016(16, "No se encontró usuario logueado", "Comuniquese con el Administrador"),
    E0017(17, "No se pudo crear el crédito", "Comuniquese con el Administrador");

    private String sbDef;
    private String sbRecommend;
    private int nuCode;

    ErrorConstants(int nuCode, String sbDef, String sbRecommend) {
        this.nuCode = nuCode;
        this.sbDef = sbDef;
        this.sbRecommend = sbRecommend;
    }

    public String def() {
        return sbDef;
    }

    public int code() {
        return nuCode;
    }

    public String recommend() {
        return sbRecommend;
    }

}
