package com.andima.gestordeapps;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Andima on 05/03/2018.
 */

public class BaseDeDatos extends SQLiteOpenHelper {

    private static final String nombreBD = "AdminApps.db";
    private static final int BD_VERSION = 1;

    public BaseDeDatos(Context context) {super(context, nombreBD, null, BD_VERSION);
    }

    @Override
    public void
    onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Usuarios ('email' TEXT PRIMARY KEY, 'password' TEXT)");

        db.execSQL("CREATE TABLE AppUsuario ('email' TEXT, 'paquete' TEXT," +
                " FOREIGN KEY(email) REFERENCES Usuarios(email)," +
                "FOREIGN KEY(paquete) REFERENCES App(paquete)," +
                "PRIMARY KEY(email,paquete))");

        db.execSQL("CREATE TABLE Apps ('paquete' TEXT PRIMARY KEY," +
                "'nombre' TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnt, int versionNue) {
        // Eliminar, si existen, las tablas
        db.execSQL("DROP TABLE IF EXISTS AppUsuario");
        db.execSQL("DROP TABLE IF EXISTS Apps");
        db.execSQL("DROP TABLE IF EXISTS Usuarios");
        // Crearla de nuevo
        onCreate(db);
    }
}
