package com.example.cresu.projectr.sqlites;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.cresu.projectr.constantes.BDDConstantes;
import com.example.cresu.projectr.modeles.Aliment;

import java.util.ArrayList;

/**
 * Created by cresu on 24/03/2018.
 */

public class AlimentDAO {

    // Declaration d'une base de données SQLiteDatabase
    private SQLiteDatabase bdd;

    // Déclaration de la Classe MaBase
    private MaBase maBase;

    public AlimentDAO(Context context){

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
    public long ajouterUnAliment(Aliment aliment){
        ContentValues values =  new ContentValues();
        values.put(BDDConstantes.ALIMENT_NOM, aliment.getNom());
        values.put(BDDConstantes.ALIMENT_CATEGORIE, aliment.getCategorie());
        values.put(BDDConstantes.ALIMENT_IMAGE_ID, aliment.getImage_id());
        values.put(BDDConstantes.ALIMENT_PRIX, aliment.getPrix());
        values.put(BDDConstantes.ALIMENT_PAS_DE_PRIX, aliment.getPas_de_prix());

        return bdd.insert(BDDConstantes.TABLE_ALIMENT, null, values);
    }

    // Méthode de suppression
    public int supprimerUnAliment(int id){
        return bdd.delete(BDDConstantes.TABLE_ALIMENT, BDDConstantes.ALIMENT_ID +" = "+id, null);
    }

    // Méthode de recherche avec la categorie des aliments
    public ArrayList<Aliment> getAliments(int categorie){
        Cursor c = bdd.query(BDDConstantes.TABLE_ALIMENT,
                new String[]{BDDConstantes.ALIMENT_ID,
                        BDDConstantes.ALIMENT_NOM,
                        BDDConstantes.ALIMENT_CATEGORIE,
                        BDDConstantes.ALIMENT_IMAGE_ID,
                        BDDConstantes.ALIMENT_PRIX,
                        BDDConstantes.ALIMENT_PAS_DE_PRIX},
                BDDConstantes.ALIMENT_CATEGORIE+" = "+categorie, null, null, null, null);

        return cursorToAliments(c);
    }

    // Méthode de recherche avec l'identifiant d'un aliment
    public Aliment getAliment(int id){
        Cursor c = bdd.query(BDDConstantes.TABLE_ALIMENT,
                new String[]{BDDConstantes.ALIMENT_ID,
                        BDDConstantes.ALIMENT_NOM,
                        BDDConstantes.ALIMENT_CATEGORIE,
                        BDDConstantes.ALIMENT_IMAGE_ID,
                        BDDConstantes.ALIMENT_PRIX,
                        BDDConstantes.ALIMENT_PAS_DE_PRIX},
                BDDConstantes.ALIMENT_ID+" = "+id, null, null, null, null);

        return cursorToAliment(c);
    }

    // Méthode de lecture d'un curseur
    private Aliment cursorToAliment(Cursor c){

        if(c.getCount() == 0)
            return null;

        c.moveToFirst();

        Aliment aliment = new Aliment();
        aliment.setId(c.getInt(BDDConstantes.ALIMENT_ID_ID));
        aliment.setNom(c.getString(BDDConstantes.ALIMENT_NOM_ID));
        aliment.setCategorie(c.getInt(BDDConstantes.ALIMENT_CATEGORIE_ID));
        aliment.setImage_id(c.getInt(BDDConstantes.ALIMENT_IMAGE_ID_ID));
        aliment.setPrix(c.getInt(BDDConstantes.ALIMENT_PRIX_ID));
        aliment.setPas_de_prix(c.getInt(BDDConstantes.ALIMENT_PAS_DE_PRIX_ID));


        // On ferme le curseur
        c.close();

        // On retourne les informations des aliments trouvé
        return aliment;
    }

    // Méthode de lecture d'un curseur
    private ArrayList<Aliment> cursorToAliments(Cursor c){

        if(c.getCount() == 0)
            return null;

        ArrayList<Aliment> retours = new ArrayList<>(c.getCount());
        c.moveToFirst();

        do{
            Aliment aliment = new Aliment();
            aliment.setId(c.getInt(BDDConstantes.ALIMENT_ID_ID));
            aliment.setNom(c.getString(BDDConstantes.ALIMENT_NOM_ID));
            aliment.setCategorie(c.getInt(BDDConstantes.ALIMENT_CATEGORIE_ID));
            aliment.setImage_id(c.getInt(BDDConstantes.ALIMENT_IMAGE_ID_ID));
            aliment.setPrix(c.getInt(BDDConstantes.ALIMENT_PRIX_ID));
            aliment.setPas_de_prix(c.getInt(BDDConstantes.ALIMENT_PAS_DE_PRIX_ID));

            retours.add(aliment);
        }while(c.moveToNext());

        // On ferme le curseur
        c.close();

        // On retourne les informations des aliments trouvé
        return retours;
    }
}
