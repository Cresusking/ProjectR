package com.example.cresu.projectr.constantes;

import com.example.cresu.projectr.utils.VariablesGlobales;

/**
 * Created by cresu on 23/04/2018.
 */

public class HttpRequestConstantes {

    public static final String BROKER = "broker.hivemq.com";
    public static final String TOPIC = VariablesGlobales.getInstance().getUser().recupererUtilisateur().getId(); //"RPROJ876324346536";
    public static final String HOST = "http://projetr.000webhostapp.com/";
    public static final String MAIN_URL = HOST + "e-yam/v1/";
    public static final String INDEX = MAIN_URL + "index.php";


    public static final String CONNEXION = INDEX + "/user/connexion";
    public static final String INSCRIPTION = INDEX + "/user/subscribe";
    public static final String UPDATE = INDEX + "/user/update";
    public static final String TRANSFERT = INDEX + "/user/transfert";
    public static final String GET_TRANSFERT = INDEX + "/update_solde/_ID_";

    public static final String GET_CARTE = INDEX + "/carte_r/_ID_/_NUMERO_";

    public static final String COMMERCANT = "commercant76";
    public static final String COMMANDE = INDEX + "/user/commande";
}
