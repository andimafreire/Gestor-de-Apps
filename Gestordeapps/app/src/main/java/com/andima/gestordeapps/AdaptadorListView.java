package com.andima.gestordeapps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Andima on 14/03/2018.
 */

public class AdaptadorListView extends BaseAdapter {

    private LayoutInflater inflater;
    private List<App> apps;

    public AdaptadorListView(Context pcontext, List<App> pApps) {
        apps = pApps;
        inflater = (LayoutInflater) pcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return apps.size();
    }

    @Override
    public Object getItem(int i) {
        return apps.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.fila_lista_apps,null);
        TextView nombre= view.findViewById(R.id.nombre);
        ImageView icono= view.findViewById(R.id.icono);
        if(apps.get(i).getIcono()!=null) icono.setImageDrawable(apps.get(i).getIcono());
        nombre.setText(apps.get(i).getNombre());
        if (apps.get(i).isInstalada()){
            TextView instalada= view.findViewById(R.id.instalada);
            instalada.setText(R.string.app_instalada);
        }
        return view;
    }
}
