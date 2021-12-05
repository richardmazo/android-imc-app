package com.rilel.imc_app.pojo;

public class Autorizacion {

    public static void setAccess(String access) {
        Autorizacion.access = access;
    }

    private static String access;

    public static String getAccess() {
        return access;
    }


}
