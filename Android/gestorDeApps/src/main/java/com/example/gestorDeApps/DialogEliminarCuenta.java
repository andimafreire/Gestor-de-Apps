package com.example.gestorDeApps;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by Andima on 16/03/2018.
 */

public class DialogEliminarCuenta extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.menu_eliminar_cuenta);
        builder.setMessage(R.string.mensaje_dialog_eliminar_cuenta);
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                /*Se borra el usuario de la base de datos y del fichero de preferencias para que al
                 * iniciar la app la próxima vez se necesario iniciar sesión de nuevo y se cierra la app.*/
                BaseDeDatos miBD = new BaseDeDatos(getActivity());
                SQLiteDatabase db = miBD.getWritableDatabase();
                db.delete("AppUsuario","email='"+
                        VariablesGlobales.getVariablesGlobales().getEmail()+"'",null);
                db.delete("Usuarios","email='"+
                        VariablesGlobales.getVariablesGlobales().getEmail()+"'",null);
                db.close();
                miBD.close();
                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("USUARIO", null).apply();
                VariablesGlobales.getVariablesGlobales().setEmail(null);
                getActivity().finish();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onCancel(dialogInterface);
            }
        });
        return builder.create();
    }
}
