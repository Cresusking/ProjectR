package com.example.cresu.projectr.sqlites;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.cresu.projectr.constantes.BDDConstantes;

/**
 * Created by cresu on 24/03/2018.
 */

public class MaBase extends SQLiteOpenHelper {

    public MaBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(BDDConstantes.CREATE_TABLE_ALIMENT);
        db.execSQL(BDDConstantes.CREATE_TABLE_CATEGORIE_ALIMENT);
        db.execSQL(BDDConstantes.CREATE_TABLE_PANIER_WALLET);
        db.execSQL(BDDConstantes.CREATE_TABLE_RECEIVER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(BDDConstantes.DROP_TABLE_ALIMENT);
        db.execSQL(BDDConstantes.DROP_TABLE_CATEGORIE_ALIMENT);
        db.execSQL(BDDConstantes.DROP_TABLE_PANIER_WALLET);
        db.execSQL(BDDConstantes.DROP_TABLE_RECEIVER);

        onCreate(db);
    }
}
