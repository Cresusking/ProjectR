package com.example.cresu.projectr.constantes;

/**
 * Created by cresu on 24/03/2018.
 */

public class BDDConstantes {

    /* Information sur la base de donnée */
    public final static String BD_NOM = "projet.db";
    public final static int VERSION = 1;

    /* Informations de la table aliment */
    public final static String TABLE_ALIMENT = "aliment";

    public final static String ALIMENT_ID = "id";
    public final static int ALIMENT_ID_ID = 0;
    public final static String ALIMENT_NOM = "nom";
    public final static int ALIMENT_NOM_ID = 1;
    public final static String ALIMENT_CATEGORIE = "categorie";
    public final static int ALIMENT_CATEGORIE_ID = 2;
    public final static String ALIMENT_IMAGE_ID = "image_id";
    public final static int ALIMENT_IMAGE_ID_ID = 3;
    public final static String ALIMENT_PRIX = "prix";
    public final static int ALIMENT_PRIX_ID = 4;
    public final static String ALIMENT_PAS_DE_PRIX = "pas_de_prix";
    public final static int ALIMENT_PAS_DE_PRIX_ID = 5;


    public final static String CREATE_TABLE_ALIMENT = "CREATE TABLE "+TABLE_ALIMENT+"( "
            +ALIMENT_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +ALIMENT_NOM+" TEXT NOT NULL, "
            +ALIMENT_CATEGORIE+" TEXT NOT NULL, "
            +ALIMENT_IMAGE_ID+" TEXT NOT NULL, "
            +ALIMENT_PRIX+" TEXT NOT NULL, "
            +ALIMENT_PAS_DE_PRIX+" TEXT NOT NULL);";

    public final static String DROP_TABLE_ALIMENT = "DROP TABLE IF EXISTS "+TABLE_ALIMENT;


    /* Informations de la table categorie d'aliment */
    public final static String TABLE_CATEGORIE_ALIMENT = "categorie_aliment";

    public final static String CATEGORIE_ALIMENT_ID = "id";
    public final static int CATEGORIE_ALIMENT_ID_ID = 0;
    public final static String CATEGORIE_ALIMENT_NOM = "nom";
    public final static int CATEGORIE_ALIMENT_NOM_ID = 1;
    public final static String CATEGORIE_ALIMENT_IMAGE_ID = "image_id";
    public final static int CATEGORIE_ALIMENT_IMAGE_ID_ID = 2;


    public final static String CREATE_TABLE_CATEGORIE_ALIMENT = "CREATE TABLE "+TABLE_CATEGORIE_ALIMENT+"( "
            +CATEGORIE_ALIMENT_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +CATEGORIE_ALIMENT_NOM+" TEXT NOT NULL, "
            +CATEGORIE_ALIMENT_IMAGE_ID+" TEXT NOT NULL);";

    public final static String DROP_TABLE_CATEGORIE_ALIMENT = "DROP TABLE IF EXISTS "+TABLE_CATEGORIE_ALIMENT;

    /* Informations de la table panier wallet */
    public final static String TABLE_PANIER_WALLET = "panier_wallet";

    public final static String PANIER_WALLET_ID = "id";
    public final static int PANIER_WALLET_ID_ID = 0;
    public final static String PANIER_WALLET_ID_PANIER = "id_panier";
    public final static int PANIER_WALLET_ID_PANIER_ID = 1;
    public final static String PANIER_WALLET_ID_ALIMENT = "id_aliment";
    public final static int PANIER_WALLET_ID_ALIMENT_ID = 2;
    public final static String PANIER_WALLET_PRIX = "prix";
    public final static int PANIER_WALLET_PRIX_ID = 3;
    public final static String PANIER_WALLET_DATE = "date";
    public final static int PANIER_WALLET_DATE_ID = 4;


    public final static String CREATE_TABLE_PANIER_WALLET = "CREATE TABLE "+TABLE_PANIER_WALLET+"( "
            +PANIER_WALLET_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +PANIER_WALLET_ID_PANIER+" TEXT NOT NULL, "
            +PANIER_WALLET_ID_ALIMENT+" TEXT NOT NULL, "
            +PANIER_WALLET_PRIX+" TEXT NOT NULL, "
            +PANIER_WALLET_DATE+" TEXT NOT NULL);";

    public final static String DROP_TABLE_PANIER_WALLET = "DROP TABLE IF EXISTS "+TABLE_PANIER_WALLET;

    /* Informations de la table receiver */
    public final static String TABLE_RECEIVER = "receiver";

    public final static String RECEIVER_ID = "id";
    public final static int RECEIVER_ID_ID = 0;
    public final static String RECEIVER_ID_RECEIVER = "id_receiver";
    public final static int RECEIVER_ID_RECEIVER_ID = 1;
    public final static String RECEIVER_NOM = "nom";
    public final static int RECEIVER_ID_NOM_ID = 2;


    public final static String CREATE_TABLE_RECEIVER = "CREATE TABLE "+TABLE_RECEIVER+"( "
            +RECEIVER_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
            +RECEIVER_ID_RECEIVER+" TEXT NOT NULL, "
            +RECEIVER_NOM+" TEXT NOT NULL);";

    public final static String DROP_TABLE_RECEIVER = "DROP TABLE IF EXISTS "+TABLE_RECEIVER;


}
