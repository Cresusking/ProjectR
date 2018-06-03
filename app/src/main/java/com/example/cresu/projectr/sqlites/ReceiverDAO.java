package com.example.cresu.projectr.sqlites;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.cresu.projectr.constantes.BDDConstantes;
import com.example.cresu.projectr.modeles.Receiver;

import java.util.ArrayList;

/**
 * Created by cresu on 08/05/2018.
 */

public class ReceiverDAO {

    // Declaration d'une base de données SQLiteDatabase
    private SQLiteDatabase bdd;

    // Déclaration de la Classe MaBase
    private MaBase maBase;

    public ReceiverDAO(Context context){

        maBase = new MaBase(context, BDDConstantes.BD_NOM, null, BDDConstantes.VERSION);
    }

    // Méthode d'ouverture en mode lecture de la base de données
    public void openR(){
        bdd = maBase.getReadableDatabase();
    }

    // Méthode d'ouverture en mode écriture de la base de données
    public void openW(){
        bdd = maBase.getWritableDatabase();
    }

    // Méthode de fermeture de la base de données
    public void close(){
        bdd.close();
    }

    // Méthode d'Ajout
    public long ajouterUnReceiver(Receiver receiver){
        ContentValues values =  new ContentValues();
        values.put(BDDConstantes.RECEIVER_ID_RECEIVER, receiver.getId_receiver());
        values.put(BDDConstantes.RECEIVER_NOM, receiver.getNom());

        return bdd.insert(BDDConstantes.TABLE_RECEIVER, null, values);
    }

    // Méthode de suppression
    public int supprimerUnReceiver(int id){
        return bdd.delete(BDDConstantes.TABLE_RECEIVER, BDDConstantes.RECEIVER_ID +" = "+id, null);
    }

    // Méthode de recherche de tous les receivers
    public ArrayList<Receiver> getReceivers(){
        Cursor c = bdd.query(BDDConstantes.TABLE_RECEIVER,
                new String[]{BDDConstantes.RECEIVER_ID,
                        BDDConstantes.RECEIVER_ID_RECEIVER,
                        BDDConstantes.RECEIVER_NOM},
                null, null, null, null, null);

        return cursorToReceivers(c);
    }

    // Méthode de lecture d'un curseur
    private ArrayList<Receiver> cursorToReceivers(Cursor c){

        if(c.getCount() == 0)
            return null;

        ArrayList<Receiver> retours = new ArrayList<>(c.getCount());
        c.moveToFirst();

        do{
            Receiver receiver = new Receiver();
            receiver.setId(c.getInt(BDDConstantes.RECEIVER_ID_ID));
            receiver.setId_receiver(c.getString(BDDConstantes.RECEIVER_ID_RECEIVER_ID));
            receiver.setNom(c.getString(BDDConstantes.RECEIVER_ID_NOM_ID));

            retours.add(receiver);
        }while(c.moveToNext());

        // On ferme le curseur
        c.close();

        // On retourne les informations des aliments trouvé
        return retours;
    }
}
