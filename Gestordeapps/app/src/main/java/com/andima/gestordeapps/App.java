package com.andima.gestordeapps;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * Created by Andima on 14/03/2018.
 */

public class App implements Comparable<App>{

    private String paquete;
    private String nombre;
    private Drawable icono;
    private boolean instalada;

    //Constructora para apps instaladas
    public App(PackageInfo pInfoDeApp, PackageManager pPM) {
        nombre = pPM.getApplicationLabel(pInfoDeApp.applicationInfo).toString();
        instalada = true;
        paquete = pInfoDeApp.packageName;
        icono = pPM.getApplicationIcon(pInfoDeApp.applicationInfo);
    }
    //constructora para apps guardadas en la base de datos que no están instaladas
    public App(String pPaquete, String pNombre) {
        paquete = pPaquete;
        nombre = pNombre;
    }

    public String getNombre() {
        return nombre;
    }
    public String getPaquete() {
        return paquete;
    }
    public Drawable getIcono() {
        return icono;
    }
    public boolean isInstalada() {
        return instalada;
    }

    @Override
    //compareTo de la interfaz Comparable, utilizado para ordenar las apps por nombre
    public int compareTo(@NonNull App pApp) {
        return getNombre().compareToIgnoreCase(pApp.getNombre());
    }


}