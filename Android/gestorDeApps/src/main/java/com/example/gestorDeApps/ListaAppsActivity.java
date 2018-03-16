package com.example.gestorDeApps;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by Andima on 14/03/2018.
 */

public class ListaAppsActivity extends AbstractActivity{

    private List<App> apps;
    private ListView listaApps;
    private AdaptadorListView eladap;
    private CargarAppsAsyncTask tarea;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_apps);
        listaApps = (ListView) findViewById(R.id.lista);
        listaApps.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                try{
                    /*se guarda la app seleccionada de la lista en una variable final de la
                    * actividad que posteriormente se lanza*/
                    App app = (App) parent.getItemAtPosition(position);
                    AppInfoActivity.app = app;
                    Log.d("seleccionada", app.getNombre());
                    startActivity(new Intent(getBaseContext(), AppInfoActivity.class));
                }catch (Exception e){
                    Log.d("seleccionada", e.getMessage());
                    recreate();
                }
            }
        });
        /*si la aplicación sufre un reinicio forzado reabre la actividad sin pasar por la actividad
        * principal*/
        if (VariablesGlobales.getVariablesGlobales().getEmail()==null) {
            VariablesGlobales.getVariablesGlobales().setEmail(PreferenceManager
                    .getDefaultSharedPreferences(this).getString("USUARIO", null));
        }
        //se establece el titulo de la aplicación con el email de registro del usuario
        setTitle(getResources().getString(R.string.titulo_lista_apps)+" "
                +VariablesGlobales.getVariablesGlobales().getEmail());
    }

    @Override
    protected void onResume() {
        super.onResume();
        //si se ha cerrado sesión o se ha eliminado la cuenta la actividad termina
        if(VariablesGlobales.getVariablesGlobales().getEmail()==null)
            finish();
        /* si el tema ha sido cambiado se marca en la clase singleton que se ha actualizado y
        * se relanza la actividad para aplicarlo*/
        if (VariablesGlobales.getVariablesGlobales().isTemaCambiado()){
            VariablesGlobales.getVariablesGlobales().setTemaCambiado(false);
            //si esta Actividad está abierta AppInfoActivity no se encuentra en la pila
            VariablesGlobales.getVariablesGlobales().setTemaCambiadoInfo(false);
            recreate();
        }
        //se recarga la lista de apps por si hubiera cambios.
        tarea = new CargarAppsAsyncTask(this);
        tarea.setIncluirInstaladas(PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("INCLUIR_INSTALADAS",false));
        tarea.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        /*se pone a true la visibilidad de los elementos del menú
        * que deben mostrarse en esta actividad*/
        menu.findItem(R.id.filtroInstaladas).setVisible(true);
        menu.findItem(R.id.filtrar).setVisible(true);
        /*se marca la casilla del elemento del menú con el valor
        * seleccionado(por defecto false(no marcada))*/
        menu.findItem(R.id.filtroInstaladas).setChecked(PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean("INCLUIR_INSTALADAS",false));
        return result;
    }

    private void cargarListView(){
        eladap = new AdaptadorListView(this, apps);
        listaApps.setAdapter(eladap);
    }

    //carga la lista de apps procesada en CargarAppsAsyncTask
    private void cargarApps(List<App> pApps){
        apps = pApps;
        cargarListView();
    }

    private class CargarAppsAsyncTask extends AsyncTask<String, Void, List<App>> {

        private boolean incluirInstaladas;
        private ListaAppsActivity activity;
        private BaseDeDatos miBD;
        private SQLiteDatabase db;

        private CargarAppsAsyncTask(ListaAppsActivity pActivity) {
            activity = pActivity;
        }

        @Override
        /*si la lista de apps procesadas esta vacía se carga el layout correspondiente
        * de lo contrario se ordena por nombre (el compareTo de la clase App se ha definido así)
        * y se llama al método cargarApps de la actividad*/
        protected void onPostExecute(List<App> pApps) {
            if(!pApps.isEmpty()){
                Collections.sort(pApps);
                activity.cargarApps(pApps);
            }else{
                activity.setContentView(R.layout.lista_apps_vacia);
            }
        }
        @Override
        protected List<App> doInBackground(String... strings) {
            Map<String, App> appsAIntrod = new HashMap<>();
            List<App> appsFinal = new ArrayList<>();
            PackageManager pm = activity.getPackageManager();

            //se recorren todas las apps del dispositivo
            for (PackageInfo infoApp : pm.getInstalledPackages(0)) {
                /*si la app tiene información, está habilitada y no es del sistema creamos la
                * instancia de App correspondiente*/
                if (null != infoApp.applicationInfo && infoApp.applicationInfo.enabled
                        && (infoApp.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0){
                    App app = new App(infoApp,pm);
                    /*se carga la app en el HashMap definido y si esta activada la opción
                    * incluir instaladas se añaden a la lista final*/
                    if(incluirInstaladas)
                        appsFinal.add(app);
                    appsAIntrod.put(app.getPaquete(),app);
                }
            }

            miBD = new BaseDeDatos(activity);
            db = miBD.getWritableDatabase();
            /*se recuperan todas las tuplas de la tabla Apps que esten relacionadas con el usuario
            * mediante la tabla AppUsuario*/
            Cursor c = db.rawQuery("SELECT * FROM Apps NATURAL JOIN AppUsuario WHERE email='"+
                    VariablesGlobales.getVariablesGlobales().getEmail()+ "'",null);
            //mientras haya tupla
            while (c.moveToNext()){
                /*si no esta instalada se añade a la lista final, de lo contrario se elimina del
                * HashMap para acabar obteniendo una lista de apps que no están en la base de datos*/
                if(!appsAIntrod.containsKey(c.getString(0))) {
                    Log.d("appBD", c.getString(0) + " " + c.getString(1) + " " + c.getString(2));
                    App app = new App(c.getString(0), c.getString(1));
                    appsFinal.add(app);
                }else appsAIntrod.remove(c.getString(0));
            }
            c.close();
            /*si la lista de apps instaladas que no están en la base de datos no esta vacía
            * se ejecuta el método que introduce todas las apps en la base de datos */
            if(!appsAIntrod.isEmpty())
                introducirEnBD(appsAIntrod.values());
            db.close();
            miBD.close();
            return appsFinal;
        }

        private void introducirEnBD(Collection<App> appsAIntrod) {
            Cursor c;
            //por cada app de la lista
            for(App app : appsAIntrod){
                c = db.rawQuery("SELECT paquete FROM Apps WHERE paquete='"+app.getPaquete()+
                        "'",null);
                //se comprueba si existía en la base de datos y, si no, se introduce.
                if(!c.moveToNext()) {
                    ContentValues nuevoA = new ContentValues();
                    nuevoA.put("paquete", app.getPaquete());
                    nuevoA.put("nombre", app.getNombre());
                    db.insert("Apps", null, nuevoA);
                }
                //después se introduce la relación app-usuario
                ContentValues nuevoAU = new ContentValues();
                nuevoAU.put("email", VariablesGlobales.getVariablesGlobales().getEmail());
                nuevoAU.put("paquete", app.getPaquete());
                db.insert("AppUsuario", null, nuevoAU);
                c.close();
            }
        }

        private void setIncluirInstaladas(boolean p){
            incluirInstaladas = p;
        }
    }
}