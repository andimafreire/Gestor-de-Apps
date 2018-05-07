package com.andima.gestordeapps;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Andima on 15/03/2018.
 */

public class AppInfoActivity extends AbstractActivity {

    public static App app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(app.getNombre());
        setContentView(R.layout.activity_app_info);
        cargarEnlayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //si se ha cerrado sesión o se ha eliminado la cuenta la actividad termina
        if(VariablesGlobales.getVariablesGlobales().getEmail()==null)
            finish();
        /* si el tema ha cambiado se relanza la actividad para aplicarlo,
         * la variable a utilizar es distinta debido a que esta actividad y ListaAppsActivity
         * pueden convivir en la pila a diferencia que LoginActivity que es cerrada*/
        if (VariablesGlobales.getVariablesGlobales().isTemaCambiadoInfo()) {
            VariablesGlobales.getVariablesGlobales().setTemaCambiadoInfo(false);
            recreate();
        }
    }

    private void cargarEnlayout() {
        //si la app está instalada se carga el icono y se visibilizan los botones
        if (app.isInstalada()){
            ((ImageView) this.findViewById(R.id.iconoInfo)).setImageDrawable(app.getIcono());
            this.findViewById(R.id.ejecutarInfo).setVisibility(View.VISIBLE);
            this.findViewById(R.id.desinstalarInfo).setVisibility(View.VISIBLE);
            this.findViewById(R.id.ajustesInfo).setVisibility(View.VISIBLE);
        }
        /*si la app es esta app se oculta el botón ejecutar para impedir cargar la
        * pila con mas actividades de esta app*/
        if (app.getPaquete().equals(getPackageName()))
             this.findViewById(R.id.ejecutarInfo).setVisibility(View.INVISIBLE);
        ((TextView) this.findViewById(R.id.nombreInfo)).setText(app.getNombre());
        ((TextView) this.findViewById(R.id.paqueteInfo)).setText(app.getPaquete());
    }
    /*botón de la vista que borra la app de la tabla relación app-usuario y, si esta instalada,
    * la desinstala*/
    public void borrarApp(View view){
        BaseDeDatos miBD = new BaseDeDatos(this);
        SQLiteDatabase db = miBD.getWritableDatabase();
        db.delete("AppUsuario","paquete='"+app.getPaquete()+"' and email='"+
                VariablesGlobales.getVariablesGlobales().getEmail()+"'",null);
        db.close();
        miBD.close();
        if (app.isInstalada()) {
            desinstalarApp(view);
        }
        finish();
    }

    public void ejecutarApp(View view){
        try {
            startActivity(getPackageManager().getLaunchIntentForPackage(app.getPaquete()));
        } catch (ActivityNotFoundException e) {
            Log.i("ejecutar",e.getMessage());
        }
    }

    public void desinstalarApp(View view){
        startActivity(new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + app.getPaquete())));
        finish();
    }

    public void ajustesApp(View view){
        try {
            /*se abre la app de ajustes por defecto del dispositivo con la información de la
            * app correspondiente*/
            startActivity(new Intent("android.settings.APPLICATION_DETAILS_SETTINGS",
                    Uri.parse("package:" + app.getPaquete())));
        } catch (ActivityNotFoundException e) {
            Log.i("ajustes",e.getMessage());
        }
    }
    public void abrirEnPlayApp(View view){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + app.getPaquete())));
    }
}
