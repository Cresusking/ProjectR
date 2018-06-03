package com.example.cresu.projectr.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.cresu.projectr.constantes.AppConstantes;
import com.example.cresu.projectr.modeles.Utilisateur;

/**
 * Created by cresu on 02/04/2018.
 */

public class UserData {

    public String TAG = UserData.class.getSimpleName();

    private SharedPreferences preferences;

    private SharedPreferences.Editor editor;

    private Context mContext;

    private int PRIVATE_MODE = 0;

    public UserData(Context context){
        this.mContext = context;
        preferences = context.getSharedPreferences(AppConstantes.USER_PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void enregistrerUnUtilisateur(Utilisateur utilisateur){
        editor.putString(AppConstantes.KEY_USER_ID, utilisateur.getId());
        editor.putString(AppConstantes.KEY_USER_PSEUDO, utilisateur.getPseudo());
        editor.putString(AppConstantes.KEY_USER_MOT_DE_PASSE, utilisateur.getMot_de_passe());
        editor.putString(AppConstantes.KEY_USER_TELEPHONE, utilisateur.getNumero_de_telephone());
        editor.putString(AppConstantes.KEY_USER_PAIEMENT, utilisateur.getNumero_de_paiement());
        editor.putLong(AppConstantes.KEY_USER_SOLDE, utilisateur.getSolde());
        editor.putString(AppConstantes.KEY_USER_PAYS, utilisateur.getPays());
        editor.putString(AppConstantes.KEY_USER_VILLE, utilisateur.getVille());
        editor.putString(AppConstantes.KEY_USER_QUARTIER, utilisateur.getQuartier());

        editor.commit();
    }

    public void updateUnUtilisateur(Utilisateur utilisateur){
        editor.putString(AppConstantes.KEY_USER_PSEUDO, utilisateur.getPseudo());
        editor.putString(AppConstantes.KEY_USER_MOT_DE_PASSE, utilisateur.getMot_de_passe());
        editor.putString(AppConstantes.KEY_USER_TELEPHONE, utilisateur.getNumero_de_telephone());
        editor.putString(AppConstantes.KEY_USER_PAIEMENT, utilisateur.getNumero_de_paiement());
        editor.putString(AppConstantes.KEY_USER_PAYS, utilisateur.getPays());
        editor.putString(AppConstantes.KEY_USER_VILLE, utilisateur.getVille());
        editor.putString(AppConstantes.KEY_USER_QUARTIER, utilisateur.getQuartier());

        editor.commit();
    }

    public void addSolde(long newSolde){
        long oldSolde = preferences.getLong(AppConstantes.KEY_USER_SOLDE, 0);
        editor.putLong(AppConstantes.KEY_USER_SOLDE, (oldSolde + newSolde));

        editor.commit();
    }

    public void setNewSolde(long newSolde){
        long oldSolde = preferences.getLong(AppConstantes.KEY_USER_SOLDE, 0);
        editor.putLong(AppConstantes.KEY_USER_SOLDE, newSolde);

        editor.commit();
    }

    public void nettoyerPreference(){
        editor.clear();
        editor.commit();
    }

    public Utilisateur recupererUtilisateur() {
        String id, pseudo, numero_de_telephone, numero_de_paiement, pays, ville, quartier;
        long solde;
        String mot_de_passe;

        id = preferences.getString(AppConstantes.KEY_USER_ID, null);

        if(id != null) {

            pseudo = preferences.getString(AppConstantes.KEY_USER_PSEUDO, null);
            mot_de_passe = preferences.getString(AppConstantes.KEY_USER_MOT_DE_PASSE, null);
            numero_de_telephone = preferences.getString(AppConstantes.KEY_USER_TELEPHONE, null);
            numero_de_paiement = preferences.getString(AppConstantes.KEY_USER_PAIEMENT, null);
            solde = preferences.getLong(AppConstantes.KEY_USER_SOLDE, 0);
            pays = preferences.getString(AppConstantes.KEY_USER_PAYS, null);
            ville = preferences.getString(AppConstantes.KEY_USER_VILLE, null);
            quartier = preferences.getString(AppConstantes.KEY_USER_QUARTIER, null);

            Utilisateur utilisateur = new Utilisateur(id, pseudo, mot_de_passe, numero_de_telephone, numero_de_paiement, solde, pays, ville, quartier);
            return utilisateur;
        }
        return null;
    }

    public void addNotification(String notification) {

        // get old notifications
        String oldNotifications = getNotifications();

        if (oldNotifications != null) {
            oldNotifications += "|" + notification;
        } else {
            oldNotifications = notification;
        }

        editor.putString(AppConstantes.KEY_NOTIFICATIONS, oldNotifications);
        editor.commit();
    }

    public String getNotifications() {
        return preferences.getString(AppConstantes.KEY_NOTIFICATIONS, null);
    }

    /*public Utilisateur recupererUtilisateur() {
        if (preferences.getString(AppConstantes.KEY_USER_NUMERO_CLIENT, null) != null) {
            int id, numero_client, solde;
            String mot_de_passe;

            numero_client = preferences.getInt(AppConstantes.KEY_USER_NUMERO_CLIENT, 0);
            mot_de_passe = preferences.getString(AppConstantes.KEY_USER_MOT_DE_PASSE, "rien");
            solde = preferences.getInt(AppConstantes.KEY_USER_SOLDE, 0);

            Utilisateur utilisateur = new Utilisateur(numero_client, mot_de_passe, solde);
            return utilisateur;
        }
        return null;
    }*/
}
