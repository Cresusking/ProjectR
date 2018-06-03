package com.example.cresu.projectr.sqlites;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.cresu.projectr.constantes.BDDConstantes;
import com.example.cresu.projectr.modeles.CategorieAliment;

import java.util.ArrayList;

/**
 * Created by cresu on 27/03/2018.
 */

public class CategorieAlimentDAO {

    // Declaration d'une base de données SQLiteDatabase
    private SQLiteDatabase bdd;

    // Déclaration de la Classe MaBase
    private MaBase maBase;

    public CategorieAlimentDAO(Context context){

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
    public long ajouterUneCategorieAliment(CategorieAliment categorieAliment){
        ContentValues values =  new ContentValues();
        values.put(BDDConstantes.CATEGORIE_ALIMENT_NOM, categorieAliment.getNom());
        values.put(BDDConstantes.CATEGORIE_ALIMENT_IMAGE_ID, categorieAliment.getImage_id());

        return bdd.insert(BDDConstantes.TABLE_CATEGORIE_ALIMENT, null, values);
    }

    // Méthode de suppression
    public int supprimerUneCategorieAliment(int id){
        return bdd.delete(BDDConstantes.TABLE_CATEGORIE_ALIMENT, BDDConstantes.CATEGORIE_ALIMENT_ID +" = "+id, null);
    }

    // Méthode de recherche avec la categorie des aliments
    public ArrayList<CategorieAliment> getCategorieAliments(){
        Cursor c = bdd.query(BDDConstantes.TABLE_CATEGORIE_ALIMENT,
                new String[]{BDDConstantes.CATEGORIE_ALIMENT_ID,
                        BDDConstantes.CATEGORIE_ALIMENT_NOM,
                        BDDConstantes.CATEGORIE_ALIMENT_IMAGE_ID},
                null, null, null, null, null);

        return cursorToCategorieAliments(c);
    }

    // Méthode de lecture d'un curseur
    private ArrayList<CategorieAliment> cursorToCategorieAliments(Cursor c){

        if(c.getCount() == 0)
            return null;

        ArrayList<CategorieAliment> retours = new ArrayList<>(c.getCount());
        c.moveToFirst();

        do{
            CategorieAliment categorieAliment = new CategorieAliment();
            categorieAliment.setId(c.getInt(BDDConstantes.CATEGORIE_ALIMENT_ID_ID));
            categorieAliment.setNom(c.getString(BDDConstantes.CATEGORIE_ALIMENT_NOM_ID));
            categorieAliment.setImage_id(c.getInt(BDDConstantes.CATEGORIE_ALIMENT_IMAGE_ID_ID));

            retours.add(categorieAliment);
        }while(c.moveToNext());

        // On ferme le curseur
        c.close();

        // On retourne les informations des aliments trouvé
        return retours;
    }
}
