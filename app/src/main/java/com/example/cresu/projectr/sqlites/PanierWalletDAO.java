package com.example.cresu.projectr.sqlites;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cresu.projectr.constantes.BDDConstantes;
import com.example.cresu.projectr.modeles.Aliment;
import com.example.cresu.projectr.modeles.PanierWallet;
import com.example.cresu.projectr.modeles.Paquet;
import com.example.cresu.projectr.utils.VariablesGlobales;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cresu on 06/05/2018.
 */

public class PanierWalletDAO {

    // Declaration d'une base de données SQLiteDatabase
    private SQLiteDatabase bdd;

    // Déclaration de la Classe MaBase
    private MaBase maBase;

    private Context context;

    public PanierWalletDAO(Context context){

        maBase = new MaBase(context, BDDConstantes.BD_NOM, null, BDDConstantes.VERSION);

        this.context = context;
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

    // Ajouter un panier dans la base de données de l'utilisateur
    public void ajouterTousLePanier(){

        PanierWallet panierWallet = new PanierWallet();
        panierWallet.setPaquetArrayList(VariablesGlobales.getInstance().getPaniers());

        int id_du_panier = countPanier() + 1;

        Long tsLong = System.currentTimeMillis();

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        Date date = new Date(tsLong);


        for(int i = 0; i < panierWallet.getPaquetArrayList().size(); i++){
            Aliment aliment = new Aliment();

            aliment.setId(panierWallet.getPaquetArrayList().get(i).getAliment().getId());
            aliment.setPrix(panierWallet.getPaquetArrayList().get(i).getAliment().getPrix());

            ajouterUneLigneDePanier(id_du_panier, aliment, format.format(date));
        }

        VariablesGlobales.getInstance().setPaniers(new ArrayList<Paquet>());
        VariablesGlobales.getInstance().setPrix_total(0);


    }

    // Méthode d'Ajout d'une ligne
    public long ajouterUneLigneDePanier(int id_panier, Aliment aliment, String date){
        ContentValues values =  new ContentValues();
        values.put(BDDConstantes.PANIER_WALLET_ID_PANIER, id_panier);
        values.put(BDDConstantes.PANIER_WALLET_ID_ALIMENT, aliment.getId());
        values.put(BDDConstantes.PANIER_WALLET_PRIX, aliment.getPrix());
        values.put(BDDConstantes.PANIER_WALLET_DATE, date);

        return bdd.insert(BDDConstantes.TABLE_PANIER_WALLET, null, values);
    }

    // Méthode de suppression
    public int supprimerUnAliment(int id){
        return bdd.delete(BDDConstantes.TABLE_PANIER_WALLET, BDDConstantes.PANIER_WALLET_ID +" = "+id, null);
    }

    // Méthode de recherche avec l'identifiant d'un aliment
    public int countPanier(){
        Cursor c = bdd.query(true, BDDConstantes.TABLE_PANIER_WALLET,
                new String[]{BDDConstantes.PANIER_WALLET_ID_PANIER},
                null,null, null, null, null, null);

        return c.getCount();
    }

    // Méthode de recherche avec l'identifiant d'un aliment
    public ArrayList<PanierWallet> getPanierWallet(int id){
        Cursor c = bdd.query(BDDConstantes.TABLE_PANIER_WALLET,
                new String[]{BDDConstantes.PANIER_WALLET_ID,
                        BDDConstantes.PANIER_WALLET_ID_PANIER,
                        BDDConstantes.PANIER_WALLET_ID_ALIMENT,
                        BDDConstantes.PANIER_WALLET_PRIX,
                        BDDConstantes.PANIER_WALLET_DATE},
                BDDConstantes.PANIER_WALLET_ID+" = "+id, null, null, null, null);

        return cursorToPaniersWallets(c);
    }

    // Méthode de recherche avec l'identifiant d'un aliment
    public ArrayList<PanierWallet> getPaniersWallets(){
        Cursor c = bdd.query(BDDConstantes.TABLE_PANIER_WALLET,
                new String[]{BDDConstantes.PANIER_WALLET_ID,
                        BDDConstantes.PANIER_WALLET_ID_PANIER,
                        BDDConstantes.PANIER_WALLET_ID_ALIMENT,
                        BDDConstantes.PANIER_WALLET_PRIX,
                        BDDConstantes.PANIER_WALLET_DATE},
                null, null, null, null, null);

        return cursorToPaniersWallets(c);
    }

    // Lecture des données de panier Wallet
    private ArrayList<PanierWallet> cursorToPaniersWallets(Cursor c){

        if(c.getCount() == 0)
            return null;

        ArrayList<PanierWallet> retours = new ArrayList<>();
        ArrayList<Paquet> paquets = new ArrayList<>();

        PanierWallet panierWallet = new PanierWallet();
        int id_courant = 0;

        AlimentDAO alimentDAO = new AlimentDAO(context);
        alimentDAO.openR();

        c.moveToFirst();
        id_courant = c.getInt(BDDConstantes.PANIER_WALLET_ID_PANIER_ID);

        do{

            if(id_courant != c.getInt(BDDConstantes.PANIER_WALLET_ID_PANIER_ID)){

                panierWallet.setPaquetArrayList(paquets);
                retours.add(panierWallet);

                panierWallet = new PanierWallet();
                paquets = new ArrayList<Paquet>();
                id_courant = c.getInt(BDDConstantes.PANIER_WALLET_ID_PANIER_ID);
            }

            Log.e("PWDAO", "id : "+c.getInt(BDDConstantes.PANIER_WALLET_ID_PANIER_ID)+" ; Aliment : "+ c.getInt(BDDConstantes.PANIER_WALLET_ID_ALIMENT_ID));

            panierWallet.setId(id_courant);
            panierWallet.setDateOperation(c.getString(BDDConstantes.PANIER_WALLET_DATE_ID));

            Paquet paquet = new Paquet();
            paquet.setAliment(alimentDAO.getAliment(c.getInt(BDDConstantes.PANIER_WALLET_ID_ALIMENT_ID)));
            paquet.getAliment().setPrix(c.getInt(BDDConstantes.PANIER_WALLET_PRIX_ID));
            paquets.add(paquet);

        }while(c.moveToNext());

        panierWallet.setPaquetArrayList(paquets);
        retours.add(panierWallet);

        // On ferme le curseur
        c.close();

        alimentDAO.close();

        // On retourne les informations des aliments trouvé
        return retours;
    }
}
