package com.example.gestorDeApps;

/**
 * Created by Andima on 14/03/2018.
 */

public class VariablesGlobales {

    private boolean temaCambiado;
    private boolean temaCambiadoInfo;
    private String email;
    private static VariablesGlobales misVariablesGlobales;

    private VariablesGlobales(){
        temaCambiado = false;
        temaCambiadoInfo = false;
    }

    public static VariablesGlobales getVariablesGlobales(){
        if (misVariablesGlobales==null) misVariablesGlobales = new VariablesGlobales();
        return misVariablesGlobales;
    }
    public boolean isTemaCambiado(){
        return temaCambiado;
    }

    public void setTemaCambiado(boolean pCambiado){
        temaCambiado=pCambiado;
    }

    public void setTemaCambiadoInfo(boolean temaCambiadoInfo) {
        this.temaCambiadoInfo = temaCambiadoInfo;
    }

    public boolean isTemaCambiadoInfo() {
        return temaCambiadoInfo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
