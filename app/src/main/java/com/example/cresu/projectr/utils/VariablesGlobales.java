package com.example.cresu.projectr.utils;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cresu.projectr.activities.MainActivity;
import com.example.cresu.projectr.constantes.HttpRequestConstantes;
import com.example.cresu.projectr.modeles.Paquet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by cresu on 24/03/2018.
 */

public class VariablesGlobales extends Application {

    private static volatile VariablesGlobales mInstance = null;

    private ArrayList<Paquet> paniers;

    private int prix_total;

    private int categorie;

    private UserData user;

    private RequestQueue requestQueue;

    public static final String TAG = VariablesGlobales.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        paniers = new ArrayList<Paquet>();
        prix_total = 0;
        user = new UserData(this);
        mInstance = this;
    }

    public static synchronized VariablesGlobales getInstance(){
        return mInstance;
    }

    public ArrayList<Paquet> getPaniers() {
        return paniers;
    }

    public void setPaniers(ArrayList<Paquet> paniers) {
        this.paniers = paniers;
    }

    public int getPrix_total() {
        return prix_total;
    }

    public void setPrix_total(int prix_total) {
        this.prix_total = prix_total;
    }

    public int getCategorie() {
        return categorie;
    }

    public void setCategorie(int categorie) {
        this.categorie = categorie;
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request){
        request.setTag(TAG);
        getRequestQueue().add(request);
    }

    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }

    // Exécuté au moment de la réception d'un transfert
    public void updateSolde(final TextView soldeTextView){
        String endPoint = HttpRequestConstantes.GET_TRANSFERT;

        Log.e(TAG, "endPoint: " + endPoint.replace("_ID_", VariablesGlobales.getInstance().getUser().recupererUtilisateur().getId()));

        StringRequest strReq = new StringRequest(Request.Method.GET,
                endPoint.replace("_ID_", VariablesGlobales.getInstance().getUser().recupererUtilisateur().getId()), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check statut
                    if (obj.getBoolean("statut") == true) {

                        // Vérifier qu'une opération a été effectué
                        //checkForOperation(VariablesGlobales.getInstance().getUser().recupererUtilisateur().getSolde(), Long.valueOf(obj.getLong("solde")));

                        // Effectuer la mise à jour du solde
                        VariablesGlobales.getInstance().getUser().setNewSolde(Long.valueOf(obj.getLong("solde")));

                        // Mettre à jour l'affichage aussi si besoin
                        if(soldeTextView != null){
                            soldeTextView.setText(""+obj.getLong("solde"));
                        }
                    } else {
                        Log.e(TAG, "" + obj.getString("message"));

                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());

                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);

            }
        });

        //Adding request to request queue
        VariablesGlobales.getInstance().addToRequestQueue(strReq);

    }

    private void checkForOperation(long old_solde, long new_solde){

        Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());

        if(old_solde < new_solde){

            notificationUtils.showNotificationMessage("Transfert Reçu","Vous avez reçu un transfert controlez votre solde","", resultIntent);

        }else if (old_solde > new_solde){
            notificationUtils.showNotificationMessage("Transfert Envoyé","Vous avez envoyé de l'argent. Contrôlez cela peut être une fraude.","", resultIntent);
        }
    }

}
