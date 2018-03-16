package com.andima.gestordeapps;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by Andima on 15/03/2018.
 */

public class DialogCerrarSesion extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.menu_cerrar_sesion);
        builder.setMessage(R.string.mensaje_dialog_cerrar_sesion);
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                /*Se borra el usuario del fichero de preferencias para que al iniciar
                la app la próxima vez se necesario iniciar sesión de nuevo y se cierra la app.*/
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
