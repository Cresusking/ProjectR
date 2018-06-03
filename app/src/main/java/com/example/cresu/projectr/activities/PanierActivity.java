package com.example.cresu.projectr.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.cresu.projectr.R;
import com.example.cresu.projectr.adapters.PanierAdapter;
import com.example.cresu.projectr.constantes.HttpRequestConstantes;
import com.example.cresu.projectr.modeles.Paquet;
import com.example.cresu.projectr.sqlites.PanierWalletDAO;
import com.example.cresu.projectr.utils.VariablesGlobales;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PanierActivity extends AppCompatActivity {

    private final static String TAG = PanierActivity.class.getSimpleName();

    private RecyclerView recyclerView;

    private TextView prix_total_provisoire;

    private FloatingActionButton valider_commande_btn;

    private PanierAdapter mAdapter;

    private ArrayList<Paquet> listOfPaquets;

    private boolean isReused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panier);

        /*
        Pour éviter le retour vers MainActivity */
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        valider_commande_btn = (FloatingActionButton) findViewById(R.id.valider_commande_btn);
        valider_commande_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForConfirmation();
            }
        });

        prix_total_provisoire = (TextView)findViewById(R.id.prix_total);
        prix_total_provisoire.setText(""+VariablesGlobales.getInstance().getPrix_total());

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        listOfPaquets = new ArrayList<>();
        listOfPaquets = VariablesGlobales.getInstance().getPaniers();


        /* Configuration du Customiser et de la liste graphique*/
        mAdapter = new PanierAdapter(this, listOfPaquets, prix_total_provisoire);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        // Ecouteur (Listener) placé sur la liste graphique pour détecter le clic
        recyclerView.addOnItemTouchListener(new PanierAdapter.RecyclerTouchListener(getApplicationContext(), recyclerView, new PanierAdapter.ClickListener() {

            Intent intent;

            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                // Prévoir un truc pour l'appui long ou pas.
            }
        }));
    }

    public void askForConfirmation(){

        // Instanciation de notre layout en tant que View
        LayoutInflater factory = LayoutInflater.from(this);
        final View creationDialog = factory.inflate(R.layout.achat_ui, null);

        final EditText input_password = (EditText) creationDialog.findViewById(R.id.input_password);

        // Creation de l'AlertDialog
        final android.app.AlertDialog.Builder adb = new android.app.AlertDialog.Builder(this);

        // On lui donne un titre
        //adb.setTitle(getResources().getString(R.string.information_title));
        adb.setTitle("ATTENTION !!!");

        adb.setView(creationDialog);

        // On modifie l'icône de l'AlertDialog
        adb.setIcon(android.R.drawable.ic_menu_info_details);

        adb.setPositiveButton("OUI", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                long solde = VariablesGlobales.getInstance().getUser().recupererUtilisateur().getSolde();
                long cout_commande = Long.valueOf(prix_total_provisoire.getText().toString());

                if(TextUtils.isEmpty(input_password.getText().toString())){
                    Toast.makeText(PanierActivity.this, "Vous n'avez pas entrez le mot de passe", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(solde - cout_commande >= 0){

                    effectuerLeTransfert(HttpRequestConstantes.COMMERCANT, ""+cout_commande, input_password.getText().toString());

                }else{
                    Toast.makeText(PanierActivity.this, "Votre solde n'est pas suffisant", Toast.LENGTH_SHORT).show();
                }

            }
        });

        adb.setNegativeButton("NON", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        adb.show();
    }

    public void realiserLaCommande(final String password){

    }

    public void effectuerLeTransfert(final String identifiant, final String montant, final String password){

        StringRequest strReq = new StringRequest(Request.Method.POST,
                HttpRequestConstantes.TRANSFERT, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    if(obj.getBoolean("statut") == true){
                        long retrait = (-1) * Long.valueOf(montant);
                        Log.e("Transfert", ""+retrait);
                        VariablesGlobales.getInstance().getUser().addSolde(retrait);

                        PanierWalletDAO panierWalletDAO = new PanierWalletDAO(PanierActivity.this);
                        panierWalletDAO.openW();

                        panierWalletDAO.ajouterTousLePanier();

                        panierWalletDAO.close();

                        startActivity(new Intent(PanierActivity.this, WalletActivity.class));
                        finish();

                    }

                    Toast.makeText(getApplicationContext(), "" + obj.getString("resultat"), Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_emetteur", VariablesGlobales.getInstance().getUser().recupererUtilisateur().getId());
                params.put("id_destinataire", identifiant);
                params.put("new_solde", montant);
                params.put("password", password);

                Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };

        //Adding request to request queue
        VariablesGlobales.getInstance().addToRequestQueue(strReq);
    }
}
