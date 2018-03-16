package com.example.gestorDeApps;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Created by Andima on 09/03/2018.
 */

public abstract class AbstractActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Se carga el tema seleccionado (por defecto el claro)
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs.getString("PREFERENCIA_DE_TEMA", "claro").equals("oscuro")) {
            Log.d(getClass().getSimpleName(), "oscuro");
            setTheme(R.style.TemaOscuro);
        }else{
            Log.d(getClass().getSimpleName(), "claro");
            setTheme(R.style.TemaClaro);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("InflateParams")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.eliminarCuenta:
                //se abre el dialogo de confirmación
                new DialogEliminarCuenta().show(getSupportFragmentManager(),"eliminar cuenta");
                break;
            case R.id.cerrarSesion:
                //se abre el dialogo de confirmación
                new DialogCerrarSesion().show(getSupportFragmentManager(),"cerrar sesion");
                break;
            case R.id.ajustes:
                //se inicia la actividad de ajustes
                startActivity(new Intent(this, PreferenciasActivity.class));
                break;
            case R.id.acercaDe:
                //se muestra un dialogo con información de la app
                new AlertDialog.Builder(this)
                        .setTitle(R.string.acerca_de)
                        .setView(getLayoutInflater().inflate(R.layout.dialogo_acercade, null))
                        .setNegativeButton(android.R.string.ok, null)
                        .show();
                break;
            case R.id.filtroInstaladas:
                /*se guarda en el fichero de preferencias el valor booleano contrario al
                * actual y se reinicia la actividad para marcarlo y actuar en consecuencia*/
                PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("INCLUIR_INSTALADAS", !item.isChecked()).apply();
                recreate();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
