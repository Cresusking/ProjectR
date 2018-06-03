package com.example.cresu.projectr.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.cresu.projectr.R;
import com.example.cresu.projectr.constantes.HttpRequestConstantes;
import com.example.cresu.projectr.modeles.Utilisateur;
import com.example.cresu.projectr.utils.VariablesGlobales;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InscriptionActivity extends AppCompatActivity {

    private final static String TAG = InscriptionActivity.class.getSimpleName();

    private EditText input_pseudo;

    private EditText input_password;

    private EditText input_telephone;

    private EditText input_paiement;

    private EditText input_pays;

    private EditText input_ville;

    private EditText input_quartier;

    private Button btn_inscription;

    /* CHECKING PERMISSION */
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        input_pseudo = (EditText)findViewById(R.id.input_pseudo);
        input_password = (EditText)findViewById(R.id.input_password);
        input_telephone = (EditText)findViewById(R.id.input_telephone);
        input_paiement = (EditText)findViewById(R.id.input_paiement);
        input_pays = (EditText)findViewById(R.id.input_pays);
        input_ville = (EditText)findViewById(R.id.input_ville);
        input_quartier = (EditText)findViewById(R.id.input_quartier);

        btn_inscription = (Button)findViewById(R.id.btn_inscription);
        btn_inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inscription();
            }
        });

        verifyStoragePermissions(this);
    }

    public void inscription(){

        final String pseudo = input_pseudo.getText().toString();
        final String password = input_password.getText().toString();
        final String telephone = input_telephone.getText().toString();
        final String paiement = input_paiement.getText().toString();
        final String pays = input_pays.getText().toString();
        final String ville = input_ville.getText().toString();
        final String quartier = input_quartier.getText().toString();

        if (TextUtils.isEmpty(pseudo) || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(telephone) || TextUtils.isEmpty(paiement) || TextUtils.isEmpty(pays)
                 || TextUtils.isEmpty(ville) || TextUtils.isEmpty(quartier)) {
            Toast.makeText(this, "Tous les champs obligatoires ne sont pas remplis", Toast.LENGTH_SHORT).show();
            return;
        }

        if(pseudo.length() < 5){
            Toast.makeText(this, "Votre pseudo doit contenir au moins 5 caractères", Toast.LENGTH_SHORT).show();
            return;
        }

        if(telephone.length() < 5){
            Toast.makeText(this, "Votre premier doit contenir au moins 5 chiffres", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                HttpRequestConstantes.INSCRIPTION, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("statut") == true) {
                        // user successfully logged in

                        JSONObject userObj = obj.getJSONObject("resultat");
                        Utilisateur user = new Utilisateur(userObj.getString("id"),
                                userObj.getString("nom"),
                                "",
                                userObj.getString("numero_de_telephone"),
                                userObj.getString("numero_de_paiement"),
                                userObj.getLong("solde"),
                                userObj.getString("pays"),
                                userObj.getString("ville"),
                                userObj.getString("quartier"));

                        VariablesGlobales.getInstance().getUser().enregistrerUnUtilisateur(user);

                        // storing user in shared preferences and database content
                        // MyApplication.getInstance().getPrefManager().storeUser(user);

                        // start main activity
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();

                    } else {
                        // login error - simply toast the message
                        Toast.makeText(getApplicationContext(), "" + obj.getString("message"), Toast.LENGTH_LONG).show();
                    }

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
                params.put("nom", pseudo);
                params.put("password", password);
                params.put("numero_de_telephone", telephone);
                params.put("numero_de_paiement", paiement);
                params.put("pays", pays);
                params.put("ville", ville);
                params.put("quartier", quartier);

                Log.e(TAG, "params: " + params.toString());
                return params;
            }
        };

        //Adding request to request queue
        VariablesGlobales.getInstance().addToRequestQueue(strReq);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)){

        }else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        }
    }

    public static void verifyStoragePermissions(Activity activity){

        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if(permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            //Toast.makeText(activity, "Passage par permission", Toast.LENGTH_SHORT).show();
        }
    }
}
