package com.example.cresu.projectr.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
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

public class ConnexionActivity extends AppCompatActivity {

    private final static String TAG = ConnexionActivity.class.getSimpleName();
    private EditText input_id;

    private EditText input_password;

    private Button btn_connexion;

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
        setContentView(R.layout.activity_connexion);

        if(VariablesGlobales.getInstance().getUser().recupererUtilisateur() != null){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        input_id = (EditText)findViewById(R.id.input_id);

        input_password = (EditText)findViewById(R.id.input_password);

        btn_connexion = (Button)findViewById(R.id.btn_connexion);
        btn_connexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_connexion.setEnabled(false);
                connexion();
            }
        });

        btn_inscription = (Button)findViewById(R.id.btn_inscription);
        btn_inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConnexionActivity.this, InscriptionActivity.class));
            }
        });

        verifyStoragePermissions(this);
    }

    public void connexion(){

        final String pseudo = input_id.getText().toString();
        final String password = input_password.getText().toString();

        if (TextUtils.isEmpty(pseudo) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Tous les champs ne sont pas remplis", Toast.LENGTH_SHORT).show();
            btn_connexion.setEnabled(true);
            return;
        }

        StringRequest strReq = new StringRequest(Request.Method.POST,
                HttpRequestConstantes.CONNEXION, new Response.Listener<String>() {

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
                        btn_connexion.setEnabled(true);
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    btn_connexion.setEnabled(true);
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                btn_connexion.setEnabled(true);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nom", pseudo);
                params.put("password", password);

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
